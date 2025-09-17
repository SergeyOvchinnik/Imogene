package com.application.panels;

import com.GA.ImageGenerator;
import com.GA.generation.RandomColorGeneration;
import com.application.Application;
import com.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeftSidebar extends JPanel {

    // Singleton pattern
    private static final LeftSidebar instance = new LeftSidebar();

    public static LeftSidebar getInstance() {
        return instance;
    }

    public LeftSidebar() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel lblGeneration = new JLabel("Generation");
        JButton generateRandom = new JButton("Generate Random");
        generateRandom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageGenerator.randomPixels(ImageScreen.currentImageHeight, ImageScreen.currentImageWidth);
                ImageScreen.redraw();
            }
        });

        JButton generateColour = new JButton("Generate Colour");
        generateColour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = (new RandomColorGeneration()).generate(ImageScreen.currentImageHeight, ImageScreen.currentImageWidth).getImage();
                ImageScreen.redraw();
            }
        });


        JLabel lblFilters = new JLabel("Filters");

        JButton filterGrayscale = new JButton("Grayscale");
        filterGrayscale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.grayscale(ImageScreen.currentImage);
                ImageScreen.redraw();
            }
        });

        JButton filterSmoothSoft = new JButton("Smooth (soft)");
        filterSmoothSoft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.smoothFilter(ImageScreen.currentImage, 0.8,  0.025);
                ImageScreen.redraw();
            }
        });

        JButton filterSmoothMedium = new JButton("Smooth (medium)");
        filterSmoothMedium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.smoothFilter(ImageScreen.currentImage, 0.5, 0.0625);
                ImageScreen.redraw();
            }
        });

        JButton filterSmoothHard = new JButton("Smooth (hard)");
        filterSmoothHard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.smoothFilter(ImageScreen.currentImage, 0.2, 0.1);
                ImageScreen.redraw();
            }
        });

        JButton filterInvert = new JButton("Invert");
        filterInvert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.invert(ImageScreen.currentImage);
                ImageScreen.redraw();
            }
        });

