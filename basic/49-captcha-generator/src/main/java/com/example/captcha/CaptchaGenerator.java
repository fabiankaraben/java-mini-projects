package com.example.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaGenerator {

    private static final int WIDTH = 160;
    private static final int HEIGHT = 60;
    private static final int FONT_SIZE = 40;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 6;

    public static class CaptchaResult {
        private final String text;
        private final BufferedImage image;

        public CaptchaResult(String text, BufferedImage image) {
            this.text = text;
            this.image = image;
        }

        public String getText() {
            return text;
        }

        public BufferedImage getImage() {
            return image;
        }
    }

    public static CaptchaResult generate() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // Random generator
        Random random = new Random();

        // Draw some noise (lines)
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 15; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g2d.drawLine(x1, y1, x2, y2);
        }

        // Generate text
        StringBuilder sb = new StringBuilder();
        g2d.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        for (int i = 0; i < LENGTH; i++) {
            char c = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
            sb.append(c);
            
            // Random color for each character
            g2d.setColor(new Color(random.nextInt(150), random.nextInt(150), random.nextInt(150)));
            
            // Draw character with some slight rotation or position jitter could be added, but keeping it simple for now
            int x = 20 + i * 20;
            int y = 45 + random.nextInt(10) - 5;
            g2d.drawString(String.valueOf(c), x, y);
        }
        
        g2d.dispose();
        
        return new CaptchaResult(sb.toString(), image);
    }
}
