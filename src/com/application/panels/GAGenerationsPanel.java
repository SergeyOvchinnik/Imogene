package com.application.panels;

import com.application.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GAGenerationsPanel extends JPanel {

    public static int generationsRunning;
    public static int currentGenerationNumber;
    public static String status;
    public static JLabel statusLabel;

    // Singleton pattern
    private static final GAGenerationsPanel instance = new GAGenerationsPanel();

    public static GAGenerationsPanel getInstance() {
        return instance;
    }

    public GAGenerationsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel lblGenerations = new JLabel("Generations");
        JTextField txtGenerations = new JTextField();
        JButton btnRun = new JButton("Run");
        JButton btnHalt = new JButton("Halt");
        JButton btnReset = new JButton("Reset");
        btnRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageScreen.halt = false;
                btnRun.setEnabled(false);
                btnHalt.setEnabled(true);
                btnReset.setEnabled(false);
                int generations =  Integer.parseInt(txtGenerations.getText());
                generationsRunning = generations;
                new Thread(() -> {
                    status = "Running";
                    updateStatusString();
                    for(int i = 0; i < generations; i++) {
                        if(ImageScreen.halt) break;
                        currentGenerationNumber = i + 1;
                        updateStatusString();
                        ImageScreen.currentGA.gaStep();
                        if(ImageScreen.halt) break;
                        //System.out.println("Step");
                        SwingUtilities.invokeLater(() -> {
                            ImageScreen.currentImage = ImageScreen.currentGA.best.getLast().getImage();
                            ImageScreen.redraw();
                        });
                        //System.out.println("Thread painted");
                    }
                    btnRun.setEnabled(true);
                    btnHalt.setEnabled(false);
                    btnReset.setEnabled(true);
                    status = "Finished";
                    updateStatusString();
                    //ga.finished = true; // TODO: no longer needed
                }).start();
            }
        });

        btnHalt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = "Stopping...";
                updateStatusString();
                ImageScreen.halt = true;
                btnHalt.setEnabled(false);
                btnReset.setEnabled(false);
            }
        });
        btnHalt.setEnabled(false);

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RightSidebar.layout.show(RightSidebar.getInstance(), "GA Params");
            }
        });

        statusLabel = new JLabel("GA has been initialised");

        add(lblGenerations);
        add(txtGenerations);
        add(btnRun);
        add(btnHalt);
        add(btnReset);
        add(statusLabel);

        lblGenerations.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtGenerations.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRun.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHalt.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReset.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblGenerations.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblGenerations.getPreferredSize().height));
        txtGenerations.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtGenerations.getPreferredSize().height));
        btnRun.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnRun.getPreferredSize().height));
        btnHalt.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnHalt.getPreferredSize().height));
        statusLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, statusLabel.getPreferredSize().height));
        btnReset.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnReset.getPreferredSize().height));
    }

    public static void updateStatusString() {
        String statusString = "";
        if("Running".equals(status))
            statusString = "Running, generation " + currentGenerationNumber + "/" + generationsRunning;
        else if("Finished".equals(status))
            statusString = "Finished after " + currentGenerationNumber + " generations";
        else
            statusString = status;
        statusLabel.setText(statusString);
    }

}
