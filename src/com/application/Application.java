package com.application;

import com.GA.crossover.BlendCrossover;
import com.GA.crossover.EnsembleCrossoverFunction;
import com.GA.crossover.PixelwiseRGBCrossover;
import com.GA.crossover.CrossoverFunction;
import com.GA.fitness.*;
import com.GA.GeneticAlgorithm;
import com.GA.ImageGenerator;
import com.GA.fitness.adjustment.FitnessAdjustment;
import com.GA.fitness.adjustment.NormalisationAdjustment;
import com.GA.generation.GenerationFunction;
import com.GA.generation.RandomBitmapGeneration;
import com.GA.generation.RandomColorGeneration;
import com.GA.mutation.*;
import com.GA.selection.RouletteWheelSelection;
import com.GA.selection.SelectionFunction;
import com.utils.BitMapImage;
import com.utils.ImageRW;
import com.utils.ImageUtils;

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

    private static JPanel modeScreen;
    private static JPanel imageScreen;

    // TODO: move elsewhere
    public static final boolean UPSCALE = true;
    public static final int UPSCALE_FACTOR = 6;

    public static int currentImageHeight = 100;
    public static int currentImageWidth = 133;

    private static BitMapImage currentImage;
    private static GeneticAlgorithm currentGA;


    public static void main(String[] args) {

        Font globalButtonFont = new Font("Arial", Font.BOLD, 16);

        // Apply it to all buttons globally
        UIManager.put("Button.font", globalButtonFont);

        // Create the main window frame
        frame = new JFrame("Imogene");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 800);
        frame.setLocationRelativeTo(null);

        // Initialise card layout
        layout = new CardLayout();
        cards = new JPanel(layout);

        modeScreen = createModeScreen();
        imageScreen = createImageScreen();

        cards.add(modeScreen, "Mode");
        cards.add(imageScreen, "Image");

        frame.add(cards);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        layout.show(cards, "Mode");

    }

    private static JPanel createModeScreen() {
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

        // Left sidebar
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel lblGeneration = new JLabel("Generation");
        JButton generateRandom = new JButton("Generate Random");
        generateRandom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImage = ImageGenerator.randomPixels(currentImageHeight, currentImageWidth);
                redraw();
            }
        });

        JButton generateColour = new JButton("Generate Colour");
        generateColour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImage = (new RandomColorGeneration()).generate(currentImageHeight, currentImageWidth).getImage();
                redraw();
            }
        });


        JLabel lblFilters = new JLabel("Filters");

        JButton filterGrayscale = new JButton("Grayscale");
        filterGrayscale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImage = ImageUtils.grayscale(currentImage);
                redraw();
            }
        });

        JButton filterSmooth = new JButton("Smooth");
        filterSmooth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImage = ImageUtils.smoothFilter(currentImage);
                redraw();
            }
        });

        JButton spectrumMaping = new JButton("Spectrum Map");
        spectrumMaping.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImage = ImageUtils.spectrumMaping(currentImage, new int[][] {new int[] {255, 0, 0}, new int[] {0, 255, 0}, new int[] {0, 0, 255}});
                redraw();
            }
        });

        JButton redRebalance = new JButton("Rebalance Red");
        redRebalance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImage = ImageUtils.rgbBalancing(currentImage, 0.6, 0.2, 0.2);
                redraw();
            }
        });

        JButton greenRebalance = new JButton("Rebalance Green");
        greenRebalance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImage = ImageUtils.rgbBalancing(currentImage, 0.2, 0.6, 0.2);
                redraw();
            }
        });

        JButton blueRebalance = new JButton("Rebalance Blue");
        blueRebalance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImage = ImageUtils.rgbBalancing(currentImage, 0.2, 0.2, 0.6);
                redraw();
            }
        });

        leftPanel.add(lblGeneration);
        leftPanel.add(generateRandom);
        leftPanel.add(generateColour);

        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        leftPanel.add(lblFilters);
        leftPanel.add(filterGrayscale);
        leftPanel.add(filterSmooth);
        leftPanel.add(spectrumMaping);
        leftPanel.add(redRebalance);
        leftPanel.add(greenRebalance);
        leftPanel.add(blueRebalance);


        // Middle image panel
        drawingPanel = new DrawingPanel(currentImageHeight, currentImageWidth);
        drawingPanel.setLayout(new GridBagLayout());

        // Center all buttons and labels of the left sidebar
        lblGeneration.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateRandom.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateColour.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblFilters.setAlignmentX(Component.CENTER_ALIGNMENT);
        filterGrayscale.setAlignmentX(Component.CENTER_ALIGNMENT);
        filterSmooth.setAlignmentX(Component.CENTER_ALIGNMENT);
        spectrumMaping.setAlignmentX(Component.CENTER_ALIGNMENT);
        redRebalance.setAlignmentX(Component.CENTER_ALIGNMENT);
        greenRebalance.setAlignmentX(Component.CENTER_ALIGNMENT);
        blueRebalance.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set all button widths to full width of the left sidebar
        generateRandom.setMaximumSize(new Dimension(Integer.MAX_VALUE, generateRandom.getPreferredSize().height));
        generateColour.setMaximumSize(new Dimension(Integer.MAX_VALUE, generateRandom.getPreferredSize().height));
        filterGrayscale.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterGrayscale.getPreferredSize().height));
        filterSmooth.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterSmooth.getPreferredSize().height));
        spectrumMaping.setMaximumSize(new Dimension(Integer.MAX_VALUE, spectrumMaping.getPreferredSize().height));
        redRebalance.setMaximumSize(new Dimension(Integer.MAX_VALUE, redRebalance.getPreferredSize().height));
        greenRebalance.setMaximumSize(new Dimension(Integer.MAX_VALUE, greenRebalance.getPreferredSize().height));
        blueRebalance.setMaximumSize(new Dimension(Integer.MAX_VALUE, blueRebalance.getPreferredSize().height));

        // Right sidebar
        CardLayout cardLayout = new CardLayout();
        JPanel rightPanel = new JPanel(cardLayout);

        // First panel, just the "Initialise GA" button
        JPanel pnlInitialiseGA = new JPanel();
        pnlInitialiseGA.setLayout(new BoxLayout(pnlInitialiseGA, BoxLayout.Y_AXIS));
        JButton btnInitialiseGA = new JButton("Initialise GA");
        btnInitialiseGA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(rightPanel, "GA Params");
            }
        });

        btnInitialiseGA.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInitialiseGA.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnInitialiseGA.getPreferredSize().height));

        pnlInitialiseGA.add(btnInitialiseGA);

        // Second panel, GA hyperparameters
        JPanel pnlGAHyperparameters = new JPanel();
        pnlGAHyperparameters.setLayout(new BoxLayout(pnlGAHyperparameters, BoxLayout.Y_AXIS));

        JLabel lblPopulationSize = new JLabel("Population Size");
        JLabel lblElite = new JLabel("Elite");
        JLabel lblReGeneration = new JLabel("Re-Generation");
        JLabel lblGenerationFunction = new JLabel("Generation Function");
        JLabel lblFitnessFunction = new JLabel("Fitness Function");
        JLabel lblSelectionFunction = new JLabel("Selection Function");
        JLabel lblCrossoverFunction = new JLabel("Crossover Function");
        JLabel lblMutationFunction = new JLabel("Mutation Function");

        JTextField txtPopulationSize = new JTextField("1000");
        JTextField txtElite = new JTextField("30");
        JTextField txtReGeneration = new JTextField("50");
        JComboBox<String> cmbGenerationFunction = new JComboBox<String>(new String[] {"Random Pixels"});
        JComboBox<String> cmbFitnessFunction = new JComboBox<String>(new String[] {"Image Likeness"});
        JComboBox<String> cmbSelectionFunction = new JComboBox<String>(new String[] {"Roulette Wheel"});
        JComboBox<String> cmbCrossoverFunction = new JComboBox<String>(new String[] {"Pixelwise Crossover"});
        JComboBox<String> cmbMutationFunction = new JComboBox<String>(new String[] {"Random Pixel Mutation"});

        JButton btnBeginGA = new JButton("Begin GA");
        btnBeginGA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int populationSize = Integer.parseInt(txtPopulationSize.getText());
                int elite = Integer.parseInt(txtElite.getText());
                int reGeneration = Integer.parseInt(txtReGeneration.getText());
                String generationFunctionOption = cmbGenerationFunction.getSelectedItem().toString();
                String fitnessFunctionOption = cmbFitnessFunction.getSelectedItem().toString();
                String selectionFunctionOption = cmbSelectionFunction.getSelectedItem().toString();
                String crossoverFunctionOption = cmbCrossoverFunction.getSelectedItem().toString();
                String mutationFunctionOption = cmbMutationFunction.getSelectedItem().toString();

                GenerationFunction generationFunction = new RandomBitmapGeneration();

                FitnessFunction fitnessFunction1 = new CheckerboardFitness();
                FitnessFunction fitnessFunction2 = new CheckerboardFitness();
                FitnessFunction fitnessFunction3 = new CheckerboardFitness();
                FitnessFunction fitnessFunction4 = new CheckerboardFitness();
                FitnessFunction fitnessFunction5 = new CheckerboardFitness();
                String targetPath = "benchmarkingImages/cans1.png";
                try {
                    fitnessFunction1 = new HueLikenessFitness(ImageRW.readImage(targetPath), currentImageHeight, currentImageWidth);
                    fitnessFunction2 = new SaturationLikenessFitness(ImageRW.readImage(targetPath), currentImageHeight, currentImageWidth);
                    fitnessFunction3 = new LightnessLikenessFitness(ImageRW.readImage(targetPath), currentImageHeight, currentImageWidth);
                    fitnessFunction4 = new ImageLikenessFitness(ImageRW.readImage(targetPath), currentImageHeight, currentImageWidth, true);
                    fitnessFunction5 = new ImageLikenessFitness(ImageRW.readImage(targetPath), currentImageHeight, currentImageWidth, false);
                } catch(Exception exception) {
                    exception.printStackTrace();
                    System.exit(0);
                }
                EnsembleFitnessFunction fitnessFunction = new EnsembleFitnessFunction();
                fitnessFunction.addFunction(fitnessFunction1, 1.0);
                fitnessFunction.addFunction(fitnessFunction2, 1.0);
                fitnessFunction.addFunction(fitnessFunction3, 1.0);
                fitnessFunction.addFunction(fitnessFunction4, 1.5);
                fitnessFunction.addFunction(fitnessFunction5, 1.5);
                fitnessFunction.makeNormalised(currentImageHeight, currentImageWidth);

                SelectionFunction selectionFunction = new RouletteWheelSelection();
                selectionFunction.makeRanked();

                CrossoverFunction crossOverFunction1 = new PixelwiseRGBCrossover();
                crossOverFunction1.makeWeighted();
                crossOverFunction1.makeGreedy(3, fitnessFunction);
                CrossoverFunction crossOverFunction2 = new BlendCrossover();
                crossOverFunction2.makeWeighted();
                EnsembleCrossoverFunction crossoverFunction = new EnsembleCrossoverFunction();
                crossoverFunction.addFunction(crossOverFunction1, 1.0);
                crossoverFunction.addFunction(crossOverFunction2, 0.01);


                EnsembleMutation mutationFunction = new EnsembleMutation(1.0);
                MutationFunction mutationFunction1 = new RandomPixelAdjustmentsMutation(1.0, 0.1, 3);
                MutationFunction mutationFunction2 = new SmoothMutation(1.0);
                MutationFunction mutationFunction3 = new RandomPixelsMutation(1.0, 0.1);
                mutationFunction1.makeGreedy(5, fitnessFunction);
                mutationFunction1.makeNoHarm(fitnessFunction);
                mutationFunction2.makeNoHarm(fitnessFunction);
                mutationFunction3.makeGreedy(5, fitnessFunction);
                mutationFunction3.makeNoHarm(fitnessFunction);
                mutationFunction.addFunction(mutationFunction1, 1.0);
                mutationFunction.addFunction(mutationFunction2, 1.0);
                mutationFunction.addFunction(mutationFunction3, 1.0);
                mutationFunction.makeNoHarm(fitnessFunction);
                FitnessAdjustment fitnessAdjustment = new NormalisationAdjustment();

