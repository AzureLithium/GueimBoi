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

    InstructionBuilder decode(DecodableByte operand) {
        instruction.addStep(new DecodeLSB(operand));
        return this;
    }

    InstructionBuilder decode(DecodableWord operand) {
        instruction.addStep(new DecodeLSB(operand));
        instruction.addStep(new DecodeMSB(operand));
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

    InstructionBuilder storeWordInAddress(AddressOperand operand) {
        instruction.addStep(new StoreLSB(operand));
        instruction.addStep(new StoreMSB(operand));
        return this;
    }

    InstructionBuilder push(PushablePopable operand) {
        instruction.addStep(new PushMSB(operand));
        instruction.addStep(new PushLSB(operand));
        return this;
    }

    InstructionBuilder pop(PushablePopable operand) {
        instruction.addStep(new PopLSB(operand));
        instruction.addStep(new PopMSB(operand));
        return this;
    }


    /**
     * Arithmetic/Logical commands
     */

    InstructionBuilder INC() {
        instruction.addStep(new INC());
        return this;
    }

    InstructionBuilder DEC() {
        instruction.addStep(new DEC());
        return this;
    }

    InstructionBuilder wordINC() {
        instruction.addStep(new WordINC());
        return this;
    }

    InstructionBuilder wordDEC() {
        instruction.addStep(new WordDEC());
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

    InstructionBuilder ADC() {
        instruction.addStep(new ADC());
        return this;
    }

    InstructionBuilder SUB() {
        instruction.addStep(new SUB());
        return this;
    }

    InstructionBuilder SBC() {
        instruction.addStep(new SBC());
        return this;
    }

    InstructionBuilder AND() {
        instruction.addStep(new AND());
        return this;
    }

    InstructionBuilder XOR() {
        instruction.addStep(new XOR());
        return this;
    }

    InstructionBuilder OR() {
        instruction.addStep(new OR());
        return this;
    }

    InstructionBuilder CP() {
        instruction.addStep(new CP());
        return this;
    }

    InstructionBuilder wordADD() {
        instruction.addStep(new WordADD());
        return this;
    }


    InstructionBuilder DAA() {
        instruction.addStep(new DAA());
        return this;
    }


    InstructionBuilder CPL() {
        instruction.addStep(new CPL());
        return this;
    }


    InstructionBuilder SCF() {
        instruction.addStep(new SCF());
        return this;
    }


    InstructionBuilder CCF() {
        instruction.addStep(new CCF());
        return this;
    }

    InstructionBuilder RLA() {
        instruction.addStep(new RLA());
        return this;
    }

    InstructionBuilder RLCA() {
        instruction.addStep(new RLCA());
        return this;
    }

    InstructionBuilder RRA() {
        instruction.addStep(new RRA());
        return this;
    }

    InstructionBuilder RRCA() {
        instruction.addStep(new RRCA());
        return this;
    }

    InstructionBuilder RL() {
        instruction.addStep(new RL());
        return this;
    }

    InstructionBuilder RLC() {
        instruction.addStep(new RLC());
        return this;
    }

    InstructionBuilder RR() {
        instruction.addStep(new RR());
        return this;
    }

    InstructionBuilder RRC() {
        instruction.addStep(new RRC());
        return this;
    }

    InstructionBuilder SLA() {
        instruction.addStep(new SLA());
        return this;
    }

    InstructionBuilder SRA() {
        instruction.addStep(new SRA());
        return this;
    }

    InstructionBuilder SWAP() {
        instruction.addStep(new SWAP());
        return this;
    }

    InstructionBuilder SRL() {
        instruction.addStep(new SRL());
        return this;
    }

    InstructionBuilder addToSP() {
        instruction.addStep(new AddToSP());
        return this;
    }


    /**
     * Bit commands
     */

    InstructionBuilder BIT(int bit) {
        instruction.addStep(new BIT(bit));
        return this;
    }

    InstructionBuilder RES(int bit) {
        instruction.addStep(new RES(bit));
        return this;
    }

    InstructionBuilder SET(int bit) {
        instruction.addStep(new SET(bit));
        return this;
    }


    /**
     * Flag commands
     */

    InstructionBuilder ifNZ() {
        instruction.addStep(new IfNZ());
        return this;
    }

    InstructionBuilder ifZ() {
        instruction.addStep(new IfZ());
        return this;
    }

    InstructionBuilder ifNC() {
        instruction.addStep(new IfNC());
        return this;
    }

    InstructionBuilder ifC() {
        instruction.addStep(new IfC());
        return this;
    }


    /**
     * Jump/Call/Return commands
     */

    InstructionBuilder jump() {
        instruction.addStep(new Jump());
        return this;
    }

    InstructionBuilder jumpRelative() {
        instruction.addStep(new JumpRelative());
        return this;
    }

    InstructionBuilder rst(int destination) {
        instruction.addStep(new RST(destination));
        return this;
    }


    /**
     * Misc commands
     */

    InstructionBuilder internalDelay() {
        instruction.addStep(new InternalDelay());
        return this;
    }

    InstructionBuilder useDataAsAddress() {
        instruction.addStep(new UseDataAsAddress());
        return this;
    }

    InstructionBuilder scheduleIME() {
        instruction.addStep(new ScheduleIME());
        return this;
    }

    InstructionBuilder setIME() {
        instruction.addStep(new SetIME());
        return this;
    }

    InstructionBuilder resetIME() {
        instruction.addStep(new ResetIME());
        return this;
    }

    InstructionBuilder HALT() {
        instruction.addStep(new HALT());
        return this;
    }


    /**
     * Build instruction
     */

    Instruction build() {
        return instruction;
    }

}
