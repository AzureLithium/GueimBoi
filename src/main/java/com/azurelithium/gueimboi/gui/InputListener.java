package com.azurelithium.gueimboi.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import com.azurelithium.gueimboi.joypad.Input;
import com.azurelithium.gueimboi.joypad.InputController;

class InputListener implements KeyListener {

    private InputController joypad;
    private HashMap<Integer, Input> keyCodeInputMap;

    InputListener(InputController _joypad) {
        joypad = _joypad;
        keyCodeInputMap = new HashMap<Integer, Input>();
        keyCodeInputMap.put(KeyEvent.VK_UP, Input.UP);
        keyCodeInputMap.put(KeyEvent.VK_DOWN, Input.DOWN);
        keyCodeInputMap.put(KeyEvent.VK_LEFT, Input.LEFT);
        keyCodeInputMap.put(KeyEvent.VK_RIGHT, Input.RIGHT);
        keyCodeInputMap.put(KeyEvent.VK_X, Input.A);
        keyCodeInputMap.put(KeyEvent.VK_Z, Input.B);
        keyCodeInputMap.put(KeyEvent.VK_ENTER, Input.START);
        keyCodeInputMap.put(KeyEvent.VK_BACK_SPACE, Input.SELECT);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        Input i = getInput(e);
        if (i != null) {
            joypad.addPressedInput(i);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Input i = getInput(e);
        if (i != null) {
            joypad.removePressedInput(i);
        }
    }

    private Input getInput(KeyEvent e) {
        return keyCodeInputMap.getOrDefault(e.getKeyCode(), null);
    }

}