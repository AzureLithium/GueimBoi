package com.azurelithium.gueimboi.gpu;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.azurelithium.gueimboi.gpu.GPU.GPURegisters;
import com.azurelithium.gueimboi.gpu.Pixel.PixelType;

class PixelFetcher {

    private final int TILE_DIMENSION = 8;
    private final int TILE_BYTE_LENGTH = 16;
    private final int TILE_BYTES_PER_PIXELBLOCK = 2;
    private final int TILEMAP_WIDTH = 32;

    private enum PixelFetcherState {
        READ_TILE_ID,
        READ_BLOCK_DATA_1,
        READ_BLOCK_DATA_2,
        INSERT_BLOCK,
        SPRITE_READ_TILE_ID,
        SPRITE_READ_ATTRIBUTES,
        SPRITE_READ_BLOCK_DATA_1,
        SPRITE_READ_BLOCK_DATA_2
    }
    private PixelFetcherState STATE;

    private GPURegisters gpuRegisters;
    private PixelFIFO pixelFIFO;
    private final int pixelsPerLine;

    private int tileID;
    private int blockData1;
    private int blockData2;
    private Sprite sprite;

    PixelFetcher(GPURegisters _gpuRegisters, PixelFIFO _pixelFIFO, int _pixelsPerLine) {
        gpuRegisters = _gpuRegisters;
        pixelFIFO = _pixelFIFO;
        pixelsPerLine = _pixelsPerLine;
        STATE = PixelFetcherState.READ_TILE_ID;
    }

    void tick() {
        switch (STATE) {
            case READ_TILE_ID:
                readTileID();
                break;
            case READ_BLOCK_DATA_1:
                readBlockData1();
                break;
            case READ_BLOCK_DATA_2:
                readBlockData2();
            case INSERT_BLOCK:
                if (pixelFIFO.canInsertBlock()) {
                    pixelFIFO.insertBlock(mergeDataBlocks(PixelType.BACKGROUND));                    
                    STATE = PixelFetcherState.READ_TILE_ID;
                }
                break;
            case SPRITE_READ_TILE_ID:
                spriteReadTileID();
                break;
            case SPRITE_READ_ATTRIBUTES:
                spriteReadAttributes();
                break;
            case SPRITE_READ_BLOCK_DATA_1:    
                spriteReadBlockData1();
                break;
            case SPRITE_READ_BLOCK_DATA_2:    
                spriteReadBlockData2();
                break;
        }
    }

    void fetchSprite(Sprite _sprite) {
        sprite = _sprite;
        STATE = PixelFetcherState.SPRITE_READ_TILE_ID;
    }

    void readTileID() {
        int tileColumn = (pixelFIFO.getPixelOffset() + pixelFIFO.size() + gpuRegisters.getSCX()) / TILE_DIMENSION;
        int tileRow = (pixelFIFO.getLineOffset() + gpuRegisters.getSCY()) / TILE_DIMENSION;
        int BGTileMapAddress = gpuRegisters.getBGTileMapAddress();
        int tileAddress = BGTileMapAddress + (tileRow % TILEMAP_WIDTH) * TILEMAP_WIDTH + (tileColumn % TILEMAP_WIDTH); //wrap control
        tileID = gpuRegisters.mmu.readByte(tileAddress);
        if (gpuRegisters.isTileDataAddressSigned()) tileID = (byte) tileID;
        STATE = PixelFetcherState.READ_BLOCK_DATA_1;
    }

    void readBlockData1() {
        int BGTileDataAddress = gpuRegisters.getTileDataAddress() + tileID * TILE_BYTE_LENGTH;
        int absoluteLineOffset = pixelFIFO.getLineOffset() + gpuRegisters.getSCY();
        int blockData1Address = BGTileDataAddress + TILE_BYTES_PER_PIXELBLOCK * (absoluteLineOffset % TILE_DIMENSION);
        blockData1 = gpuRegisters.mmu.readByte(blockData1Address);
        STATE = PixelFetcherState.READ_BLOCK_DATA_2;
    }

