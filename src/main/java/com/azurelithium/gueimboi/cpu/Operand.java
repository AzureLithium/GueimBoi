package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.cpu.Instruction.InstructionContext;
import com.azurelithium.gueimboi.utils.ByteUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Operand {

    A {

        void read(InstructionContext instructionContext) {            
            instructionContext.data = instructionContext.registers.getA();
            logger.trace("Step: Reading data 0x{} from register {}.", String.format("%h", instructionContext.data), this.name());
        }

        void write(InstructionContext instructionContext) {
            instructionContext.registers.setA(ByteUtils.toByte(instructionContext.data));
            logger.trace("Step: Writing data 0x{} to register {}.", String.format("%h", instructionContext.data), this.name());
        }
        
    };

    final Logger logger = LoggerFactory.getLogger(Operand.class);

    abstract void read(InstructionContext instructionContext);
    abstract void write(InstructionContext instructionContext);

}