//        JButton spectrumMaping = new JButton("Spectrum Map");
//        spectrumMaping.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                currentImage = ImageUtils.spectrumMaping(currentImage, new int[][] {new int[] {255, 0, 0}, new int[] {0, 255, 0}, new int[] {0, 0, 255}});
//                redraw();
//            }
//        });

        JButton redRebalance = new JButton("Rebalance Red");
        redRebalance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.rgbBalancing(ImageScreen.currentImage, 0.6, 0.2, 0.2);
                ImageScreen.redraw();
            }
        });

        JButton greenRebalance = new JButton("Rebalance Green");
        greenRebalance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.rgbBalancing(ImageScreen.currentImage, 0.2, 0.6, 0.2);
                ImageScreen.redraw();
            }
        });

        JButton blueRebalance = new JButton("Rebalance Blue");
        blueRebalance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.rgbBalancing(ImageScreen.currentImage, 0.2, 0.2, 0.6);
                ImageScreen.redraw();
            }
        });

        JLabel lblProjections = new JLabel("Spectrum Projections");

        JButton btnRedOntoGreen = new JButton("Red -> Green");
        btnRedOntoGreen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.spectralProjection(ImageScreen.currentImage, "Red", "Green");
                ImageScreen.redraw();
            }
        });

        JButton btnGreenOntoBlue = new JButton("Green -> Blue");
        btnGreenOntoBlue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.spectralProjection(ImageScreen.currentImage, "Green", "Blue");
                ImageScreen.redraw();
            }
        });

        JButton btnBlueOntoRed = new JButton("Blue -> Red");
        btnBlueOntoRed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.spectralProjection(ImageScreen.currentImage, "Blue", "Red");
                ImageScreen.redraw();
            }
        });

        JButton btnHueOntoSaturation = new JButton("Hue -> Saturation (red)");
        btnHueOntoSaturation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.spectralProjection(ImageScreen.currentImage, "Hue", "Saturation");
                ImageScreen.redraw();
            }
        });

        JButton btnSaturationOntoLightness = new JButton("Saturation -> Lightness");
        btnSaturationOntoLightness.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.spectralProjection(ImageScreen.currentImage, "Saturation", "Lightness");
                ImageScreen.redraw();
            }
        });

        JButton btnLightnessOntoHue = new JButton("Lightness -> Hue");
        btnLightnessOntoHue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.spectralProjection(ImageScreen.currentImage, "Lightness", "Hue");
                ImageScreen.redraw();
            }
        });

        JButton btnCustom = new JButton("Custom");
        btnCustom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.currentImage = ImageUtils.spectralProjection(ImageScreen.currentImage, "Red", "Hue");
                ImageScreen.redraw();
            }
        });

        // Generation section
        add(lblGeneration);
        add(generateRandom);
        add(generateColour);

        // Separator
        add(Box.createVerticalStrut(10));
        add(new JSeparator(SwingConstants.HORIZONTAL));

        // Filter section
        add(lblFilters);
        add(filterGrayscale);
        add(filterSmoothSoft);
        add(filterSmoothMedium);
        add(filterSmoothHard);
        add(filterInvert);
        //leftPanel.add(spectrumMaping);
        add(redRebalance);
        add(greenRebalance);
        add(blueRebalance);

        // Separator
        add(Box.createVerticalStrut(10));
        add(new JSeparator(SwingConstants.HORIZONTAL));

        // Spectrum projection section
        add(lblProjections);
        add(btnRedOntoGreen);
        add(btnGreenOntoBlue);
        add(btnBlueOntoRed);
        add(btnHueOntoSaturation);
        add(btnSaturationOntoLightness);
        add(btnLightnessOntoHue);
        add(btnCustom);

        // Center all buttons and labels of the left sidebar
        lblGeneration.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateRandom.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateColour.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblFilters.setAlignmentX(Component.CENTER_ALIGNMENT);
        filterGrayscale.setAlignmentX(Component.CENTER_ALIGNMENT);
        filterSmoothSoft.setAlignmentX(Component.CENTER_ALIGNMENT);
        filterSmoothMedium.setAlignmentX(Component.CENTER_ALIGNMENT);
        filterSmoothHard.setAlignmentX(Component.CENTER_ALIGNMENT);
        filterInvert.setAlignmentX(Component.CENTER_ALIGNMENT);
        //spectrumMaping.setAlignmentX(Component.CENTER_ALIGNMENT);
        redRebalance.setAlignmentX(Component.CENTER_ALIGNMENT);
        greenRebalance.setAlignmentX(Component.CENTER_ALIGNMENT);
        blueRebalance.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblProjections.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRedOntoGreen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGreenOntoBlue.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBlueOntoRed.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHueOntoSaturation.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSaturationOntoLightness.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLightnessOntoHue.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCustom.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set all button widths to full width of the left sidebar
        generateRandom.setMaximumSize(new Dimension(Integer.MAX_VALUE, generateRandom.getPreferredSize().height));
        generateColour.setMaximumSize(new Dimension(Integer.MAX_VALUE, generateColour.getPreferredSize().height));
        filterGrayscale.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterGrayscale.getPreferredSize().height));
        filterSmoothSoft.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterSmoothSoft.getPreferredSize().height));
        filterSmoothMedium.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterSmoothMedium.getPreferredSize().height));
        filterSmoothHard.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterSmoothHard.getPreferredSize().height));
        filterInvert.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterInvert.getPreferredSize().height));
        //spectrumMaping.setMaximumSize(new Dimension(Integer.MAX_VALUE, spectrumMaping.getPreferredSize().height));
        redRebalance.setMaximumSize(new Dimension(Integer.MAX_VALUE, redRebalance.getPreferredSize().height));
        greenRebalance.setMaximumSize(new Dimension(Integer.MAX_VALUE, greenRebalance.getPreferredSize().height));
        blueRebalance.setMaximumSize(new Dimension(Integer.MAX_VALUE, blueRebalance.getPreferredSize().height));
        // lblProjections.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblProjections.getPreferredSize().height));
        btnRedOntoGreen.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnRedOntoGreen.getPreferredSize().height));
        btnGreenOntoBlue.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnGreenOntoBlue.getPreferredSize().height));
        btnBlueOntoRed.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnBlueOntoRed.getPreferredSize().height));
        btnHueOntoSaturation.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnHueOntoSaturation.getPreferredSize().height));
        btnSaturationOntoLightness.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnSaturationOntoLightness.getPreferredSize().height));
        btnLightnessOntoHue.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnLightnessOntoHue.getPreferredSize().height));
        btnCustom.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnCustom.getPreferredSize().height));

    }

}