//                GenerationFunction generationFunction = new RandomBitmapGeneration();
//                if(generationFunctionOption.equals("Random Pixels")) {
//                    generationFunction = new RandomBitmapGeneration();
//                }
//                EnsembleFitnessFunction fitnessFunction = new EnsembleFitnessFunction();
//                if(fitnessFunctionOption.equals("Image Likeness")) {
//                    try {
//                        BitMapImage image = ImageRW.readImage("benchmarkingImages/cans1.png");
//                        FitnessFunction fitnessFunction1 = new ImageLikenessFitness(image, currentImageHeight, currentImageWidth, false);
//                        fitnessFunction1.makeNormalised(currentImageHeight, currentImageWidth);
//                        fitnessFunction.addFunction(fitnessFunction1, 1.5);
//                        FitnessFunction fitnessFunction2 = new ImageLikenessFitness(image, currentImageHeight, currentImageWidth, true);
//                        fitnessFunction2.makeNormalised(currentImageHeight, currentImageWidth);
//                        fitnessFunction.addFunction(fitnessFunction2, 1.5);
//                        FitnessFunction fitnessFunction3 = new ValueLikenessFitness(image, currentImageHeight, currentImageWidth);
//                        fitnessFunction3.makeNormalised(currentImageHeight, currentImageWidth);
//                        fitnessFunction.addFunction(fitnessFunction3, 1.0);
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                        System.exit(0);
//                    }
//                }
//
//                SelectionFunction selectionFunction = new RouletteWheelSelection();
//
//                CrossoverFunction crossoverFunction = new PixelwiseCrossover();
//                crossoverFunction.makeWeighted();
//                crossoverFunction.makeGreedy(3, fitnessFunction);
//
//                MutationFunction mutationFunction1 = new RandomPixelsMutation(1.0, 0.1);
//                mutationFunction1.makeGreedy(3, fitnessFunction);
//                mutationFunction1.makeNoHarm(fitnessFunction);
//                MutationFunction mutationFunction2 = new RandomPixelAdjustmentsMutation(1.0, 0.3,5);
//                mutationFunction2.makeGreedy(3, fitnessFunction);
//                mutationFunction2.makeNoHarm(fitnessFunction);
//                EnsembleMutation mutationFunction = new EnsembleMutation(0.5);
//                mutationFunction.addFunction(mutationFunction1, 1.0);
//                mutationFunction.addFunction(mutationFunction2, 1.0);
//                mutationFunction.makeNoHarm(fitnessFunction);
//
//                FitnessAdjustment fitnessAdjustment = new NormalisationAdjustment();

                currentGA = new GeneticAlgorithm(
                        currentImageHeight,
                        currentImageWidth,
                        populationSize,
                        elite,
                        0,
                        reGeneration,
                        generationFunction,
                        fitnessFunction,
                        selectionFunction,
                        crossoverFunction,
                        mutationFunction,
                        fitnessAdjustment
                );

                cardLayout.show(rightPanel, "GA Generations");
            }
        });

        pnlGAHyperparameters.add(lblPopulationSize);
        pnlGAHyperparameters.add(txtPopulationSize);
        pnlGAHyperparameters.add(lblElite);
        pnlGAHyperparameters.add(txtElite);
        pnlGAHyperparameters.add(lblReGeneration);
        pnlGAHyperparameters.add(txtReGeneration);
        pnlGAHyperparameters.add(lblGenerationFunction);
        pnlGAHyperparameters.add(cmbGenerationFunction);
        pnlGAHyperparameters.add(lblFitnessFunction);
        pnlGAHyperparameters.add(cmbFitnessFunction);
        pnlGAHyperparameters.add(lblSelectionFunction);
        pnlGAHyperparameters.add(cmbSelectionFunction);
        pnlGAHyperparameters.add(lblCrossoverFunction);
        pnlGAHyperparameters.add(cmbCrossoverFunction);
        pnlGAHyperparameters.add(lblMutationFunction);
        pnlGAHyperparameters.add(cmbMutationFunction);
        pnlGAHyperparameters.add(btnBeginGA);

        lblPopulationSize.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblElite.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblReGeneration.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblGenerationFunction.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblFitnessFunction.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSelectionFunction.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblCrossoverFunction.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMutationFunction.setAlignmentX(Component.CENTER_ALIGNMENT);
        cmbGenerationFunction.setAlignmentX(Component.CENTER_ALIGNMENT);
        cmbFitnessFunction.setAlignmentX(Component.CENTER_ALIGNMENT);
        cmbSelectionFunction.setAlignmentX(Component.CENTER_ALIGNMENT);
        cmbCrossoverFunction.setAlignmentX(Component.CENTER_ALIGNMENT);
        cmbMutationFunction.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBeginGA.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblPopulationSize.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblPopulationSize.getPreferredSize().height));
        lblElite.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblElite.getPreferredSize().height));
        lblReGeneration.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblReGeneration.getPreferredSize().height));
        lblGenerationFunction.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblGenerationFunction.getPreferredSize().height));
        lblFitnessFunction.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblFitnessFunction.getPreferredSize().height));
        lblSelectionFunction.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblSelectionFunction.getPreferredSize().height));
        lblCrossoverFunction.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblCrossoverFunction.getPreferredSize().height));
        lblMutationFunction.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblMutationFunction.getPreferredSize().height));
        cmbGenerationFunction.setMaximumSize(new Dimension(Integer.MAX_VALUE, cmbGenerationFunction.getPreferredSize().height));
        cmbFitnessFunction.setMaximumSize(new Dimension(Integer.MAX_VALUE, cmbFitnessFunction.getPreferredSize().height));
        cmbSelectionFunction.setMaximumSize(new Dimension(Integer.MAX_VALUE, cmbSelectionFunction.getPreferredSize().height));
        cmbCrossoverFunction.setMaximumSize(new Dimension(Integer.MAX_VALUE, cmbCrossoverFunction.getPreferredSize().height));
        cmbMutationFunction.setMaximumSize(new Dimension(Integer.MAX_VALUE, cmbMutationFunction.getPreferredSize().height));
        txtPopulationSize.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtPopulationSize.getPreferredSize().height));
        txtElite.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtElite.getPreferredSize().height));
        txtReGeneration.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtReGeneration.getPreferredSize().height));
        btnBeginGA.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnBeginGA.getPreferredSize().height));

        JPanel pnlGenerations = new JPanel();
        pnlGenerations.setLayout(new BoxLayout(pnlGenerations, BoxLayout.Y_AXIS));

        JLabel lblGenerations = new JLabel("Generations");
        JTextField txtGenerations = new JTextField();
        JButton btnRun = new JButton("Run");
        btnRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnRun.setEnabled(false);
                int generations =  Integer.parseInt(txtGenerations.getText());
                new Thread(() -> {
                    for(int i = 0; i < generations; i++) {
                        currentGA.gaStep();
                        System.out.println("Step");
                        SwingUtilities.invokeLater(() -> {
                            currentImage = currentGA.best.getLast().getImage();
                            redraw();
                        });
                        System.out.println("Thread painted");
                    }
                    btnRun.setEnabled(true);
                    //ga.finished = true; // TODO: no longer needed
                }).start();
            }
        });

        pnlGenerations.add(lblGenerations);
        pnlGenerations.add(txtGenerations);
        pnlGenerations.add(btnRun);

        lblGenerations.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtGenerations.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblFitnessFunction.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblGenerations.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblGenerations.getPreferredSize().height));
        txtGenerations.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtGenerations.getPreferredSize().height));
        btnRun.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnRun.getPreferredSize().height));


        // Add panels to the right sidebar card layout
        rightPanel.add(pnlInitialiseGA, "GA Init");
        rightPanel.add(pnlGAHyperparameters, "GA Params");
        rightPanel.add(pnlGenerations, "GA Generations");

        // Display the first card
        cardLayout.show(rightPanel, "GA Init");

        // Create splitPanes to separate the screen into three resizable panels
        JSplitPane splitLeftCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, drawingPanel);
        splitLeftCenter .setResizeWeight(0.15);
        JSplitPane splitAll = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitLeftCenter, rightPanel);
        splitAll.setResizeWeight(0.85);

        // Create the image screen JPanel and return it
        JPanel imageScreen = new JPanel(new BorderLayout());
        imageScreen.setLayout(new BorderLayout());
        imageScreen.add(splitAll, BorderLayout.CENTER);
        return imageScreen;
    }

    private static JPanel createImageScreenOld() {
        JPanel imageScreen = new JPanel();
        imageScreen.setLayout(new BoxLayout(imageScreen, BoxLayout.X_AXIS));

        drawingPanel = new DrawingPanel(currentImageHeight, currentImageWidth);


        JButton btnRandom = new JButton("Generate random");
        btnRandom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImage = ImageGenerator.randomPixels(currentImageHeight, currentImageWidth);
                redraw();
            }
        });

        JButton btnGA = new JButton("Generate using GA");
        btnGA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int height = currentImageHeight;
                int width = currentImageWidth;
                int populationSize = 1000;
                int generations = 10000;
                int elite = 20;
                int additionalEDA = 0;
                int regeneration = 30;
                GenerationFunction generationFunction = new RandomBitmapGeneration();
                //GenerationFunction generationFunction = new RandomColorGeneration();
