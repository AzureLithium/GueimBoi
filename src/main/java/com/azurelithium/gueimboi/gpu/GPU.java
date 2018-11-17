package com.azurelithium.gueimboi.gpu;

import com.azurelithium.gueimboi.common.Component;
import com.azurelithium.gueimboi.gui.Display;
import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.memory.MMU.MemRegister;

public class GPU extends Component {

    class GPURegisters {

        MMU mmu;
        
        GPURegisters(MMU _mmu) {
            mmu = _mmu;
        }

        int getLCDC() {
            return mmu.getMemRegister(MemRegister.LCDC);
        }

        int getTileDataAddress() {
            int LCDC = getLCDC();
            return ((LCDC & 1 << 4) == 0 ? 0x8800 : 0x8000);
        }

        int getBGTileMapAddress() {
            int LCDC = getLCDC();
            return ((LCDC & 1 << 3) == 0 ? 0x9800 : 0x9C00);
        }

        int getSCY() {
            return mmu.getMemRegister(MemRegister.SCY);
        }

        int getSCX() {
            return mmu.getMemRegister(MemRegister.SCX);
        }

        int getLY() {
            return mmu.getMemRegister(MemRegister.LY);
        }
    
        void incrementLY() {
            int LY = getLY();
            mmu.setMemRegister(MemRegister.LY, ++LY % LINES_PER_FRAME);
        }

        int[] getBGP() {
            int BGP = mmu.getMemRegister(MemRegister.BGP);
            int[] grayShades = new int[] {
                BGP & 0x3,
                BGP >> 2 & 0x3,
                BGP >> 4 & 0x3,
                BGP >> 6 & 0x3 
            };
            return grayShades;
        }

    }

    private final int HBLANK_EARLIEST_START_TICK = 252;
    private final int VBLANK_START_LINE = 144;
    private final int LINES_PER_FRAME = 154;

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
        gpuRegisters = new GPURegisters(_mmu);
        GPU_MODE = Mode.OAM_SEARCH;
        gpuMode = new OAMSearchMode();
    }

    public void tick() {
        gpuMode.tick();
        ticksInLine++;
        if (gpuMode.isFinished()) {
            switch (GPU_MODE) {
                case OAM_SEARCH:
                    GPU_MODE = Mode.PIXEL_TRANSFER;
                    gpuMode = new PixelTransferMode(display, gpuRegisters);
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
                        ticksInLine = 0;
                    } else {
                        GPU_MODE = Mode.VBLANK;
                        gpuMode = new VBlankMode();
                        ticksInLine = 0;
                    }
                    break;
                case VBLANK:
                    gpuRegisters.incrementLY();
                    if (gpuRegisters.getLY() == 0) {
                        GPU_MODE = Mode.OAM_SEARCH;
                        gpuMode = new OAMSearchMode();
                        ticksInLine = 0;
                    } else {
                        gpuMode = new VBlankMode();
                        ticksInLine = 0;
                    }
                    break;
            }
        }
    }

}