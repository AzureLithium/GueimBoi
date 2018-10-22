package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.cpu.Instruction.InstructionContext;
import com.azurelithium.gueimboi.utils.ByteUtils;
import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Operand {

    A {

        void read(InstructionContext instructionContext) {
            instructionContext.data = new int[] {instructionContext.registers.getA()};
            logger.trace("Step: Reading data {} from register {}.", instructionContext.printData(),
                    this.name());
        }

        void write(InstructionContext instructionContext) {
            instructionContext.registers.setA(ByteUtils.toByte(instructionContext.data[0]));
            logger.trace("Step: Writing data {} to register {}.", instructionContext.printData(),
                    this.name());
        }

    },

    SP {

        void read(InstructionContext instructionContext) {
            int SP = instructionContext.registers.getSP();
            instructionContext.data = new int[] {ByteUtils.getMSB(SP), ByteUtils.getLSB(SP)};
            logger.trace("Step: Reading data {} from register {}.", instructionContext.printData(),
                    this.name());
        }

        void write(InstructionContext instructionContext) {
            instructionContext.registers.setSP(
                    ByteUtils.toWord(instructionContext.data[0], instructionContext.data[1]));
            logger.trace("Step: Writing data {} to register {}.", instructionContext.printData(),
                    this.name());
        }

    },

    BYTE {

        void decode(InstructionContext instructionContext) {
            int address = instructionContext.registers.getPC();
            instructionContext.data = new int[] {instructionContext.mmu.readByte(address)};
            instructionContext.registers.incrementPC(Byte.BYTES);
            logger.trace("Decoding: Fetched byte immediate operand {} from address {}.",
                    instructionContext.printData(), StringUtils.toHex(address));
        }

    },

    WORD {

        void decode(InstructionContext instructionContext) {
            int address = instructionContext.registers.getPC();
            instructionContext.data = instructionContext.mmu.readBytes(address, Short.BYTES);
            instructionContext.registers.incrementPC(Short.BYTES);
            logger.trace("Decoding: Fetched word immediate operand {} from address {}.",
                    instructionContext.printData(), StringUtils.toHex(address));
        }

    },

    BYTE_ADDRESS {

        void decode(InstructionContext instructionContext) {
            int address = instructionContext.registers.getPC();
            instructionContext.dataAddress = instructionContext.mmu.readByte(address);
            instructionContext.registers.incrementPC(Byte.BYTES);
            logger.trace("Decoding: Fetched byte address operand {} from address {}.",
                    instructionContext.printDataAddress(), StringUtils.toHex(address));
        }

        void read(InstructionContext instructionContext) {
            instructionContext.data =
                    new int[] {instructionContext.mmu.readByte(instructionContext.dataAddress)};
            logger.trace("Reading: Read byte operand {} from address {}.",
                    instructionContext.printData(), instructionContext.printDataAddress());
        }

        void write(InstructionContext instructionContext) {
            instructionContext.mmu.writeByte(instructionContext.dataAddress,
                    instructionContext.data[0]);
            logger.trace("Step: Writing byte data {} to address {}.",
                    instructionContext.printData(), instructionContext.printDataAddress());
        }

    },

    WORD_ADDRESS {

        void decode(InstructionContext instructionContext) {
            int address = instructionContext.registers.getPC();
            int[] bytes = instructionContext.mmu.readBytes(address, Short.BYTES);
            instructionContext.dataAddress = ByteUtils.toWord(bytes[0], bytes[1]);
            instructionContext.registers.incrementPC(Short.BYTES);
            logger.trace("Decoding: Fetched word address operand {} from address {}.",
                    instructionContext.printDataAddress(), StringUtils.toHex(address));
        }

        void read(InstructionContext instructionContext) {
            instructionContext.data =
                    instructionContext.mmu.readBytes(instructionContext.dataAddress, Short.BYTES);
            logger.trace("Reading: Read word operand {} from address {}.",
                    instructionContext.printData(), instructionContext.printDataAddress());
        }

        void write(InstructionContext instructionContext) {
            instructionContext.mmu.writeBytes(instructionContext.dataAddress,
                    instructionContext.data);
            logger.trace("Step: Writing word data {} to address {}.",
                    instructionContext.printData(), instructionContext.printDataAddress());
        }

    };

    final Logger logger = LoggerFactory.getLogger(Operand.class);

    void decode(InstructionContext instructionContext) {};

    void read(InstructionContext instructionContext) {};

    void write(InstructionContext instructionContext) {};

}
