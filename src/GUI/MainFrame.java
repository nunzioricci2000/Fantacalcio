package GUI;

import Control.Controller;
import GUI.Calciatori.CalciatoriAggiungiView;
import GUI.Calciatori.CalciatoriModificaView;
import GUI.Calciatori.CalciatoriView;
import GUI.Calciatori.MilitanzeAggiungiView;
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
    
    // Tab composite per le funzionalità di gestione
    private JTabbedPane aggiungiTabPane;
    private JTabbedPane modificaTabPane;
    
    // Views per le funzionalità di gestione
    private CalciatoriAggiungiView aggiungiCalciatoreView;
    private MilitanzeAggiungiView aggiungiMilitanzaView;
    private CalciatoriModificaView modificaCalciatoreView;
    
    private JPanel statusBar;
    private JLabel statusLabel;
    private final int loginTabIndex = 0;
    
    // Indici per le tab di gestione
    private final String aggiungiTabName = "Aggiungi";
    private final String modificaTabName = "Modifica";

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
        
        // Creazione delle viste standard
        loginView = new LoginView(controller);
        logoutView = new LogoutView(controller);
        calciatoriView = new CalciatoriView(controller);
        
        // Creazione delle viste di gestione
        aggiungiCalciatoreView = new CalciatoriAggiungiView(controller);
        aggiungiMilitanzaView = new MilitanzeAggiungiView(controller);
        modificaCalciatoreView = new CalciatoriModificaView(controller);
        
        // Creazione dei tab panel per raggruppare le funzionalità
        aggiungiTabPane = new JTabbedPane();
        aggiungiTabPane.addTab("Calciatore", aggiungiCalciatoreView);
        aggiungiTabPane.addTab("Militanza", aggiungiMilitanzaView);
        
        modificaTabPane = new JTabbedPane();
        modificaTabPane.addTab("Calciatore", modificaCalciatoreView);
        
        // Aggiungi le viste standard alla tab
        tabbedPane.addTab("Login", loginView);
        tabbedPane.addTab("Calciatori", calciatoriView);
        // Le tab di gestione verranno aggiunte solo quando l'utente accede come admin
        
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
    public void showAggiungiCalciatoreView() {
        // Verifica che la tab Aggiungi sia presente
        int aggiungiIndex = tabbedPane.indexOfTab(aggiungiTabName);
        if (aggiungiIndex != -1) {
            tabbedPane.setSelectedIndex(aggiungiIndex);
            aggiungiTabPane.setSelectedComponent(aggiungiCalciatoreView);
        }
    }
    
    @Override
    public void showAggiungiMilitanzaView() {
        // Verifica che la tab Aggiungi sia presente
        int aggiungiIndex = tabbedPane.indexOfTab(aggiungiTabName);
        if (aggiungiIndex != -1) {
            tabbedPane.setSelectedIndex(aggiungiIndex);
            aggiungiTabPane.setSelectedComponent(aggiungiMilitanzaView);
        }
    }

    @Override
    public void showModificaCalciatoreView() {
        // Verifica che la tab Modifica sia presente
        int modificaIndex = tabbedPane.indexOfTab(modificaTabName);
        if (modificaIndex != -1) {
            tabbedPane.setSelectedIndex(modificaIndex);
            modificaTabPane.setSelectedComponent(modificaCalciatoreView);
        }
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
            
            // Aggiunge le tab di gestione se non sono già presenti
            if (tabbedPane.indexOfTab(aggiungiTabName) == -1) {
                tabbedPane.addTab(aggiungiTabName, aggiungiTabPane);
            }
            
            if (tabbedPane.indexOfTab(modificaTabName) == -1) {
                tabbedPane.addTab(modificaTabName, modificaTabPane);
                
                // Carica subito i calciatori nella vista di modifica
                try {
                    modificaCalciatoreView.displayCalciatori(controller.getCalciatori());
                } catch (Exception e) {
                    showError("Errore nel caricamento dei calciatori: " + e.getMessage());
                }
            }
        } else {
            // Ripristina la tab di login
            tabbedPane.setComponentAt(loginTabIndex, loginView);
            tabbedPane.setTitleAt(loginTabIndex, "Login");
            
            // Rimuove le tab di gestione se presenti
            if (tabbedPane.indexOfTab(aggiungiTabName) != -1) {
                tabbedPane.remove(tabbedPane.indexOfTab(aggiungiTabName));
            }
            
            if (tabbedPane.indexOfTab(modificaTabName) != -1) {
                tabbedPane.remove(tabbedPane.indexOfTab(modificaTabName));
            }
        }
    }

    @Override
    public void shutdown() {
        dispose();
        System.exit(0);
    }
}
