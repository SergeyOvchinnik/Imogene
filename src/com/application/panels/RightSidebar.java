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

public class RightSidebar extends JPanel {

    // Card layout elements visible to other application panels
    protected static final CardLayout layout = new CardLayout();

    private boolean remote = false;

    // Singleton pattern
    private static final RightSidebar instance = new RightSidebar();

    public static RightSidebar getInstance() {
        return instance;
    }

    public RightSidebar() {
        super(layout);

        // First panel, just the "Initialise GA" button
        JPanel pnlInitialiseGA = GAInitPanel.getInstance();

        // Second panel, GA hyperparameters
        JPanel pnlGAHyperparameters = GAParametersPanel.getInstance();

        // Third panel, GA generations and control buttons
        JPanel pnlGenerations = GAGenerationsPanel.getInstance();

        // Add panels to the right sidebar card layout
        add(pnlInitialiseGA, "GA Init");
        add(pnlGAHyperparameters, "GA Params");
        add(pnlGenerations, "GA Generations");

        // Add the cards panel to the right sidebar
        //add(cards);

        // Display the first card
        layout.show(this, "GA Init");

    }

    public void makeRemote() {

    }

}
