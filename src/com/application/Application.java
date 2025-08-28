package com.application;

import com.GA.crossover.BitwiseCrossOver;
import com.GA.crossover.CrossOverFunction;
import com.GA.fitness.CheckerboardFitness;
import com.GA.fitness.ColourFitness;
import com.GA.GeneticAlgorithm;
import com.GA.ImageGenerator;
import com.GA.fitness.FitnessFunction;
import com.GA.generation.GenerationFunction;
import com.GA.generation.RandomBitmapGeneration;
import com.GA.generation.RandomColorGeneration;
import com.GA.mutation.MutationFunction;
import com.GA.mutation.PaintCanMutation;
import com.GA.mutation.RandomPixelsMutation;
import com.GA.selection.RouletteWheelSelection;
import com.GA.selection.SelectionFunction;
import com.utils.BitMapImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


/**
 * TODO:
 *
 * - Implement an image-to-image animation generation, e.g. upload two images, then run a GA that tries to optimise the likeness of the population to the combination of the first and second image, e.g. fitness is 100% of similarity to first image + 0% similarity to second. As the generation pass, the balance shifts towards the later image.
 *
 *
 */


public class Application {

    public static Random rng =  new Random();

    private static JFrame frame;
    private static CardLayout layout;
    private static JPanel cards;
    private static DrawingPanel drawingPanel;

    private static JPanel choiceScreen;
    private static JPanel imageScreen;

    // TODO: move elsewhere
    public static final boolean UPSCALE = true;
    public static final int UPSCALE_FACTOR = 5;

    public static int currentImageWidth = 50;
    public static int currentImageHeight = 50;

    public static BitMapImage currentImage;

    public static void main(String[] args) {

        Font globalButtonFont = new Font("Arial", Font.BOLD, 16);

        // Apply it to all buttons globally
        UIManager.put("Button.font", globalButtonFont);

        // Create the main window frame
        frame = new JFrame("Manmade Horrors");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Initialise card layout
        layout = new CardLayout();
        cards = new JPanel(layout);

        choiceScreen = createChoiceScreen();
        imageScreen = createImageScreen();

        cards.add(choiceScreen, "Choice");
        cards.add(imageScreen, "Image");

        frame.add(cards);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        layout.show(cards, "Choice");

    }

    private static JPanel createChoiceScreen() {
        JPanel choiceScreen = new JPanel(new GridBagLayout());
        JButton btnLocal = new JButton("Run locally");
        JButton btnRemote = new JButton("Run remotely");
        btnLocal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layout.show(cards, "Image");
            }
        });

        Dimension buttonSize = new Dimension(300, 80);
        btnLocal.setPreferredSize(buttonSize);
        btnRemote.setPreferredSize(buttonSize);

        int buttonSpacing = 20;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, buttonSpacing/2);
        gbc.gridx = 0;
        choiceScreen.add(btnLocal, gbc);

        gbc.insets = new Insets(5, buttonSpacing/2, 5, 0);
        gbc.gridx = 1;
        choiceScreen.add(btnRemote, gbc);

        return choiceScreen;
    }

    private static JPanel createImageScreen() {
        JPanel imageScreen = new JPanel();
        imageScreen.setLayout(new BoxLayout(imageScreen, BoxLayout.X_AXIS));

        drawingPanel = new DrawingPanel(currentImageWidth, currentImageHeight);


        JButton btnRandom = new JButton("Generate random");
        btnRandom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BitMapImage image = ImageGenerator.randomPixels(currentImageWidth, currentImageHeight);
                paintImage(image);

            }
        });

        JButton btnGA = new JButton("Generate using GA");
        btnGA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int height = currentImageHeight;
                int width = currentImageWidth;
                int populationSize = 1000;
                int generations = 1000;
                int elite = 30;
                int regeneration = 50;
                GenerationFunction generationFunction = new RandomColorGeneration();
                FitnessFunction fitnessFunction = new CheckerboardFitness();
                SelectionFunction selectionFunction = new RouletteWheelSelection();
                selectionFunction.makeRanked();
                CrossOverFunction crossOverFunction = new BitwiseCrossOver();
                crossOverFunction.makeGreedy(3, fitnessFunction);
                //MutationFunction mutationFunction = new RandomPixelsMutation(0.1, 0.1);
                MutationFunction mutationFunction = new PaintCanMutation();
                //mutationFunction.makeGreedy(5, fitnessFunction);

                GeneticAlgorithm ga = new GeneticAlgorithm(
                        height,
                        width,
                        populationSize,
                        generations,
                        elite,
                        regeneration,
                        generationFunction,
                        fitnessFunction,
                        selectionFunction,
                        crossOverFunction,
                        mutationFunction
                );

                new Thread(() -> {
                    for(int i = 0; i < ga.generations; i++) {
                        ga.step();
                        System.out.println("Step");
                        SwingUtilities.invokeLater(() -> {
                            currentImage = ga.best.getLast().getImage();
                            redraw();
                        });
                        System.out.println("Thread painted");
                    }
                    ga.finished = true;
                }).start();

            }
        });

        imageScreen.add(drawingPanel);
        drawingPanel.setVisible(true);
        imageScreen.add(btnRandom);
        imageScreen.add(btnGA);

        return imageScreen;
    }

    public static void drawPixel(int x, int y, Color color) {
        if(UPSCALE) {
            //System.out.println("O: " + x + ", " + y);
            for(int xc = x*UPSCALE_FACTOR; xc < x*UPSCALE_FACTOR + UPSCALE_FACTOR; xc++)
                for(int yc = y*UPSCALE_FACTOR; yc < y*UPSCALE_FACTOR + UPSCALE_FACTOR; yc++) {
                    //System.out.println(xc + ", " + yc);
                    drawingPanel.setPixel(xc, yc, color);
                }
        }
        else
            drawingPanel.setPixel(x, y, color);
    }

    public static void redraw() {
        paintImage(currentImage);
    }

    public static void paintImage(BitMapImage image) {
        imageScreen.remove(drawingPanel);
        drawingPanel = new DrawingPanel(currentImageHeight, currentImageWidth);
        imageScreen.add(drawingPanel);
        imageScreen.setVisible(true);
        int[][][] bitmap = image.getRgb();
        for(int y = 0; y < Application.currentImageWidth; y++) {
            for(int x = 0; x < Application.currentImageHeight; x++) {
                drawPixel(x, y, new Color(bitmap[y][x][0], bitmap[y][x][1], bitmap[y][x][2]));
            }
        }
        System.out.println("Painted image");
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
