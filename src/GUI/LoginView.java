package GUI;

import Control.Controller;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {
    private final Controller controller;
    private JTextField passwordField;
    
    public LoginView(Controller controller) {
        this.controller = controller;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Pannello centrale con form di login
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Logo o titolo
        JLabel titleLabel = new JLabel("Sistema Gestione Calciatori");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 30, 5);
        loginPanel.add(titleLabel, gbc);
        
        // Istruzioni
        JLabel instructionLabel = new JLabel("Inserisci la password per accedere come amministratore");
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 20, 5);
        loginPanel.add(instructionLabel, gbc);
        
        // Label password
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        loginPanel.add(passwordLabel, gbc);
        
        // Campo password
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(passwordField, gbc);
        
        // Bottone login
        JButton loginButton = new JButton("Accedi");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        loginPanel.add(loginButton, gbc);
        
        // Azioni del bottone login
        loginButton.addActionListener(_ -> {
            String password = passwordField.getText();
            controller.accediComeAmministratore(password);
            passwordField.setText(""); // Pulisci il campo password dopo il tentativo
        });
        
        // Aggiunta di un bottone per accedere automaticamente alla vista dei calciatori
        JButton viewCalciatoriButton = new JButton("Visualizza Calciatori");
        gbc.gridy = 4;
        loginPanel.add(viewCalciatoriButton, gbc);
        
        viewCalciatoriButton.addActionListener(_ -> {
            controller.vediElencoCalciatori();
        });
        
        add(loginPanel, BorderLayout.CENTER);
    }
}
