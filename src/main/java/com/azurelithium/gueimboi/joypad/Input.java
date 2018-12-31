package com.azurelithium.gueimboi.joypad;

public enum Input {

    UP(Constants.UP_SELECT_PRESSED_BIT, Constants.DIRECTION_SELECT_BIT),
    DOWN(Constants.DOWN_START_PRESSED_BIT, Constants.DIRECTION_SELECT_BIT),
    LEFT(Constants.LEFT_B_PRESSED_BIT, Constants.DIRECTION_SELECT_BIT),
    RIGHT(Constants.RIGHT_A_PRESSED_BIT, Constants.DIRECTION_SELECT_BIT),
    A(Constants.RIGHT_A_PRESSED_BIT, Constants.BUTTON_SELECT_BIT),
    B(Constants.LEFT_B_PRESSED_BIT, Constants.BUTTON_SELECT_BIT),
    START(Constants.DOWN_START_PRESSED_BIT, Constants.BUTTON_SELECT_BIT),
    SELECT(Constants.UP_SELECT_PRESSED_BIT, Constants.BUTTON_SELECT_BIT);
    
    private static class Constants {
        private static final int RIGHT_A_PRESSED_BIT = 0;
        private static final int LEFT_B_PRESSED_BIT = 1;
        private static final int UP_SELECT_PRESSED_BIT = 2;
        private static final int DOWN_START_PRESSED_BIT = 3;
        private static final int DIRECTION_SELECT_BIT = 4;
        private static final int BUTTON_SELECT_BIT = 5;
    }

    private final int pressedBit;

    private final int selectBit;

    Input(int _pressedBit, int _selectBit) {
        pressedBit = _pressedBit;
        selectBit = _selectBit;
    }

    public int getPressedBit() {
        return pressedBit;
    }

    public int getSelectBit() {
        return selectBit;
    }

}