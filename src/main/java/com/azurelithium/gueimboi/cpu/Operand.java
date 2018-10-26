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


interface Readable {
    int read(ExecutionContext executionContext);
}


interface Writable {
    void write(ExecutionContext executionContext);
}


interface AddressReadable {
    void readFromAddress(ExecutionContext executionContext);
}


interface AddressWritable {
    void writeToAddress(ExecutionContext executionContext);
}


abstract class RegisterOperand extends Operand implements Readable, Writable {

    abstract int getRegisterValue(ExecutionContext executionContext);

    abstract void setRegisterValue(ExecutionContext executionContext);

    public int read(ExecutionContext executionContext) {
        int registerValue = getRegisterValue(executionContext);
        logReadFromRegister(StringUtils.toHex(registerValue));
        return registerValue;
    }

    public void write(ExecutionContext executionContext) {
        setRegisterValue(executionContext);
        logWriteToRegister(executionContext.printPrimaryData());
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


abstract class AddressOperand extends Operand implements Readable, Writable {

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
                executionContext.getPrimaryDataBytes());
        logWriteBytesToAddress(executionContext.printPrimaryData(),
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
        executionContext.registers.setA(executionContext.getPrimaryData());
    }

}


final class B extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getB();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setB(executionContext.getPrimaryData());
    }

}


final class C extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getC();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setC(executionContext.getPrimaryData());
    }

}


final class CAddress extends AddressOperand {

    String getName() {
        return "(C)";
    }

    void setAddress(ExecutionContext executionContext) {
        executionContext.dataAddress = 0xFF + executionContext.registers.getC();
    }

}


final class D extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getD();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setD(executionContext.getPrimaryData());
    }

}


final class E extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getE();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setE(executionContext.getPrimaryData());
    }

}


final class H extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getH();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setH(executionContext.getPrimaryData());
    }

}


final class L extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getL();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setL(executionContext.getPrimaryData());
    }

}


final class AF extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getAF();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setAF(executionContext.getPrimaryData());
    }

}


final class BC extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getBC();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setBC(executionContext.getPrimaryData());
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
        executionContext.registers.setDE(executionContext.getPrimaryData());
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
        executionContext.registers.setHL(executionContext.getPrimaryData());
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
        executionContext.registers.setPC(executionContext.getPrimaryData());
    }

}


final class SP extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getSP();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setSP(executionContext.getPrimaryData());
    }

}


final class ByteImmediate extends Operand implements Decodable {

    String getName() {
        return "d8";
    }

    public void decode(ExecutionContext executionContext) {
        int address = executionContext.registers.getPC();
        int byteRead = executionContext.MMU.readByte(address);
        executionContext.setPrimaryData(byteRead);
        executionContext.registers.incrementPC(Byte.BYTES);
        logDecode(executionContext.printPrimaryData(), StringUtils.toHex(address));
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
        executionContext.dataAddress = 0xFF + executionContext.MMU.readByte(address);
        executionContext.registers.incrementPC(Byte.BYTES);
        logDecode(executionContext.printDataAddress(), StringUtils.toHex(address));
    }

    public void logDecode(String data, String address) {
        logger.trace("Decoding: Fetched word address operand {} from address {}.", data, address);
    }

}


final class SignedByteImmediate extends Operand implements Decodable {

    String getName() {
        return "r8";
    }

    public void decode(ExecutionContext executionContext) {
        int address = executionContext.registers.getPC();
        int byteRead = (byte) executionContext.MMU.readByte(address);
        executionContext.setPrimaryData(byteRead);
        executionContext.registers.incrementPC(Byte.BYTES);
        logDecode(executionContext.printPrimaryData(), StringUtils.toHex(address));
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
        executionContext.setPrimaryData(ByteUtils.toWord(bytesRead[0], bytesRead[1]));
        executionContext.registers.incrementPC(Short.BYTES);
        logDecode(executionContext.printPrimaryData(), StringUtils.toHex(address));
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
        executionContext.registers.incrementPC(Short.BYTES);
        logDecode(executionContext.printDataAddress(), StringUtils.toHex(address));
    }

    public void logDecode(String data, String address) {
        logger.trace("Decoding: Fetched word address operand {} from address {}.", data, address);
    }

}
