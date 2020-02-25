package com.azurelithium.gueimboi.gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.azurelithium.gueimboi.common.GameBoy;
import com.azurelithium.gueimboi.joypad.InputController;

public class MainWindow extends JFrame {

    class LoadROM extends AbstractAction {
        private static final long serialVersionUID = -4582912918514243129L;

        final JFileChooser fc = new JFileChooser();

        public LoadROM() {
            super("Load ROM");
        }
    
        public void actionPerformed(ActionEvent e) {            
            int returnVal = fc.showOpenDialog(MainWindow.this);
    
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                loadROM(file.getAbsolutePath());
            }
        }
    }
    
    class About extends AbstractAction {
        private static final long serialVersionUID = 1254062046688479693L;

        public About() {
            super("About GueimBoi");
        }
    
        public void actionPerformed(ActionEvent e) {            
            String repoURL = "https://github.com/AzureLithium/GueimBoi";

            JDialog dialog = new JDialog(MainWindow.this);

            JPanel panel = new JPanel();

            JTextPane textPane = new JTextPane();
            textPane.setContentType("text/html"); 
            textPane.setText("<html><center>"
                + "<b>GueimBoi: GameBoy Classic Emulator</b><br><br>"
                + "Experimental and self-learning project.<br><br>"
                + "Author: Javier Lario (javi.lario@gmail.com)<br>"
                + "Source: <a href=\"" + repoURL + "\">" + repoURL +"</a><br><br>"
                + "GueimBoi is licensed under the<br>"
                + "GNU General Public License v3.0"
                + "</center></html>");
            textPane.addHyperlinkListener(new HyperlinkListener() {
                @Override
                public void hyperlinkUpdate(HyperlinkEvent e)
                {
                    if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                        try {                            
                            Desktop.getDesktop().browse(new URI(e.getURL().toString()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            textPane.setEditable(false);
            textPane.setBackground(null);

            panel.add(textPane);
            dialog.add(panel);
            dialog.setResizable(false);
            dialog.pack();
            dialog.setLocationRelativeTo(MainWindow.this);
            dialog.setVisible(true);
        }
    }

    private static final long serialVersionUID = -7360770248156614083L;
    
    private Display display;
    private InputController joypad;
    private InputListener inputListener;
    private GameBoy gameBoy;

    public MainWindow() {
        createMenus();
        display = new Display(this);
        add(display);
        pack();
        setSize(getWidth(), getHeight());
        //setSize(getWidth() * 2, getHeight() * 2);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        joypad = new InputController();
        inputListener = new InputListener(joypad);
        addKeyListener(inputListener);
    }

    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadROMMenuItem = new JMenuItem(new LoadROM());
        fileMenu.add(loadROMMenuItem);
        menuBar.add(fileMenu);
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem(new About());
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    public void loadROM(String ROMPath) {
        stopGameBoy();
        gameBoy = new GameBoy(display, joypad, ROMPath);
        initializeGameBoy();
        startGameBoy();
    }

    public void initializeGameBoy() {        
        if (gameBoy != null) {
            gameBoy.initialize();
        }
    }

    public void startGameBoy() {        
        if (gameBoy != null) {
            gameBoy.start();
        }
    }

    public void stopGameBoy() {
        if (gameBoy != null) {
            gameBoy.stop();
        }
    }

    public void close() {
        dispose();
    }

    public StringBuilder getSerialContent() {
        return gameBoy.getSerialContent();
    }
    
}


