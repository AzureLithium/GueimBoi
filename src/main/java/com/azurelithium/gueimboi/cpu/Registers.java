package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.ByteUtils;

class Registers {

    private final int BYTE_MASK = 0xFF;
    private final int WORD_MASK = 0xFFFF;

    private int A;
    private int B;
    private int C;
    private int D;
    private int E;
    private int H;
    private int L;
    private int SP;
    private int PC;
    private Flags flags;

    Registers() {
        flags = new Flags();
    }

    int getA() {
        return A;
    }

    void setA(int _A) {
        A = _A & BYTE_MASK;
    }

    int getB() {
        return B;
    }

    void setB(int _B) {
        B = _B & BYTE_MASK;
    }

    int getC() {
        return C;
    }

    void setC(int _C) {
        C = _C & BYTE_MASK;
    }

    int getD() {
        return D;
    }

    void setD(int _D) {
        D = _D & BYTE_MASK;
    }

    int getE() {
        return E;
    }

    void setE(int _E) {
        E = _E & BYTE_MASK;
    }

    int getH() {
        return H;
    }

    void setH(int _H) {
        H = _H & BYTE_MASK;
    }

    int getL() {
        return L;
    }

    void setL(int _L) {
        L = _L & BYTE_MASK;
    }

    int getAF() {
        return ByteUtils.toWord(A, flags.getFlags());
    }

    void setAF(int _AF) {
        A = ByteUtils.getMSB(_AF);
        flags = new Flags(ByteUtils.getLSB(_AF));
    }

    int getBC() {
        return ByteUtils.toWord(B, C);
    }

    void setBC(int _BC) {
        B = ByteUtils.getMSB(_BC);
        C = ByteUtils.getLSB(_BC);
    }

    int getDE() {
        return ByteUtils.toWord(D, E);
    }

    void setDE(int _DE) {
        D = ByteUtils.getMSB(_DE);
        E = ByteUtils.getLSB(_DE);
    }

    int getHL() {
        return ByteUtils.toWord(H, L);
    }

    void setHL(int _HL) {
        H = ByteUtils.getMSB(_HL);
        L = ByteUtils.getLSB(_HL);
    }

    int getSP() {
        return SP;
    }

    void setSP(int _SP) {
        SP = _SP & WORD_MASK;
    }

    int getPC() {
        return PC;
    }

    void setPC(int _PC) {
        PC = _PC & WORD_MASK;
    }

    Flags getFlags() {
        return flags;
    }

    void setFlags(int _flags) {
        flags = new Flags(_flags);
    }

}
