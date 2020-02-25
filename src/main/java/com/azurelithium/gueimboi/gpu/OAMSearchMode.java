package com.azurelithium.gueimboi.gpu;

import java.util.Comparator;
import java.util.LinkedList;

import com.azurelithium.gueimboi.gpu.GPU.GPURegisters;
import com.azurelithium.gueimboi.memory.MMU;

class OAMSearchMode extends GPUMode {
    
    private final int MAX_SPRITES_PER_LINE = 10;
    private final int OAM_MAP_ADDRESS_START = 0xFE00;
    private final int SPRITE_BYTE_SIZE = 4;

    private final int DURATION_TICKS = 80;    
    private final int TICKS_PER_M_CYCLE = 2;

    private GPURegisters GPURegisters;
    private MMU MMU;

    private LinkedList<Sprite> sprites = new LinkedList<Sprite>();
    private int nextSpriteAddress = OAM_MAP_ADDRESS_START;

    OAMSearchMode(GPURegisters _GPURegisters, MMU _MMU) {
        this.GPURegisters = _GPURegisters;
        MMU = _MMU;
    }
    
    void tick() {
        if (ticksInMode % TICKS_PER_M_CYCLE == 0) {
            if (GPURegisters.isOBJEnabled() && sprites.size() < MAX_SPRITES_PER_LINE) {
                int LY = GPURegisters.getLY();

                // 16 bit bus lets us read 2 bytes per tick
                int Y = MMU.readByte(nextSpriteAddress);
                int X = MMU.readByte(nextSpriteAddress + 1);

                if (Y - 16 <= LY && LY < Y - 16 + (GPURegisters.isOBJDoubleSizeEnabled() ? 16 : 8)) {
                    sprites.add(new Sprite(Y, X, nextSpriteAddress));
                }

                nextSpriteAddress += SPRITE_BYTE_SIZE;
            }
        }
        ticksInMode++;
    }

    boolean isFinished() {
        return ticksInMode == DURATION_TICKS;
    }

    LinkedList<Sprite> getSprites() {
        Comparator<Sprite> spriteComparator = new Comparator<Sprite>() {
            public int compare(Sprite s1, Sprite s2) {
                int compareXPosition = ((Integer)s1.getX()).compareTo(s2.getX());

                if (compareXPosition != 0) {
                    return compareXPosition;
                } 

                int compareOAMPosition = ((Integer)s2.getAddress()).compareTo(s1.getAddress());

                return compareOAMPosition;
            }
        };
        
        sprites.sort(spriteComparator);
        return sprites;
    }

}