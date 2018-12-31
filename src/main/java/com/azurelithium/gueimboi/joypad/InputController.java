package com.azurelithium.gueimboi.joypad;

import java.util.List;
import java.util.LinkedList;

import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.memory.MemRegisterEnum;
import com.azurelithium.gueimboi.utils.ByteUtils;

public class InputController {

    private static final int JOYPAD_INT_REQUEST_BIT = 4;

    private List<Input> pressedInputs;

    private MMU MMU;

    public InputController() {
        pressedInputs = new LinkedList<Input>();
    }

    public List<Input> getPressedInputs() {
        return pressedInputs.subList(0, pressedInputs.size());
    }

    public void addPressedInput(Input input) {
        if (!pressedInputs.contains(input) && 
            !(
                input == Input.UP && pressedInputs.contains(Input.DOWN) ||
                input == Input.DOWN && pressedInputs.contains(Input.UP) ||
                input == Input.LEFT && pressedInputs.contains(Input.RIGHT) ||
                input == Input.RIGHT && pressedInputs.contains(Input.LEFT)
            )) {
            pressedInputs.add(input);
            requestJoypadInterrupt();
        }        
    }

    public void removePressedInput(Input input) {
        if (pressedInputs.contains(input)) {
            pressedInputs.remove(input);
        }
    }

    public void setMMU(MMU _MMU) {
        MMU = _MMU;
    }

    private void requestJoypadInterrupt() {
        int IF = MMU.getMemRegister(MemRegisterEnum.IF);
        IF = ByteUtils.setBit(IF, JOYPAD_INT_REQUEST_BIT);
        MMU.setMemRegister(MemRegisterEnum.IF, IF);
    }

}