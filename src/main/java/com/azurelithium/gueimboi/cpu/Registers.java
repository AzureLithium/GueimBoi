package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.ByteUtils;

class Registers {

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
        A = _A;
    }

    int getB() {
        return B;
    }

    void setB(int _B) {
        B = _B;
    }

    int getC() {
        return C;
    }

    void setC(int _C) {
        C = _C;
    }

    int getD() {
        return D;
    }

    void setD(int _D) {
        D = _D;
    }

    int getE() {
        return E;
    }

    void setE(int _E) {
        E = _E;
    }

    int getH() {
        return H;
    }

    void setH(int _H) {
        H = _H;
    }

    int getL() {
        return L;
    }

    void setL(int _L) {
        L = _L;
    }

    int getAF() {
        return ByteUtils.toWord(A, flags.getFlags());
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
        SP = _SP;
    }

    void incrementSP(int _SP) {
        SP += _SP;
    }

    int getPC() {
        return PC;
    }

    void setPC(int _PC) {
        PC = _PC;
    }

    void incrementPC(int _PC) {
        PC += _PC;
    }

    Flags getFlags() {
        return flags;
    }

}
