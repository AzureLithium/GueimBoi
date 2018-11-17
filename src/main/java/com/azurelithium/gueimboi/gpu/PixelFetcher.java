package com.azurelithium.gueimboi.gpu;

import java.util.LinkedList;
import com.azurelithium.gueimboi.gpu.GPU.GPURegisters;

class PixelFetcher {

    private final int TILE_DIMENSION = 8;
    private final int TILE_BYTE_LENGTH = 16;
    private final int TILE_BYTES_PER_PIXELBLOCK = 2;
    private final int TILEMAP_WIDTH = 32;

    private enum PixelFetcherState {
        READ_TILE_ID,
        READ_BLOCK_DATA_1,
        READ_BLOCK_DATA_2,
        INSERT_BLOCK
    }
    private PixelFetcherState STATE;

    private GPURegisters gpuRegisters;
    private PixelFIFO pixelFIFO;

    private int tileID;
    private int blockData1;
    private int blockData2;

    PixelFetcher(GPURegisters _gpuRegisters, PixelFIFO _pixelFIFO) {
        gpuRegisters = _gpuRegisters;
        pixelFIFO = _pixelFIFO;
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
                    pixelFIFO.insertBlock(mergeDataBlocks());
                }
        }
    }

    void readTileID() {
        int tileColumn = (pixelFIFO.getPixelOffset() + pixelFIFO.size() + gpuRegisters.getSCX()) / TILE_DIMENSION;
        int tileRow = (pixelFIFO.getLineOffset() + gpuRegisters.getSCY()) / TILE_DIMENSION;
        int BGTileMapAddress = gpuRegisters.getBGTileMapAddress();
        int tileAddress = BGTileMapAddress + (tileRow % TILEMAP_WIDTH) * TILEMAP_WIDTH + (tileColumn % TILEMAP_WIDTH); //wrap control
        tileID = gpuRegisters.mmu.readByte(tileAddress);
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

    LinkedList<Integer> mergeDataBlocks() {
        LinkedList<Integer> pixelBlock = new LinkedList<Integer>();
        int mask;
        int[] BGP = gpuRegisters.getBGP();         
        for (int i=0; i<TILE_DIMENSION; i++) {
            mask = 1 << i;
            int colorNumber = (((blockData1 & mask) >> i) << 1) + ((blockData2 & mask) >> i);
            pixelBlock.addFirst(BGP[colorNumber]);
        }
        STATE = PixelFetcherState.READ_TILE_ID;
        return pixelBlock;
    }

}