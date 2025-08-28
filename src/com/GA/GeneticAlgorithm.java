package com.GA;

import com.GA.crossover.CrossOverFunction;
import com.GA.fitness.FitnessFunction;
import com.GA.generation.GenerationFunction;
import com.GA.mutation.MutationFunction;
import com.GA.selection.SelectionFunction;
import com.application.Application;
import com.utils.BitMapImage;

import java.util.ArrayList;

public class GeneticAlgorithm {

    // TODO: fix encapsulation in this class
    public GenerationFunction generationFunction;
    public FitnessFunction fitnessFunction;
    public SelectionFunction selectionFunction;
    public CrossOverFunction crossOverFunction;
    public MutationFunction mutationFunction;

    public int width = 100;
    public int height = 100;

    public int populationSize = 1000;
    public int elite = 30;
    public int regeneration = 100;
    public int generations = 10000;
    public IndividualImage[] population;
    public boolean finished = false;

    public ArrayList<IndividualImage> best = new ArrayList<IndividualImage>();

    public GeneticAlgorithm(
            int height,
            int width,
            int populationSize,
            int generations,
            int elite,
            int regeneration,
            GenerationFunction generationFunction,
            FitnessFunction fitnessFunction,
            SelectionFunction selectionFunction,
            CrossOverFunction crossOverFunction,
            MutationFunction mutationFunction) {

        // Initialise parameters
        this.width = width;
        this.height = height;
        this.populationSize = populationSize;
        this.generations = generations;
        this.elite = elite;
        this.regeneration = regeneration;
        this.generationFunction = generationFunction;
        this.fitnessFunction = fitnessFunction;
        this.selectionFunction = selectionFunction;
        this.crossOverFunction = crossOverFunction;
        this.mutationFunction = mutationFunction;

        // Initialise population
        population = new IndividualImage[populationSize];
        for(int i = 0; i < populationSize; i++) {
            population[i] = generationFunction.generate(height, width);
            population[i].assignFitness(fitnessFunction.fitness(population[i]));
        }
    }

    public void step() {

        // Sort population by fitness
        for(int i = 0; i < populationSize - 1; i++) { // TODO: rewrite sorting
            for(int j = i; j < populationSize; j++) {
                if(population[i].getFitness() < population[j].getFitness()) {
                    IndividualImage temp = population[i];
                    population[i] = population[j];
                    population[j] = temp;
                }
            }
        }

        // Save the best individual in this generation
        best.add(population[0]);

        // Initialise population for the next generation
        IndividualImage[] nextPopulation = new IndividualImage[populationSize];

        // Copy over the elite
        for(int i = 0; i < elite; i++) {
            nextPopulation[i] = population[i];
        }

        // Use selection, crossover and mutation to create the next generation
        for(int i = elite; i < populationSize - regeneration; i++) {
            IndividualImage[] parents = selectionFunction.select(population);
            IndividualImage parent1 = parents[0];
            IndividualImage parent2 = parents[1];
            nextPopulation[i] = crossOverFunction.crossover(parent1, parent2);
            nextPopulation[i] = mutationFunction.mutate(nextPopulation[i]);
        }

        // Re-generate some individuals
        for(int i = populationSize - regeneration; i < populationSize; i++) {
            nextPopulation[i] = generationFunction.generate(height, width);
        }

        // Recalculate fitnesses of all individuals
        for(int i = 0; i < populationSize; i++) {
            nextPopulation[i].assignFitness(fitnessFunction.fitness(nextPopulation[i]));
        }

        // Update the population
        population = nextPopulation;

    }

}