//                FitnessFunction fitnessFunction1 = new CheckerboardFitness();
//                try {
//                    fitnessFunction1 = new ImageLikenessFitness(ImageRW.readImage("benchmarkingImages/cans1.png"), height, width, true);
//                }
//                catch(Exception exception) {
//                    exception.printStackTrace();
//                    System.exit(0);
//                }
//                FitnessFunction fitnessFunction2 = new CheckerboardFitness();
//                try {
//                    fitnessFunction2 = new ImageLikenessFitness(ImageRW.readImage("benchmarkingImages/cans1.png"), height, width, false);
//                }
//                catch(Exception exception) {
//                    exception.printStackTrace();
//                    System.exit(0);
//                }
//                EnsembleFitnessFunction fitnessFunction = new EnsembleFitnessFunction();
//                fitnessFunction.addFunction(fitnessFunction1, 0.5);
//                fitnessFunction.addFunction(fitnessFunction2, 0.5);
//                fitnessFunction.makeNormalised(currentImageHeight, currentImageWidth);

                FitnessFunction fitnessFunction1 = new CheckerboardFitness();
                FitnessFunction fitnessFunction2 = new CheckerboardFitness();
                FitnessFunction fitnessFunction3 = new CheckerboardFitness();
                FitnessFunction fitnessFunction4 = new CheckerboardFitness();
                FitnessFunction fitnessFunction5 = new CheckerboardFitness();
                String targetPath = "benchmarkingImages/cans1.png";
                try {
                    fitnessFunction1 = new HueLikenessFitness(ImageRW.readImage(targetPath), height, width);
                    fitnessFunction2 = new SaturationLikenessFitness(ImageRW.readImage(targetPath), height, width);
                    fitnessFunction3 = new LightnessLikenessFitness(ImageRW.readImage(targetPath), height, width);
                    fitnessFunction4 = new ImageLikenessFitness(ImageRW.readImage(targetPath), height, width, true);
                    fitnessFunction5 = new ImageLikenessFitness(ImageRW.readImage(targetPath), height, width, false);
                } catch(Exception exception) {
                    exception.printStackTrace();
                    System.exit(0);
                }
                EnsembleFitnessFunction fitnessFunction = new EnsembleFitnessFunction();
                fitnessFunction.addFunction(fitnessFunction1, 1.0);
                //fitnessFunction.addFunction(fitnessFunction2, 1.0);
                fitnessFunction.addFunction(fitnessFunction3, 1.0);
                fitnessFunction.addFunction(fitnessFunction4, 1.5);
                fitnessFunction.addFunction(fitnessFunction5, 1.5);
                fitnessFunction.makeNormalised(height, width);

                SelectionFunction selectionFunction = new RouletteWheelSelection();
                selectionFunction.makeRanked();

                CrossoverFunction crossOverFunction1 = new PixelwiseRGBCrossover();
                crossOverFunction1.makeWeighted();
                crossOverFunction1.makeGreedy(3, fitnessFunction);
                CrossoverFunction crossOverFunction2 = new BlendCrossover();
                crossOverFunction2.makeWeighted();
                EnsembleCrossoverFunction crossOverFunction = new EnsembleCrossoverFunction();
                crossOverFunction.addFunction(crossOverFunction1, 1.0);
                crossOverFunction.addFunction(crossOverFunction2, 0.01);


                EnsembleMutation mutationFunction = new EnsembleMutation(1.0);
                MutationFunction mutationFunction1 = new RandomPixelAdjustmentsMutation(1.0, 0.1, 3);
                MutationFunction mutationFunction2 = new SmoothMutation(1.0);
                MutationFunction mutationFunction3 = new RandomPixelsMutation(1.0, 0.05);
                mutationFunction1.makeGreedy(5, fitnessFunction);
                mutationFunction1.makeNoHarm(fitnessFunction);
                mutationFunction2.makeNoHarm(fitnessFunction);
                mutationFunction3.makeGreedy(5, fitnessFunction);
                mutationFunction3.makeNoHarm(fitnessFunction);
                mutationFunction.addFunction(mutationFunction1, 1.0);
                mutationFunction.addFunction(mutationFunction2, 1.2);
                mutationFunction.addFunction(mutationFunction3, 1.0);
                mutationFunction.makeNoHarm(fitnessFunction);
                FitnessAdjustment fitnessAdjustment = new NormalisationAdjustment();
                //FitnessAdjustment fitnessAdjustment = new NoAdjustment();

                GeneticAlgorithm ga = new GeneticAlgorithm(
                        height,
                        width,
                        populationSize,
                        elite,
                        additionalEDA,
                        regeneration,
                        generationFunction,
                        fitnessFunction,
                        selectionFunction,
                        crossOverFunction,
                        mutationFunction,
                        fitnessAdjustment
                );

                new Thread(() -> {
                    for(int i = 0; i < generations; i++) {
                        ga.gaStep();
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

    private static void paintImage(BitMapImage image) {
//        imageScreen.remove(drawingPanel);
//        drawingPanel = new DrawingPanel(currentImageHeight, currentImageWidth);
//        imageScreen.add(drawingPanel, BorderLayout.CENTER);
//        imageScreen.setVisible(true);
        int[][][] bitmap = image.getRgb();
        for(int y = 0; y < currentImageHeight; y++) {
            for(int x = 0; x < currentImageWidth; x++) {
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
