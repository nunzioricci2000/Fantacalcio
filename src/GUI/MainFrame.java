package GUI;

import Control.Controller;
import GUI.Calciatori.CalciatoriView;
import Model.Calciatore;
import UI.UserInterface;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame implements UserInterface {
    private Controller controller;
    private JTabbedPane tabbedPane;
    private LoginView loginView;
    private LogoutView logoutView;
    private CalciatoriView calciatoriView;
    private AdminView adminView;
    private JPanel statusBar;
    private JLabel statusLabel;
    
    // Indice della scheda di login/logout
    private final int loginTabIndex = 0;

    public MainFrame() { }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void initialize() {
        // Configurazione del JFrame
        setTitle("Gestione Calciatori");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Creazione dei componenti principali
        tabbedPane = new JTabbedPane();
        
        // Creazione delle viste
        loginView = new LoginView(controller);
        logoutView = new LogoutView(controller);
        calciatoriView = new CalciatoriView(controller);
        
        // Aggiungi le viste alla tab
        tabbedPane.addTab("Login", loginView);
        tabbedPane.addTab("Calciatori", calciatoriView);
        
        // Status bar per i messaggi
        statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusLabel = new JLabel(" ");
        statusBar.add(statusLabel, BorderLayout.CENTER);
        
        // Layout principale
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    @Override
    public void showMessage(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(new Color(0, 100, 0));
    }

    @Override
    public void showError(String error) {
        statusLabel.setText(error);
        statusLabel.setForeground(Color.RED);
        
        // Mostra anche un dialog per errori importanti
        JOptionPane.showMessageDialog(this, error, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showLoginView() {
        // Sostituisce la tab di logout con la tab di login
        tabbedPane.setComponentAt(loginTabIndex, loginView);
        tabbedPane.setTitleAt(loginTabIndex, "Login");
        tabbedPane.setSelectedIndex(loginTabIndex);
    }

    @Override
    public void showCalciatoriView() {
        tabbedPane.setSelectedComponent(calciatoriView);
        controller.vediElencoCalciatori();
    }

    @Override
    public void showAdminView() {
        // Crea la vista admin solo quando necessario (lazy initialization)
        if (adminView == null) {
            adminView = new AdminView(controller);
            tabbedPane.addTab("Amministrazione", adminView);
        }
        
        tabbedPane.setSelectedComponent(adminView);
    }

    @Override
    public void displayCalciatori(List<Calciatore> calciatori) {
        calciatoriView.displayCalciatori(calciatori);
    }

    @Override
    public void displayDettaglioCalciatore(Calciatore calciatore) {
        calciatoriView.displayDettaglioCalciatore(calciatore);
    }

    @Override
    public void notifyAdminMode(boolean isAdmin) {
        if (isAdmin) {
            // Sostituisce la tab di login con la tab di logout
            tabbedPane.setComponentAt(loginTabIndex, logoutView);
            tabbedPane.setTitleAt(loginTabIndex, "Logout");
            
            // Crea e mostra la vista admin
            if (adminView == null) {
                adminView = new AdminView(controller);
                tabbedPane.addTab("Amministrazione", adminView);
            }
        } else {
            // Rimuove la tab di amministrazione
            if (adminView != null) {
                tabbedPane.remove(adminView);
                adminView = null;
            }
            
            // Ripristina la tab di login
            tabbedPane.setComponentAt(loginTabIndex, loginView);
            tabbedPane.setTitleAt(loginTabIndex, "Login");
        }
    }

    @Override
    public void shutdown() {
        dispose();
        System.exit(0);
    }
}
