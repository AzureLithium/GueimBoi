package com.azurelithium.gueimboi.gpu;

class Pixel implements Comparable<Pixel> {

    enum PixelType {
        BACKGROUND,
        WINDOW,
        SPRITE
    }

    private static final int GAMEBOY_GRAYSHADE_MASK = 0x3; // 4 grayshades

    private int value;
    private PixelType pixelType;
    private Sprite sprite;

    Pixel(int _value, PixelType _pixelType) {
        value = _value & GAMEBOY_GRAYSHADE_MASK;
        pixelType = _pixelType;
    }

    int getValue() {
        return value;
    }

    PixelType getPixelType() {
        return pixelType;
    }

    boolean getSpritePriority() {
        return (sprite != null ? !sprite.getPriority() : null);
    }

    void setSprite(Sprite _sprite) {
        sprite = _sprite;
    }

    boolean getPaletteNumber() {
        return (sprite != null ? sprite.getPaletteNumber() : null);
    }

    @Override
    public int compareTo(Pixel p) {
        if (pixelType == PixelType.BACKGROUND) {
            return ((p.getValue() != 0) && (value == 0 || p.getSpritePriority()) ? -1 : 1);
        } else if (pixelType == PixelType.SPRITE) {
            return ((value == 0 && p.getValue() != 0) ? -1 : 1);
        }

        return 0;
    }

}