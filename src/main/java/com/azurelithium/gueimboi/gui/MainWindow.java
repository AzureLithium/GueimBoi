package com.azurelithium.gueimboi.gui;

import javax.swing.JFrame;

import com.azurelithium.gueimboi.common.GameBoy;
import com.azurelithium.gueimboi.joypad.InputController;

public class MainWindow extends JFrame {

    private static final long serialVersionUID = -7360770248156614083L;
    
    private Display display;
    private InputController joypad;
    private InputListener inputListener;
    private GameBoy gameBoy;

    public MainWindow() {
        display = new Display(this);
        add(display);
        pack();
        //setSize(getWidth(), getHeight());
        setSize(getWidth() * 2, getHeight() * 2);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        joypad = new InputController();
        inputListener = new InputListener(joypad);
        addKeyListener(inputListener);
    }

    public void loadROM(String ROMPath) {
        gameBoy = new GameBoy(display, joypad, ROMPath);
    }

    public void initializeGameBoy() {
        gameBoy.initialize();
    }

    public void startGameBoy() {
        gameBoy.start();
    }

    public void stopGameBoy() {
        gameBoy.stop();
    }

    public void close() {
        dispose();
    }

    public StringBuilder getSerialContent() {
        return gameBoy.getSerialContent();
    }
    
}