    void readBlockData2() {
        int BGTileDataAddress = gpuRegisters.getTileDataAddress() + tileID * TILE_BYTE_LENGTH;
        int absoluteLineOffset = pixelFIFO.getLineOffset() + gpuRegisters.getSCY();
        int blockData2Address = BGTileDataAddress + TILE_BYTES_PER_PIXELBLOCK * (absoluteLineOffset % TILE_DIMENSION) + 1;
        blockData2 = gpuRegisters.mmu.readByte(blockData2Address);
        STATE = PixelFetcherState.INSERT_BLOCK;
    }

    void spriteReadTileID() {
        int spriteTileNumberAddress = sprite.getAddress() + 2;
        int spriteTileID = gpuRegisters.mmu.readByte(spriteTileNumberAddress);
        sprite.setTileNumber(spriteTileID);
        STATE = PixelFetcherState.SPRITE_READ_ATTRIBUTES;
    }

    void spriteReadAttributes() {
        int spriteAttributesAddress = sprite.getAddress() + 3;
        int spriteTileAttributes = gpuRegisters.mmu.readByte(spriteAttributesAddress);
        sprite.setAttributes(spriteTileAttributes);
        STATE = PixelFetcherState.SPRITE_READ_BLOCK_DATA_1;
    }

    void spriteReadBlockData1() {
        int spriteLineOffset = pixelFIFO.getLineOffset() - (sprite.getY() - 16);
        if (sprite.getYFlip()) spriteLineOffset = TILE_DIMENSION - 1 - spriteLineOffset;
        int spriteBlockData1Address = gpuRegisters.getSpriteTileDataAddress() + (sprite.getTileNumber() << 4) + TILE_BYTES_PER_PIXELBLOCK * spriteLineOffset;
        blockData1 = gpuRegisters.mmu.readByte(spriteBlockData1Address);
        STATE = PixelFetcherState.SPRITE_READ_BLOCK_DATA_2;
    }

    void spriteReadBlockData2() {
        int spriteLineOffset = pixelFIFO.getLineOffset() - (sprite.getY() - 16);
        if (sprite.getYFlip()) spriteLineOffset = TILE_DIMENSION - 1 - spriteLineOffset;
        int spriteBlockData2Address = gpuRegisters.getSpriteTileDataAddress() + (sprite.getTileNumber() << 4) + TILE_BYTES_PER_PIXELBLOCK * spriteLineOffset + 1;
        blockData2 = gpuRegisters.mmu.readByte(spriteBlockData2Address);

        pixelFIFO.overlap(mergeDataBlocks(PixelType.SPRITE));

        pixelFIFO.unblock();
        STATE = PixelFetcherState.READ_TILE_ID;
    }

    List<Pixel> mergeDataBlocks(PixelType pixelType) { // creo que va a ser mejor separar en varias funciones segun el pixeltype
        List<Pixel> pixelBlock = new LinkedList<Pixel>();
        int mask;      
        for (int i=0; i<TILE_DIMENSION; i++) {
            mask = 1 << i;
            int colorNumber = (((blockData2 & mask) >> i) << 1) + ((blockData1 & mask) >> i);
            Pixel pixel = new Pixel(colorNumber, pixelType);
            if (pixelType == PixelType.SPRITE) pixel.setSprite(sprite);
            pixelBlock.add(0, pixel);
        }

        if (pixelType == PixelType.SPRITE) {
            if (sprite.getXFlip()) {
                Collections.reverse(pixelBlock);
            }

            if (sprite.getX() < TILE_DIMENSION) {
                pixelBlock = pixelBlock.subList(TILE_DIMENSION - sprite.getX(), TILE_DIMENSION);
                for (int i=0; i < TILE_DIMENSION - sprite.getX(); i++) {
                    Pixel pixel = new Pixel(0, pixelType);
                    pixel.setSprite(sprite);
                    pixelBlock.add(pixel); // fill with translucent pixels 
                }                
            } else if (sprite.getX() > pixelsPerLine && sprite.getX() < pixelsPerLine + TILE_DIMENSION) {
                pixelBlock = pixelBlock.subList(0, pixelsPerLine + TILE_DIMENSION - sprite.getX());
                for (int i=0; i < sprite.getX() - pixelsPerLine; i++) {
                    Pixel pixel = new Pixel(0, pixelType);
                    pixel.setSprite(sprite);
                    pixelBlock.add(0, pixel); // fill with translucent pixels 
                }
            }
        }        

        return pixelBlock;
    }

}