package com.azurelithium.gueimboi.gui;

import javax.swing.JFrame;

import com.azurelithium.gueimboi.common.GameBoy;

public class MainWindow extends JFrame {

    private static final long serialVersionUID = -7360770248156614083L;
    
    private Display display;
    private GameBoy gameBoy;

    public MainWindow() {
        display = new Display(this);
        add(display);
        pack();
        setSize(getWidth() * 2, getHeight() * 2);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void loadROM(String ROMPath) {
        gameBoy = new GameBoy(display, ROMPath);
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
