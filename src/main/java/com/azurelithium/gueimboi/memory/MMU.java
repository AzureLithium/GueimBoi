package com.azurelithium.gueimboi.memory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeMap;

import com.azurelithium.gueimboi.memory.MemRegisterEnum;
import com.azurelithium.gueimboi.joypad.InputController;
import com.azurelithium.gueimboi.timer.Timer;

public class MMU {

    private TreeMap<MemRegisterEnum, MemRegister> MemRegisterByEnum;
    private TreeMap<Integer, MemRegister> MemRegisterByAddress;

    private Memory memory;
    private InputController joypad;
    private Timer timer;
    private String ROMPath; 
    private int serialChars = 0;
    private final int serialContentCapacity = 512; 
    private StringBuilder serialContent = new StringBuilder(serialContentCapacity);

    public MMU(String _ROMPath) {
        memory = new Memory();
        MemRegisterByEnum = new TreeMap<MemRegisterEnum, MemRegister>();
        MemRegisterByAddress = new TreeMap<Integer, MemRegister>();
        ROMPath = _ROMPath;
        loadCartridge();
        memory.writeBytes(0x0000, ROM.GAMEBOY_ROM);
    }

    public StringBuilder getSerialContent() {
        return serialContent;
    }

    public void setInputController(InputController _joypad) {
        joypad = _joypad;
    }

    public void setTimer(Timer _timer) {
        timer = _timer;
    }

    public void initializeMemRegisters() {
        initializeJoypadMemRegisters();
        initializeTimerMemRegisters();
        initializeGPUMemRegisters();
        initializeInterruptsMemRegisters();
        initializeControlMemRegisters();
    }

    public void initializeJoypadMemRegisters() {
        JOYP JOYP = new JOYP(memory, 0xFF00, joypad);
        MemRegisterByEnum.put(MemRegisterEnum.JOYP, JOYP);
        MemRegisterByAddress.put(JOYP.getAddress(), JOYP);
    }

    public void initializeTimerMemRegisters() {
        DIVLSB DIVLSB = new DIVLSB(memory, 0xFF03, timer);
        DIV DIV = new DIV(memory, 0xFF04, timer);
        TIMA TIMA = new TIMA(memory, 0xFF05, timer);
        TMA TMA = new TMA(memory, 0xFF06, timer);
        TAC TAC = new TAC(memory, 0xFF07, timer);
        MemRegisterByEnum.put(MemRegisterEnum.DIVLSB, DIVLSB);
        MemRegisterByEnum.put(MemRegisterEnum.DIV, DIV);
        MemRegisterByEnum.put(MemRegisterEnum.TIMA, TIMA);
        MemRegisterByEnum.put(MemRegisterEnum.TMA, TMA);
        MemRegisterByEnum.put(MemRegisterEnum.TAC, TAC);
        MemRegisterByAddress.put(DIVLSB.getAddress(), DIVLSB);
        MemRegisterByAddress.put(DIV.getAddress(), DIV);
        MemRegisterByAddress.put(TIMA.getAddress(), TIMA);
        MemRegisterByAddress.put(TMA.getAddress(), TMA);
        MemRegisterByAddress.put(TAC.getAddress(), TAC);
    }

    public void initializeGPUMemRegisters() {
        MemRegister LCDC = new MemRegister(memory, 0xFF40);
        STAT STAT = new STAT(memory, 0xFF41);
        MemRegister SCY = new MemRegister(memory, 0xFF42);
        MemRegister SCX = new MemRegister(memory, 0xFF43);
        MemRegister LY = new MemRegister(memory, 0xFF44);
        MemRegister LYC = new MemRegister(memory, 0xFF45);
        MemRegister BGP = new MemRegister(memory, 0xFF47);
        MemRegisterByEnum.put(MemRegisterEnum.LCDC, LCDC);
        MemRegisterByEnum.put(MemRegisterEnum.STAT, STAT);
        MemRegisterByEnum.put(MemRegisterEnum.SCY, SCY);
        MemRegisterByEnum.put(MemRegisterEnum.SCX, SCX);
        MemRegisterByEnum.put(MemRegisterEnum.LY, LY);
        MemRegisterByEnum.put(MemRegisterEnum.LYC, LYC);
        MemRegisterByEnum.put(MemRegisterEnum.BGP, BGP);
        MemRegisterByAddress.put(LCDC.getAddress(), LCDC);
        MemRegisterByAddress.put(STAT.getAddress(), STAT);
        MemRegisterByAddress.put(SCY.getAddress(), SCY);
        MemRegisterByAddress.put(SCX.getAddress(), SCX);
        MemRegisterByAddress.put(LY.getAddress(), LY);
        MemRegisterByAddress.put(LYC.getAddress(), LYC);
        MemRegisterByAddress.put(BGP.getAddress(), BGP);
    }

    public void initializeInterruptsMemRegisters() {
        IF IF = new IF(memory, 0xFF0F, timer);
        IE IE = new IE(memory, 0xFFFF);
        MemRegisterByEnum.put(MemRegisterEnum.IF, IF);
        MemRegisterByEnum.put(MemRegisterEnum.IE, IE);
        MemRegisterByAddress.put(IF.getAddress(), IF);
        MemRegisterByAddress.put(IE.getAddress(), IE);
    }

    public void initializeControlMemRegisters() {
        ROM_DISABLE ROM_DISABLE = new ROM_DISABLE(memory, 0xFF50, this);
        MemRegisterByEnum.put(MemRegisterEnum.ROM_DISABLE, ROM_DISABLE);
        MemRegisterByAddress.put(ROM_DISABLE.getAddress(), ROM_DISABLE);
    }

    public int readByte(int address) {
        if (MemRegisterByAddress.containsKey(address)) {
            return MemRegisterByAddress.get(address).controlledRead();
        } else {
            return memory.readByte(address);
        }
    }

    public void writeByte(int address, int value) {
        if (address == 0xFF01) { //automated test purposes - Blargg's test write ASCII chars here
            serialContent.insert(serialChars++, (char)value);
        }
        if (MemRegisterByAddress.containsKey(address)) {
            MemRegisterByAddress.get(address).controlledWrite(value);
        } else {
            memory.writeByte(address, value & 0xFF);
        }
    }

    public int getMemRegister(MemRegisterEnum memRegisterEnum) {
        MemRegister memRegister = MemRegisterByEnum.get(memRegisterEnum);
        return memRegister.read();
    }

    public void setMemRegister(MemRegisterEnum memRegisterEnum, int value) {
        MemRegister memRegister = MemRegisterByEnum.get(memRegisterEnum);
        memRegister.write(value);
    }

    public void loadCartridge() {
        try {
            byte[] bFile = Files
                .readAllBytes(Paths.get(ROMPath));
            memory.writeBytes(0x0000, convertToIntArray(bFile));
        } catch (IOException e) {};
    }

    private int[] convertToIntArray(byte[] input) {
        int[] ret = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            ret[i] = input[i] & 0xFF; // Range 0 to 255, not -128 to 127
        }
        return ret;
    }

}
