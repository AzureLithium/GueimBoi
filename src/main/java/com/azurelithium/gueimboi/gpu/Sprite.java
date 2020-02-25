package com.azurelithium.gueimboi.gpu;

import com.azurelithium.gueimboi.utils.ByteUtils;

class Sprite {

    private final int BYTE_MASK = 0xFF;

    private final int OBJ_PRIORITY_BIT = 7;
    private final int Y_FLIP_BIT = 6;
    private final int X_FLIP_BIT = 5;
    private final int PALETTE_BIT = 4;

    private int Y;
    private int X;
    private int tileNumber;
    private int attributes;
    private int address;

    Sprite(int _Y, int _X, int _address) {
        Y = _Y & BYTE_MASK;
        X = _X & BYTE_MASK;
        address = _address;
    }

    int getY() {
        return Y;
    }

    int getX() {
        return X;
    }

    int getTileNumber() {
        return tileNumber;
    }

    void setTileNumber(int _tileNumber) {
        tileNumber = _tileNumber & BYTE_MASK;
    }

    boolean getPriority() {
        return ByteUtils.getBit(attributes, OBJ_PRIORITY_BIT);
    }

    boolean getYFlip() {
        return ByteUtils.getBit(attributes, Y_FLIP_BIT);
    }

    boolean getXFlip() {
        return ByteUtils.getBit(attributes, X_FLIP_BIT);
    }

    boolean getPaletteNumber() {
        return ByteUtils.getBit(attributes, PALETTE_BIT);
    }

    void setAttributes(int _attributes) {
        attributes = _attributes & BYTE_MASK;
    }

    int getAddress() {
        return address;
    }

}