package com.application;

import javax.swing.*;
import java.awt.*;

public class ConnectScreen extends JFrame {

    public ConnectScreen() {
        super();
        this.setTitle("Mode");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(3, 2));
        this.setSize(500, 300);
        JLabel lblIP = new JLabel("IP address:");
        this.add(lblIP);
        JTextField fldIP = new JTextField();
        this.add(fldIP);
        JLabel lblPassword = new JLabel("Password:");
        this.add(lblPassword);
        JPasswordField fldPassword = new JPasswordField();
        fldPassword.setEchoChar('*');
        this.add(fldPassword);
        JButton btnConnect = new JButton("Connect");
        this.add(btnConnect);
        this.pack();
        this.setLocationRelativeTo(null);
    }

}
