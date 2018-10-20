package com.azurelithium.gueimboi.cpu;

import java.util.LinkedList;
import java.util.List;

public class Instruction {

    class InstructionContext {        
        int data;
        int cycles;
        Registers registers;
    }
    
    private int opcode;
    private String mnemonic;
    private InstructionContext instructionContext;
    private List<InstructionStep> instructionSteps;
    
    public Instruction(int _opcode, String _mnemonic, Registers _registers) {
        opcode = _opcode;
        mnemonic = _mnemonic;   
        instructionSteps = new LinkedList<InstructionStep>();
        instructionContext = new InstructionContext() {{ 
            registers = _registers; 
        }};
    }

    public void addStep(InstructionStep instructionStep) {
        instructionStep.setInstructionContext(instructionContext);
        instructionSteps.add(instructionStep);
    }

    public void execute() {
        System.out.printf("Executing 0x%h : %s%n", opcode, mnemonic);
        for (InstructionStep instructionStep : instructionSteps) {
            instructionStep.execute();
        }
    }

}