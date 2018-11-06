package com.azurelithium.gueimboi.cpu;

class InstructionBuilder {

    private Instruction instruction;

    /**
     * Create instruction
     */

    InstructionBuilder instruction(int opcode, String mnemonic) {
        instruction = new Instruction(opcode, mnemonic);
        return this;
    }

    /**
     * Decode operand command
     */

    InstructionBuilder decodeOperand(Decodable operand) {
        instruction.setInstructionOperand(operand);
        return this;
    }

    /**
     * Loading and Storing commands
     */

    InstructionBuilder load(ReadableWritable operand) {
        instruction.addStep(new Load(operand));
        return this;
    }

    InstructionBuilder store(ReadableWritable operand) {
        instruction.addStep(new Store(operand));
        return this;
    }

    InstructionBuilder push() {
        instruction.addStep(new Push());
        return this;
    }

    InstructionBuilder pop() {
        instruction.addStep(new Pop());
        return this;
    }

    /**
     * Arithmetic/Logical commands
     */

    InstructionBuilder byteALUIncrement() {
        instruction.addStep(new ByteALUIncrement());
        return this;
    }

    InstructionBuilder byteALUDecrement() {
        instruction.addStep(new ByteALUDecrement());
        return this;
    }

    InstructionBuilder wordALUIncrement() {
        instruction.addStep(new WordALUIncrement());
        return this;
    }

    InstructionBuilder wordALUDecrement() {
        instruction.addStep(new WordALUDecrement());
        return this;
    }

    InstructionBuilder postIncrementHL() {
        instruction.addStep(new PostIncrementHL());
        return this;
    }

    InstructionBuilder postDecrementHL() {
        instruction.addStep(new PostDecrementHL());
        return this;
    }

    InstructionBuilder ADD() {
        instruction.addStep(new ADD());
        return this;
    }

    InstructionBuilder SUB() {
        instruction.addStep(new SUB());
        return this;
    }

    InstructionBuilder XOR() {
        instruction.addStep(new XOR());
        return this;
    }

    InstructionBuilder CP() {
        instruction.addStep(new CP());
        return this;
    }

    InstructionBuilder RLA() {
        instruction.addStep(new RLA());
        return this;
    }

    InstructionBuilder RL() {
        instruction.addStep(new RL());
        return this;
    }

    /**
     * Bit commands
     */

    InstructionBuilder testBit(int bit) {
        instruction.addStep(new TestBit(bit));
        return this;
    }

    /**
     * Flag commands
     */

    InstructionBuilder ifZ() {
        instruction.addStep(new IfZ());
        return this;
    }

    InstructionBuilder ifNZ() {
        instruction.addStep(new IfNZ());
        return this;
    }


    /**
     * Jump/Call/Return commands
     */

    InstructionBuilder jumpRelative() {
        instruction.addStep(new JumpRelative());
        return this;
    }

    InstructionBuilder call() {
        instruction.addStep(new Call());
        return this;
    }

    InstructionBuilder ret() {
        instruction.addStep(new Ret());
        return this;
    }


    /**
     * Build instruction
     */

    Instruction build() {
        return instruction;
    }

}
