package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.ByteUtils;
import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

enum Operand {

    A {

        void read(ExecutionContext executionContext) {
            executionContext.data = executionContext.registers.getA();
            logReadFromRegister(executionContext.printData());
        }

        void write(ExecutionContext executionContext) {
            executionContext.registers.setA(executionContext.data);
            logWriteToRegister(executionContext.printData());
        }

    },

    SP {

        void read(ExecutionContext executionContext) {
            executionContext.data = executionContext.registers.getSP();
            logReadFromRegister(executionContext.printData());
        }

        void write(ExecutionContext executionContext) {
            executionContext.registers.setSP(executionContext.data);
            logWriteToRegister(executionContext.printData());
        }

    },

    BYTE {

        void decode(ExecutionContext executionContext) {
            int address = executionContext.registers.getPC();
            executionContext.data = executionContext.MMU.readByte(address);
            executionContext.registers.incrementPC(Byte.BYTES);
            logger.trace("Decoding: Fetched byte immediate operand {} from address {}.",
                    executionContext.printData(), StringUtils.toHex(address));
        }

    },

    WORD {

        void decode(ExecutionContext executionContext) {
            int address = executionContext.registers.getPC();
            int[] bytes = executionContext.MMU.readBytes(address, Short.BYTES);
            executionContext.data = ByteUtils.toWord(bytes[0], bytes[1]);
            executionContext.registers.incrementPC(Short.BYTES);
            logger.trace("Decoding: Fetched word immediate operand {} from address {}.",
                    executionContext.printData(), StringUtils.toHex(address));
        }

    },

    BYTE_ADDRESS {

        void decode(ExecutionContext executionContext) {
            int address = executionContext.registers.getPC();
            executionContext.dataAddress = executionContext.MMU.readByte(address);
            executionContext.registers.incrementPC(Byte.BYTES);
            logger.trace("Decoding: Fetched byte address operand {} from address {}.",
                    executionContext.printDataAddress(), StringUtils.toHex(address));
        }

        void read(ExecutionContext executionContext) {
            executionContext.data = executionContext.MMU.readByte(executionContext.dataAddress);
            logger.trace("Reading: Read byte operand {} from address {}.",
                    executionContext.printData(), executionContext.printDataAddress());
        }

        void write(ExecutionContext executionContext) {
            executionContext.MMU.writeByte(executionContext.dataAddress, executionContext.data);
            logger.trace("Step: Writing byte data {} to address {}.", executionContext.printData(),
                    executionContext.printDataAddress());
        }

    },

    WORD_ADDRESS {

        void decode(ExecutionContext executionContext) {
            int address = executionContext.registers.getPC();
            int[] bytes = executionContext.MMU.readBytes(address, Short.BYTES);
            executionContext.dataAddress = ByteUtils.toWord(bytes[0], bytes[1]);
            executionContext.registers.incrementPC(Short.BYTES);
            logger.trace("Decoding: Fetched word address operand {} from address {}.",
                    executionContext.printDataAddress(), StringUtils.toHex(address));
        }

        void read(ExecutionContext executionContext) {
            int[] bytes = executionContext.MMU.readBytes(executionContext.dataAddress, Short.BYTES);
            executionContext.data = ByteUtils.toWord(bytes[0], bytes[1]);
            logger.trace("Reading: Read word operand {} from address {}.",
                    executionContext.printData(), executionContext.printDataAddress());
        }

        void write(ExecutionContext executionContext) {
            int[] bytes = new int[] {ByteUtils.getMSB(executionContext.data),
                    ByteUtils.getLSB(executionContext.data)};
            executionContext.MMU.writeBytes(executionContext.dataAddress, bytes);
            logger.trace("Step: Writing word data {} to address {}.", executionContext.printData(),
                    executionContext.printDataAddress());
        }

    };

    final Logger logger = LoggerFactory.getLogger(Operand.class);

    void decode(ExecutionContext executionContext) {};

    void read(ExecutionContext executionContext) {};

    void write(ExecutionContext executionContext) {};

    void logReadFromRegister(String data) {
        logger.trace("Load: Reading data {} from register {}.", data, this.name());
    }

    void logWriteToRegister(String data) {
        logger.trace("Store: Writing data {} to register {}.", data, this.name());
    }

}
