package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.ByteUtils;
import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Operand {

    final Logger logger = LoggerFactory.getLogger(getClass());

    String getName() {
        return getClass().getSimpleName();
    }

}


interface Decodable {
    void decode(ExecutionContext executionContext);
    void logDecode(String data, String address);
}


interface ReadableWritable {
    int read(ExecutionContext executionContext);
    void write(ExecutionContext executionContext);
}


abstract class RegisterOperand extends Operand implements ReadableWritable {

    abstract int getRegisterValue(ExecutionContext executionContext);

    abstract void setRegisterValue(ExecutionContext executionContext);

    public int read(ExecutionContext executionContext) {
        int registerValue = getRegisterValue(executionContext);
        logReadFromRegister(StringUtils.toHex(registerValue));
        return registerValue;
    }

    public void write(ExecutionContext executionContext) {
        setRegisterValue(executionContext);
        logWriteToRegister(executionContext.printData());
    }

    void logReadFromRegister(String data) {
        logger.trace("Registers: Reading data {} from register {}.", data,
                getClass().getSimpleName());
    }

    void logWriteToRegister(String data) {
        logger.trace("Registers: Writing data {} to register {}.", data,
                getClass().getSimpleName());
    }

}


abstract class AddressOperand extends Operand implements ReadableWritable {

    void setAddress(ExecutionContext executionContext) {};

    public int read(ExecutionContext executionContext) {
        setAddress(executionContext);
        int byteRead = executionContext.MMU.readByte(executionContext.dataAddress);
        logReadByteFromAddress(StringUtils.toHex(byteRead),
                executionContext.printDataAddress());
        return byteRead;
    }

    public void write(ExecutionContext executionContext) {
        setAddress(executionContext);
        executionContext.MMU.writeBytes(executionContext.dataAddress,
                executionContext.getDataBytes());
        logWriteBytesToAddress(executionContext.printData(),
                executionContext.printDataAddress());
    }

    void logReadByteFromAddress(String data, String address) {
        logger.trace("Memory: Read byte {} from address {}.", data, address);
    }

    void logWriteBytesToAddress(String data, String address) {
        logger.trace("Memory: Writing bytes {} to address {}.", data, address);
    }

}


final class A extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getA();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setA(executionContext.getData());
    }

}


final class B extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getB();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setB(executionContext.getData());
    }

}


final class C extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getC();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setC(executionContext.getData());
    }

}


final class CAddress extends AddressOperand {

    String getName() {
        return "(C)";
    }

    void setAddress(ExecutionContext executionContext) {
        executionContext.dataAddress = 0xFF00 | executionContext.registers.getC();
    }

}


final class D extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getD();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setD(executionContext.getData());
    }

}


final class E extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getE();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setE(executionContext.getData());
    }

}


final class H extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getH();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setH(executionContext.getData());
    }

}


final class L extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getL();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setL(executionContext.getData());
    }

}


final class AF extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getAF();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setAF(executionContext.getData());
    }

}


final class BC extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getBC();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setBC(executionContext.getData());
    }

}


final class BCAddress extends AddressOperand {

    String getName() {
        return "(BC)";
    }

    void setAddress(ExecutionContext executionContext) {
        executionContext.dataAddress = executionContext.registers.getBC();
    }

}


final class DE extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getDE();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setDE(executionContext.getData());
    }

}


final class DEAddress extends AddressOperand {

    String getName() {
        return "(DE)";
    }

    void setAddress(ExecutionContext executionContext) {
        executionContext.dataAddress = executionContext.registers.getDE();
    }

}


final class HL extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getHL();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setHL(executionContext.getData());
    }

}


final class HLAddress extends AddressOperand {

    String getName() {
        return "(HL)";
    }

    void setAddress(ExecutionContext executionContext) {
        executionContext.dataAddress = executionContext.registers.getHL();
    }

}


final class PC extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getPC();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setPC(executionContext.getData());
    }

}


final class SP extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getSP();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setSP(executionContext.getData());
    }

}


final class ByteImmediate extends Operand implements Decodable {

    String getName() {
        return "d8";
    }

    public void decode(ExecutionContext executionContext) {
        int address = executionContext.registers.getPC();
        int byteRead = executionContext.MMU.readByte(address);
        executionContext.setData(byteRead);
        executionContext.ALU.incrementPC(executionContext, Byte.BYTES);
        logDecode(executionContext.printData(), StringUtils.toHex(address));
    }

    public void logDecode(String data, String address) {
        logger.trace("Decoding: Fetched byte operand {} from address {}.", data, address);
    }

}


final class ByteAddress extends AddressOperand implements Decodable {

    String getName() {
        return "(a8)";
    }

    public void decode(ExecutionContext executionContext) {
        int address = executionContext.registers.getPC();
        int byteRead = executionContext.MMU.readByte(address);
        executionContext.dataAddress = 0xFF00 | byteRead;
        executionContext.ALU.incrementPC(executionContext, Byte.BYTES);
        logDecode(StringUtils.toHex(byteRead), StringUtils.toHex(address));
    }

    public void logDecode(String data, String address) {
        logger.trace("Decoding: Fetched byte address operand {} from address {}.", data, address);
    }

}


final class SignedByteImmediate extends Operand implements Decodable {

    String getName() {
        return "r8";
    }

    public void decode(ExecutionContext executionContext) {
        int address = executionContext.registers.getPC();
        int byteRead = (byte) executionContext.MMU.readByte(address);
        executionContext.setData(byteRead);
        executionContext.ALU.incrementPC(executionContext, Byte.BYTES);
        logDecode(executionContext.printData(), StringUtils.toHex(address));
    }

    public void logDecode(String data, String address) {
        logger.trace("Decoding: Fetched signed byte operand {} from address {}.", data, address);
    }

}


final class WordImmediate extends Operand implements Decodable {

    String getName() {
        return "d16";
    }

    public void decode(ExecutionContext executionContext) {
        int address = executionContext.registers.getPC();
        int[] bytesRead = executionContext.MMU.readBytes(address, Short.BYTES);
        executionContext.setData(ByteUtils.toWord(bytesRead[0], bytesRead[1]));
        executionContext.ALU.incrementPC(executionContext, Short.BYTES);
        logDecode(executionContext.printData(), StringUtils.toHex(address));
    }

    public void logDecode(String data, String address) {
        logger.trace("Decoding: Fetched word operand {} from address {}.", data, address);
    }

}


final class WordAddress extends AddressOperand implements Decodable {

    String getName() {
        return "(a16)";
    }

    public void decode(ExecutionContext executionContext) {
        int address = executionContext.registers.getPC();
        int[] bytesRead = executionContext.MMU.readBytes(address, Short.BYTES);
        executionContext.dataAddress = ByteUtils.toWord(bytesRead[0], bytesRead[1]);
        executionContext.ALU.incrementPC(executionContext, Short.BYTES);
        logDecode(executionContext.printDataAddress(), StringUtils.toHex(address));
    }

    public void logDecode(String data, String address) {
        logger.trace("Decoding: Fetched word address operand {} from address {}.", data, address);
    }

}
