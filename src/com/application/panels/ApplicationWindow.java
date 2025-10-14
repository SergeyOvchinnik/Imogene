package com.application.panels;

import com.application.Application;
import com.application.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

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

//        KeyStroke ctrlPlus = KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,
//                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
//
//        // Ctrl + Minus → decrease font
//        KeyStroke ctrlMinus = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,
//                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
//
//        JRootPane root = this.getRootPane();
//
//        // Register actions
//        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlPlus, "increaseFont");
//        root.getActionMap().put("increaseFont", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                FontManager.increaseFontSize();
//            }
//        });
//
//        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlMinus, "decreaseFont");
//        root.getActionMap().put("decreaseFont", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                FontManager.decreaseFontSize();
//            }
//        });

        // Card for choosing to run the application locally or connect to a remote backend
        JPanel modeScreen = ModeScreen.getInstance();

        // Main application screen with the image view and sidebars
        JPanel imageScreen = ImageScreen.getInstance();

        // Screen where the connection parameter is selected
        JPanel connectionScreen = ConnectionScreen.getInstance();

        // Assemble the card layout
        cards.add(modeScreen, "Mode");
        cards.add(imageScreen, "Image");
        cards.add(connectionScreen,"Connection");
        add(cards);

        // Display the first card
        layout.show(cards, "Mode");
    }

}
