package com.application;

import com.GA.GeneticAlgorithm;
import com.GA.IndividualImage;
import com.application.panels.ApplicationWindow;
import com.application.panels.ImageScreen;
import com.utils.BitMapImage;
import com.utils.ImageRW;
import com.utils.ImageUtils;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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

        // Set up fonts to be used in the GUI
        FontManager.configureFonts();

        // Create and display the application window
        ApplicationWindow window = ApplicationWindow.getInstance();
        window.setVisible(true);

    }

    public static void saveGAAsGIF() {
        try (ImageOutputStream output = new FileImageOutputStream(new File("out/" + System.currentTimeMillis() + ".gif"))) {
            Converter writer = new Converter(output,
                    BufferedImage.TYPE_INT_RGB, 100, true); // 100 ms/frame, loop

            GeneticAlgorithm ga = ImageScreen.currentGA;
            ArrayList<IndividualImage> images = ga.best;

            for (IndividualImage frame : images) {
                writer.writeToSequence(ImageRW.toBufferedImage(frame.getImage()));
            }
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
