package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.ByteUtils;

public class Registers {

    private byte a;
    private byte b;
    private byte c;
    private byte d;
    private byte e;
    private byte h;
    private byte l;
    private short sp;
    private short pc;
    private Flags flags;

    Registers() {
        flags = new Flags();
    }

    byte getA() {
        return a;
    }

    void setA(byte _a) {
        a = _a;
    }

    byte getB() {
        return b;
    }

    void setB(byte _b) {
        b = _b;
    }

    byte getC() {
        return c;
    }

    void setC(byte _c) {
        c = _c;
    }

    byte getD() {
        return d;
    }

    void setD(byte _d) {
        d = _d;
    }

    byte getE() {
        return e;
    }

    void setE(byte _e) {
        e = _e;
    }

    byte getH() {
        return h;
    }

    void setH(byte _h) {
        h = _h;
    }

    byte getL() {
        return l;
    }

    void setL(byte _l) {
        l = _l;
    }

    short getAF() {
        return ByteUtils.toWord(a, flags.getFlags());
    }

    short getBC() {
        return ByteUtils.toWord(b, c);
    }

    void setBC(short _bc) {
        b = ByteUtils.getMSB(_bc);
        c = ByteUtils.getLSB(_bc);
    }

    short getDE() {
        return ByteUtils.toWord(d, e);
    }

    void setDE(short _de) {
        d = ByteUtils.getMSB(_de);
        e = ByteUtils.getLSB(_de);
    }

    short getHL() {
        return ByteUtils.toWord(h, l);
    }

    void setHL(short _hl) {
        h = ByteUtils.getMSB(_hl);
        l = ByteUtils.getLSB(_hl);
    }

    short getSP() {
        return sp;
    }

    void setSP(short _sp) {
        sp = _sp;
    }

    short getPC() {
        return pc;
    }

    void setPC(short _pc) {
        pc = _pc;
    }

}