package com.application.panels;

import com.GA.GeneticAlgorithm;
import com.GA.crossover.BlendCrossover;
import com.GA.crossover.CrossoverFunction;
import com.GA.crossover.EnsembleCrossoverFunction;
import com.GA.crossover.PixelwiseRGBCrossover;
import com.GA.fitness.*;
import com.GA.fitness.adjustment.FitnessAdjustment;
import com.GA.fitness.adjustment.NormalisationAdjustment;
import com.GA.generation.GenerationFunction;
import com.GA.generation.RandomBitmapGeneration;
import com.GA.mutation.*;
import com.GA.selection.RouletteWheelSelection;
import com.GA.selection.SelectionFunction;
import com.application.Application;
import com.utils.ImageRW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GAParametersPanel extends JPanel {

    // Singleton pattern
    private static final GAParametersPanel instance = new GAParametersPanel();

    public static GAParametersPanel getInstance() {
        return instance;
    }

    public GAParametersPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

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
                String targetPath = "benchmarkingImages/sergio1.png";
                try {
                    fitnessFunction1 = new HueLikenessFitness(ImageRW.readImage(targetPath), ImageScreen.currentImageHeight, ImageScreen.currentImageWidth);
                    fitnessFunction2 = new SaturationLikenessFitness(ImageRW.readImage(targetPath), ImageScreen.currentImageHeight, ImageScreen.currentImageWidth);
                    fitnessFunction3 = new LightnessLikenessFitness(ImageRW.readImage(targetPath), ImageScreen.currentImageHeight, ImageScreen.currentImageWidth);
                    fitnessFunction4 = new ImageLikenessFitness(ImageRW.readImage(targetPath), ImageScreen.currentImageHeight, ImageScreen.currentImageWidth, true);
                    fitnessFunction5 = new ImageLikenessFitness(ImageRW.readImage(targetPath), ImageScreen.currentImageHeight, ImageScreen.currentImageWidth, false);
                } catch(Exception exception) {
                    exception.printStackTrace();
                    System.exit(0);
                }
                EnsembleFitnessFunction fitnessFunction = new EnsembleFitnessFunction();
                fitnessFunction.addFunction(fitnessFunction1, 0.5);
                //fitnessFunction.addFunction(fitnessFunction2, 1.0);
                fitnessFunction.addFunction(fitnessFunction3, 0.5);
                fitnessFunction.addFunction(fitnessFunction4, 1.5);
                fitnessFunction.addFunction(fitnessFunction5, 1.5);
                fitnessFunction.makeNormalised(ImageScreen.currentImageHeight, ImageScreen.currentImageWidth);

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

                ImageScreen.currentGA = new GeneticAlgorithm(
                        ImageScreen.currentImageHeight,
                        ImageScreen.currentImageWidth,
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

                RightSidebar.layout.show(RightSidebar.getInstance(), "GA Generations");
            }
        });

        add(lblPopulationSize);
        add(txtPopulationSize);
        add(lblElite);
        add(txtElite);
        add(lblReGeneration);
        add(txtReGeneration);
        add(lblGenerationFunction);
        add(cmbGenerationFunction);
        add(lblFitnessFunction);
        add(cmbFitnessFunction);
        add(lblSelectionFunction);
        add(cmbSelectionFunction);
        add(lblCrossoverFunction);
        add(cmbCrossoverFunction);
        add(lblMutationFunction);
        add(cmbMutationFunction);
        add(btnBeginGA);

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
    }

}
