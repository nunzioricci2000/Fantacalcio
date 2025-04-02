package GUI;

import Control.Controller;

import javax.swing.*;
import java.awt.*;

public class LogoutView extends JPanel {
    private final Controller controller;
    
    public LogoutView(Controller controller) {
        this.controller = controller;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        
        JLabel infoLabel = new JLabel("Sei attualmente loggato come amministratore");
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(infoLabel, gbc);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setPreferredSize(new Dimension(150, 50));
        logoutButton.addActionListener(_ -> controller.logout());
        
        gbc.gridy = 1;
        gbc.insets = new Insets(50, 20, 20, 20);
        contentPanel.add(logoutButton, gbc);
        
        add(contentPanel, BorderLayout.CENTER);
    }
}
