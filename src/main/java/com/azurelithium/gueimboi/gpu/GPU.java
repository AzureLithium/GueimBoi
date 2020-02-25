package com.azurelithium.gueimboi.gpu;

import java.util.LinkedList;

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

        void setSTATLYCFlag(boolean status) {
            int STAT = gpuRegisters.getSTAT();            
            STAT &= 0xB;
            STAT |= ((status ? 1 : 0) << 2);
            setSTAT(STAT);
        }

        void setSTATMode(int mode) {
            int STAT = gpuRegisters.getSTAT();            
            STAT &= 0xC;
            STAT |= (mode &= 0x3);
            setSTAT(STAT);
        }

        int getBGTileMapAddress() {
            int LCDC = getLCDC();
            return (ByteUtils.getBit(LCDC, BG_TILE_MAP_ADDRESS_BIT) ? BG_TILE_MAP_ADDRESS_1 : BG_TILE_MAP_ADDRESS_0);
        }

        boolean isTileDataAddressSigned() {
            int LCDC = getLCDC();
            return ByteUtils.getBit(LCDC, BG_TILE_MAP_ADDRESS_BIT);
        }

        int getTileDataAddress() {
            int LCDC = getLCDC();
            return (ByteUtils.getBit(LCDC, TILE_DATA_ADDRESS_BIT) ? TILE_DATA_ADDRESS_1 : TILE_DATA_ADDRESS_0);
        }
        
        int getSpriteTileDataAddress() {
            return TILE_DATA_ADDRESS_1;
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

        int getLYC() {
            return mmu.getMemRegister(MemRegisterEnum.LYC);
        }
    
        void incrementLY() {
            int LY = getLY();
            mmu.setMemRegister(MemRegisterEnum.LY, ++LY % LINES_PER_FRAME);
            if (getLY() == getLYC()) {
                setSTATLYCFlag(true);
                checkSTATInterrupt(6);
            } else {
                setSTATLYCFlag(false);
            }
        }

        void resetLY() {
            mmu.setMemRegister(MemRegisterEnum.LY, 0);
        }

        int[] getBGP() {
            return getPalette(MemRegisterEnum.BGP);
        }

        int[] getOBP0() {
            return getPalette(MemRegisterEnum.OBP0);
        }

        int[] getOBP1() {
            return getPalette(MemRegisterEnum.OBP1);
        }

        private int[] getPalette(MemRegisterEnum paletteRegister) {
            int registerValue = mmu.getMemRegister(paletteRegister);
            int[] palette = new int[] {
                registerValue & 0x3,
                registerValue >> 2 & 0x3,
                registerValue >> 4 & 0x3,
                registerValue >> 6 & 0x3 
            };
            return palette;
        }

    }

    private final int VBLANK_INT_REQUEST_BIT = 0;
    //private final int LCDSTAT_INT_REQUEST_BIT = 1;
    //private boolean LCDSTATInterruptTriggeredInLastCheck;

    private final int SCANLINE_TICKS = 456;
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
    private MMU mmu;

    private GPURegisters gpuRegisters;
    private GPUMode gpuMode;
    private int ticksInLine;

    public GPU(Display _display, MMU _mmu) {
        display = _display;
        mmu = _mmu;
        VBLANK_START_LINE = display.getGameboyLCDHeight();
        LINES_PER_FRAME = VBLANK_START_LINE + 10;
        gpuRegisters = new GPURegisters(mmu);
        GPU_MODE = Mode.OAM_SEARCH;
        gpuMode = new OAMSearchMode(gpuRegisters, mmu);
    }

    public void tick() {
        if (!gpuRegisters.isLCDEnabled()) return;
        gpuMode.tick();
        ticksInLine++;
        if (gpuMode.isFinished()) {
            switch (GPU_MODE) {
                case OAM_SEARCH:              
                    GPU_MODE = Mode.PIXEL_TRANSFER;
                    LinkedList<Sprite> sprites = ((OAMSearchMode) gpuMode).getSprites();
                    gpuMode = new PixelTransferMode(gpuRegisters, sprites, display);
                    break;
                case PIXEL_TRANSFER:
                    checkSTATInterrupt(3);
                    GPU_MODE = Mode.HBLANK;
                    gpuMode = new HBlankMode(SCANLINE_TICKS - ticksInLine);
                    break;
                case HBLANK:
                    gpuRegisters.incrementLY();
                    if (gpuRegisters.getLY() < VBLANK_START_LINE) {
                        checkSTATInterrupt(5);
                        GPU_MODE = Mode.OAM_SEARCH;
                        gpuMode = new OAMSearchMode(gpuRegisters, mmu);                        
                    } else {
                        checkSTATInterrupt(4);
                        GPU_MODE = Mode.VBLANK;
                        gpuMode = new VBlankMode();
                        requestVBlankInterrupt();
                    }
                    ticksInLine = 0;
                    break;
                case VBLANK:
                    gpuRegisters.incrementLY();
                    if (gpuRegisters.getLY() == 0) {
                        checkSTATInterrupt(5);
                        GPU_MODE = Mode.OAM_SEARCH;                        
                        gpuMode = new OAMSearchMode(gpuRegisters, mmu);                        
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
        gpuMode = new OAMSearchMode(gpuRegisters, mmu);
    }

    private void requestVBlankInterrupt() {
        int IF = gpuRegisters.mmu.getMemRegister(MemRegisterEnum.IF);
        IF = ByteUtils.setBit(IF, VBLANK_INT_REQUEST_BIT);
        gpuRegisters.mmu.setMemRegister(MemRegisterEnum.IF, IF);
    }

    private void setSTATMode(int mode) {
        gpuRegisters.setSTATMode(mode);
    }

    private void checkSTATInterrupt(int bit) {
        /*int STAT = gpuRegisters.getSTAT();
        boolean interrupt = (STAT & (1 << bit)) != 0;
        if (interrupt & !LCDSTATInterruptTriggeredInLastCheck) {
            int IF = gpuRegisters.mmu.getMemRegister(MemRegisterEnum.IF);
            IF = ByteUtils.setBit(IF, LCDSTAT_INT_REQUEST_BIT);
            gpuRegisters.mmu.setMemRegister(MemRegisterEnum.IF, IF);
            LCDSTATInterruptTriggeredInLastCheck = true;
        } else {
            LCDSTATInterruptTriggeredInLastCheck = false;
        }*/
    }

}