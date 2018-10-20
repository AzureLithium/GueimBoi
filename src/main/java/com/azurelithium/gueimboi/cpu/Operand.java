package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.cpu.Instruction.InstructionContext;
import com.azurelithium.gueimboi.utils.ByteUtils;

public enum Operand {

    A {

        void read(InstructionContext instructionContext) {            
            instructionContext.data = instructionContext.registers.getA();
            System.out.printf("Step: Reading data 0x%h from register %s%n", instructionContext.data, this.name());
        }

        void write(InstructionContext instructionContext) {
            instructionContext.registers.setA(ByteUtils.toByte(instructionContext.data));
            System.out.printf("Step: Writing data 0x%h to register %s%n", instructionContext.data, this.name());
        }
        
    };

    abstract void read(InstructionContext instructionContext);
    abstract void write(InstructionContext instructionContext);

}