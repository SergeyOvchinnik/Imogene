package com.application;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class DrawingPanel extends JPanel {
    private BufferedImage canvas;

    public DrawingPanel(int height, int width) {
        if(Application.UPSCALE) {
            width *= Application.UPSCALE_FACTOR;
            height *= Application.UPSCALE_FACTOR;
        }
        this.setSize(width, height);
        System.out.println(getWidth() + " by " + getHeight());
        canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        clearCanvas();
    }

    public void setPixel(int x, int y, Color color) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            canvas.setRGB(x, y, color.getRGB());
            repaint();
        }
    }

    public void clearCanvas() {
        Graphics2D g2d = canvas.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, this);
    }
}
