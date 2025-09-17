package com.application.panels;

import com.application.Application;

import javax.swing.*;
import java.awt.*;

public class ApplicationWindow extends JFrame {

    // Card layout elements visible to other application panels
    protected static final CardLayout layout = new CardLayout();
    protected static final JPanel cards = new JPanel(layout);

    // Singleton pattern
    private static final ApplicationWindow instance = new ApplicationWindow();

    public static ApplicationWindow getInstance() {
        return instance;
    }

    private ApplicationWindow() {
        super("Imogene");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800);
        setLocationRelativeTo(null);

        // Card for choosing to run the application locally or connect to a remote backend
        JPanel modeScreen = ModeScreen.getInstance();

        // Main application screen with the image view and sidebars
        JPanel imageScreen = ImageScreen.getInstance();

        cards.add(modeScreen, "Mode");
        cards.add(imageScreen, "Image");
        add(cards);

        layout.show(cards, "Mode");
    }

}
