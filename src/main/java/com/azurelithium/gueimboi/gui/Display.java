package com.azurelithium.gueimboi.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display {

    class DisplayPanel extends JPanel {

        private BufferedImage img;
        private Image scaled;
    
        DisplayPanel(BufferedImage _img) {
            img = _img;
        }

        void setPixel(int x, int y, int rgb) {
            img.setRGB(x, y, rgb);
        }
    
        public void paint(Graphics g) {
            super.paintComponent(g);
            g.drawImage(scaled, 0, 0, Color.WHITE, null);
        }
    
        public void invalidate() {
            super.invalidate();
            int width = getWidth();
            int height = getHeight();
    
            if (width > 0 && height > 0) {
                scaled = img.getScaledInstance(getWidth(), getHeight(),
                Image.SCALE_SMOOTH);
            }
        }
    
        public Dimension getPreferredSize() {
            return new Dimension(img.getWidth(this), img.getHeight(this));
        }

    }

    private final long SECOND_IN_NS = 1000000000;
    private final int GAMEBOY_LCD_WIDTH = 160;
    private final int GAMEBOY_LCD_HEIGHT = 144;

    private final int[] COLORS = new int[] {
        Color.WHITE.getRGB(), 
        Color.LIGHT_GRAY.getRGB(), 
        Color.DARK_GRAY.getRGB(), 
        Color.BLACK.getRGB()
    };

    private JFrame frame;
    private DisplayPanel displayPanel;
    private double lastFrame;
    private double lastFrameRateRefresh;
    private int frames;
    private double refreshRateSumatory;
    private double nsInterval;
    private double delta = 1;

    public Display(double _frameRate) {
        nsInterval = SECOND_IN_NS / _frameRate;
        frame = new JFrame();
        displayPanel = new DisplayPanel(createGameboyLCD());
        frame.add(displayPanel);
        frame.pack();
        frame.setSize(frame.getWidth() * 2, frame.getHeight() * 2);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    } 

    private BufferedImage createGameboyLCD() {
        BufferedImage bufferedImage = new BufferedImage(GAMEBOY_LCD_WIDTH, GAMEBOY_LCD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setPaint(Color.WHITE);
        graphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        return bufferedImage;
    }

    public void setPixel(int x, int y, int colorCode) {
        displayPanel.setPixel(x, y, COLORS[colorCode]);
    }

    public void initializeFrameTime() {
        lastFrame = System.nanoTime();
        lastFrameRateRefresh = System.nanoTime();
    }

    public void waitRefresh() {
        while (true) {
            delta = (System.nanoTime() - lastFrame) / nsInterval;
            if (delta >= 1) { 
                refresh();
                updateFrameTime(); 
                delta--;   
                return;
            }
        }
    }

    public void refresh() { 
        double currentFrame = System.nanoTime();
        refreshRateSumatory += frameTime(currentFrame, lastFrame);
        frames++;
        if (currentFrame - lastFrameRateRefresh > SECOND_IN_NS / 10) {
            frame.setTitle(String.format("GueimBoi | %.2f fps", refreshRateSumatory/frames));
            lastFrameRateRefresh = lastFrameRateRefresh + SECOND_IN_NS / 10;
            refreshRateSumatory = 0;
            frames = 0;
        }    
        displayPanel.repaint();
        displayPanel.revalidate();
    }

    private double frameTime(double currentFrame, double lastFrame) {
        return SECOND_IN_NS / (currentFrame - lastFrame);
    }

    public void updateFrameTime() {
        lastFrame += nsInterval;
    }
    
}

