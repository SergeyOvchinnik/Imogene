package com.application;

import com.application.panels.ApplicationWindow;
import com.application.panels.ImageScreen;
import com.utils.BitMapImage;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


/**
 * TODO:
 *
 * - Implement an image-to-image animation generation, e.g. upload two images, then run a GA that tries to optimise the likeness of the population to the combination of the first and second image, e.g. fitness is 100% of similarity to first image + 0% similarity to second. As the generation pass, the balance shifts towards the later image.
 *
 *
 */


public class Application {

    public static Random rng = new Random();

    public static void main(String[] args) {

        // TODO: move fonts to a font manager
        Font globalButtonFont = new Font("Arial", Font.BOLD, 16);

        // Apply it to all buttons globally
        UIManager.put("Button.font", globalButtonFont);

        ApplicationWindow window = ApplicationWindow.getInstance();

        window.setVisible(true);

    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
