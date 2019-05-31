package com.azurelithium.gueimboi.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.azurelithium.gueimboi.common.GameBoy;

public class Display extends JPanel {

    private static final long serialVersionUID = 695771422856595687L;

    private final int GAMEBOY_LCD_WIDTH = 160;
    private final int GAMEBOY_LCD_HEIGHT = 144;

    private final int[] COLORS = new int[] {
        /*Color.WHITE.getRGB(), 
        Color.LIGHT_GRAY.getRGB(), 
        Color.DARK_GRAY.getRGB(), 
        Color.BLACK.getRGB()*/
        new Color(0xE0, 0xF8, 0xD0).getRGB(), 
        new Color(0x88, 0xC0, 0x70).getRGB(), 
        new Color(0x34, 0x68, 0x56).getRGB(), 
        new Color(0x08, 0x18, 0x20).getRGB()
    };

    private JFrame frame;
    private BufferedImage img;
    private Image scaled;

    private final long SECOND_IN_NS = 1000000000;

    private double delta = 1;
    private double frameRate = (double)GameBoy.GAMEBOY_CYCLE_RATE / GameBoy.CYCLES_PER_FRAME;
    private double nsInterval;
    private double lastFrame;
    private double lastFrameRateRefresh;
    private double frameTimeSumSinceLastFrameRateRefresh;
    private int framesSinceLastFrameRateRefresh;

    Display(JFrame _frame) {
        frame = _frame;
        img = createGameboyLCD();
        nsInterval = SECOND_IN_NS / frameRate;
    }

    public int getGameboyLCDWidth() {
        return GAMEBOY_LCD_WIDTH;
    }

    public int getGameboyLCDHeight() {
        return GAMEBOY_LCD_HEIGHT;
    }

    private BufferedImage createGameboyLCD() {
        BufferedImage bufferedImage = new BufferedImage(GAMEBOY_LCD_WIDTH, GAMEBOY_LCD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setPaint(Color.WHITE);
        graphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        return bufferedImage;
    }

    public void setPixel(int x, int y, int pixel) {
        img.setRGB(x, y, COLORS[pixel]);
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

    public void initializeFrameTime() {
        lastFrame = System.nanoTime();
        lastFrameRateRefresh = System.nanoTime();
    }

    public void waitRefresh() {
        while (true) {
            delta = (System.nanoTime() - lastFrame) / nsInterval;
            if (delta >= 1) {
                refreshTitle();
                updateFrameTime();
                refreshDisplay();              
                delta--;   
                return;
            }
        }
    }

    void refreshTitle() {
        double currentFrame = System.nanoTime();
        frameTimeSumSinceLastFrameRateRefresh += currentFrame - lastFrame;
        framesSinceLastFrameRateRefresh++;
        if (currentFrame - lastFrameRateRefresh > SECOND_IN_NS / 5) {
            frame.setTitle(String.format("GueimBoi | %.2f fps", 
                SECOND_IN_NS / (frameTimeSumSinceLastFrameRateRefresh/framesSinceLastFrameRateRefresh)));
            lastFrameRateRefresh += SECOND_IN_NS / 5;
            frameTimeSumSinceLastFrameRateRefresh = 0;
            framesSinceLastFrameRateRefresh = 0;
        }       
    }

    public void updateFrameTime() {
        lastFrame += nsInterval;
    }

    void refreshDisplay() {
        repaint();
        revalidate();
    }

    public void clear() {
        img = createGameboyLCD();
    }
    
}
