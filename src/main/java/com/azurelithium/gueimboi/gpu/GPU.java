package com.azurelithium.gueimboi.gpu;

import com.azurelithium.gueimboi.common.Component;
import com.azurelithium.gueimboi.gui.Display;
import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.memory.MemRegisterEnum;
import com.azurelithium.gueimboi.utils.ByteUtils;

public class GPU extends Component {

    class GPURegisters {

        final int BG_DISPLAY_ENABLED_BIT = 0;
        final int OBJ_DISPLAY_ENABLED_BIT = 1;
        final int OBJ_DOUBLE_SPRITE_SIZE_BIT = 2;
        final int BG_TILE_MAP_ADDRESS_BIT = 3;
        final int BG_TILE_MAP_ADDRESS_0 = 0x9800;
        final int BG_TILE_MAP_ADDRESS_1 = 0x9C00;
        final int TILE_DATA_ADDRESS_BIT = 4;
        final int TILE_DATA_ADDRESS_0 = 0x8800;
        final int TILE_DATA_ADDRESS_1 = 0x8000;
        final int LCD_ENABLED_BIT = 7;

        MMU mmu;
        
        GPURegisters(MMU _mmu) {
            mmu = _mmu;
        }

        int getLCDC() {
            return mmu.getMemRegister(MemRegisterEnum.LCDC);
        }

        boolean isLCDEnabled() {
            int LCDC = getLCDC();
            return ByteUtils.getBit(LCDC, LCD_ENABLED_BIT);
        }

        boolean isBGEnabled() {
            int LCDC = getLCDC();
            return ByteUtils.getBit(LCDC, BG_DISPLAY_ENABLED_BIT);
        }

        boolean isOBJEnabled() {
            int LCDC = getLCDC();
            return ByteUtils.getBit(LCDC, OBJ_DISPLAY_ENABLED_BIT);
        }

        boolean isOBJDoubleSizeEnabled() {
            int LCDC = getLCDC();
            return ByteUtils.getBit(LCDC, OBJ_DOUBLE_SPRITE_SIZE_BIT);
        }

        int getSTAT() {
            return mmu.getMemRegister(MemRegisterEnum.STAT);
        }

        void setSTAT(int value) {
            mmu.setMemRegister(MemRegisterEnum.STAT, value);
        }

        int getBGTileMapAddress() {
            int LCDC = getLCDC();
            return (ByteUtils.getBit(LCDC, BG_TILE_MAP_ADDRESS_BIT) ? BG_TILE_MAP_ADDRESS_1 : BG_TILE_MAP_ADDRESS_0);
        }

        int getTileDataAddress() {
            int LCDC = getLCDC();
            return (ByteUtils.getBit(LCDC, TILE_DATA_ADDRESS_BIT) ? TILE_DATA_ADDRESS_1 : TILE_DATA_ADDRESS_0);
        }        

        int getSCY() {
            return mmu.getMemRegister(MemRegisterEnum.SCY);
        }

        int getSCX() {
            return mmu.getMemRegister(MemRegisterEnum.SCX);
        }

        int getLY() {
            return mmu.getMemRegister(MemRegisterEnum.LY);
        }
    
        void incrementLY() {
            int LY = getLY();
            mmu.setMemRegister(MemRegisterEnum.LY, ++LY % LINES_PER_FRAME);
        }

        void resetLY() {
            mmu.setMemRegister(MemRegisterEnum.LY, 0);
        }

        int[] getBGP() {
            int BGP = mmu.getMemRegister(MemRegisterEnum.BGP);
            int[] grayShades = new int[] {
                BGP & 0x3,
                BGP >> 2 & 0x3,
                BGP >> 4 & 0x3,
                BGP >> 6 & 0x3 
            };
            return grayShades;
        }

    }

    private final int VBLANK_INT_REQUEST_BIT = 0;

    private final int HBLANK_EARLIEST_START_TICK = 252;
    private final int VBLANK_START_LINE;
    private final int LINES_PER_FRAME;

    private enum Mode {
        HBLANK,         // mode 0
        VBLANK,         // mode 1
        OAM_SEARCH,     // mode 2
        PIXEL_TRANSFER  // mode 3
    }
    private Mode GPU_MODE;

    private Display display;
    private GPURegisters gpuRegisters;
    private GPUMode gpuMode;
    private int ticksInLine;

    public GPU(Display _display, MMU _mmu) {
        display = _display;
        VBLANK_START_LINE = display.getGameboyLCDHeight();
        LINES_PER_FRAME = VBLANK_START_LINE + 10;
        gpuRegisters = new GPURegisters(_mmu);
        GPU_MODE = Mode.OAM_SEARCH;
        gpuMode = new OAMSearchMode();
    }

    public void tick() {
        if (!gpuRegisters.isLCDEnabled()) return;
        gpuMode.tick();
        ticksInLine++;
        if (gpuMode.isFinished()) {
            switch (GPU_MODE) {
                case OAM_SEARCH:                    
                    GPU_MODE = Mode.PIXEL_TRANSFER;
                    gpuMode = new PixelTransferMode(gpuRegisters, display);
                    break;
                case PIXEL_TRANSFER:
                    GPU_MODE = Mode.HBLANK;
                    gpuMode = new HBlankMode(ticksInLine - HBLANK_EARLIEST_START_TICK);
                    break;
                case HBLANK:
                    gpuRegisters.incrementLY();
                    if (gpuRegisters.getLY() < VBLANK_START_LINE) {
                        GPU_MODE = Mode.OAM_SEARCH;
                        gpuMode = new OAMSearchMode();                        
                    } else {
                        GPU_MODE = Mode.VBLANK;
                        gpuMode = new VBlankMode();
                        requestVBlankInterrupt();
                    }
                    ticksInLine = 0;
                    break;
                case VBLANK:
                    gpuRegisters.incrementLY();
                    if (gpuRegisters.getLY() == 0) {
                        GPU_MODE = Mode.OAM_SEARCH;                        
                        gpuMode = new OAMSearchMode();                        
                    } else {
                        gpuMode = new VBlankMode();
                    }
                    ticksInLine = 0;
                    break;
            }
            setSTATMode(GPU_MODE.ordinal());
        }
    }

    public void reset() {
        gpuRegisters.resetLY();
        ticksInLine = 0;
        GPU_MODE = Mode.OAM_SEARCH;                        
        gpuMode = new OAMSearchMode();
    }

    private void requestVBlankInterrupt() {
        int IF = gpuRegisters.mmu.getMemRegister(MemRegisterEnum.IF);
        IF = ByteUtils.setBit(IF, VBLANK_INT_REQUEST_BIT);
        gpuRegisters.mmu.setMemRegister(MemRegisterEnum.IF, IF);
    }

    private void setSTATMode(int mode) {
        int STAT = gpuRegisters.getSTAT();
        STAT &= 0xC;
        STAT |= mode &= 0x3;
        gpuRegisters.setSTAT(STAT);
    }

}