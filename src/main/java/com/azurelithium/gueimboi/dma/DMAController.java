package com.azurelithium.gueimboi.dma;

import com.azurelithium.gueimboi.common.Component;
import com.azurelithium.gueimboi.memory.MMU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DMAController extends Component {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private enum DMAControllerState {
        IDLE,
        STARTUP, 
        TRANSFERRING, 
        TEARDOWN
    }

    private DMAControllerState DMACONTROLLER_STATE = DMAControllerState.IDLE;

    private final int TRANSFER_DESTINATION_START_ADDRESS = 0xFE00;
    private final int BYTES_TO_TRANSFER = 160;

    private MMU MMU;

    private int transferSourceStartAddress;
    private int bytesTransferred;

    public DMAController(MMU _MMU) {
        MMU = _MMU;
    }

    public void tick() {
        switch (DMACONTROLLER_STATE) {
            case IDLE:
                return;
            case STARTUP:                
                DMACONTROLLER_STATE = DMAControllerState.TRANSFERRING;
                return;
            case TRANSFERRING:
                transferByte();
                return;
            case TEARDOWN:
                DMACONTROLLER_STATE = DMAControllerState.IDLE;
                return;
        }
    }

    public void startTransfer(int _transferSourceStartAddress) {
        transferSourceStartAddress = _transferSourceStartAddress;
        bytesTransferred = 0;
        DMACONTROLLER_STATE = DMAControllerState.STARTUP;
    }

    private void transferByte() {
        int byteToTransfer = MMU.readByte(transferSourceStartAddress + bytesTransferred);
        MMU.writeByte(TRANSFER_DESTINATION_START_ADDRESS + bytesTransferred, byteToTransfer);
        bytesTransferred++;
        if (bytesTransferred == BYTES_TO_TRANSFER) {
            DMACONTROLLER_STATE = DMAControllerState.TEARDOWN;
        }
    }

}