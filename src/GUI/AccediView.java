package GUI;

import Control.Controller;

import javax.swing.*;

public class AccediView extends JPanel {
    public AccediView(Controller controller) {
        super();
        JLabel passwordLabel = setUpPasswordLabel();
        JPasswordField passwordField = setUpPasswordField();
        JButton loginButton = setUpLoginButton(controller, passwordField);
        this.add(passwordLabel);
        this.add(passwordField);
        this.add(loginButton);
    }

    private JLabel setUpPasswordLabel() {
        return new JLabel("Password:");
    }

    private JPasswordField setUpPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        passwordField.setColumns(16);
        return passwordField;
    }

    private JButton setUpLoginButton(Controller controller, JPasswordField passwordField) {
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            controller.accediComeAmministratore(passwordField.getText());
        });
        return loginButton;
    }
}
