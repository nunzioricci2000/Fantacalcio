package GUI.Calciatori;

import Control.Controller;
import Model.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class MilitanzeAggiungiView extends JPanel {
    private final Controller controller;
    
    // Form fields
    private JComboBox<CalciatoreComboItem> calciatoreComboBox;
    private JComboBox<Squadra> squadraComboBox;
    private JSpinner partiteGiocateSpinner;
    private JSpinner goalSegnatiSpinner;
    private JCheckBox portiereMilitanzaCheckBox;
    private JSpinner goalSubitiSpinner;
    private JSpinner dataInizioSpinner;
    private JSpinner dataFineSpinner;
    private List<Squadra> squadre;
    
    public MilitanzeAggiungiView(Controller controller) {
        this.controller = controller;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title
        JLabel titleLabel = new JLabel("Aggiungi Nuova Militanza");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);
        
        // Calciatore selection
        addLabelAndComponent(formPanel, "Calciatore:", createCalciatoreComboBox(), 0, 1, gbc);
        
        // Squadra selection
        addLabelAndComponent(formPanel, "Squadra:", createSquadraComboBox(), 0, 2, gbc);
        
        // Partite giocate
        addLabelAndComponent(formPanel, "Partite giocate:", createPartiteGiocateSpinner(), 0, 3, gbc);
        
        // Goal segnati
        addLabelAndComponent(formPanel, "Goal segnati:", createGoalSegnatiSpinner(), 0, 4, gbc);
        
        // È portiere
        addLabelAndComponent(formPanel, "È portiere:", createPortiereCheckBox(), 0, 5, gbc);
        
        // Goal subiti (solo per portieri)
        addLabelAndComponent(formPanel, "Goal subiti:", createGoalSubitiSpinner(), 0, 6, gbc);
        
        // Periodo Section
        JPanel periodoPanel = new JPanel(new BorderLayout());
        periodoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Periodo di militanza",
            TitledBorder.LEFT,
            TitledBorder.TOP
        ));
        
        JPanel periodoFieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints periodoGbc = new GridBagConstraints();
        periodoGbc.insets = new Insets(5, 5, 5, 5);
        periodoGbc.anchor = GridBagConstraints.WEST;
        
        // Data inizio
        periodoGbc.gridx = 0;
        periodoGbc.gridy = 0;
        periodoFieldsPanel.add(new JLabel("Data inizio:"), periodoGbc);
        periodoGbc.gridx = 1;
        periodoFieldsPanel.add(createDataInizioSpinner(), periodoGbc);
        
        // Data fine
        periodoGbc.gridx = 0;
        periodoGbc.gridy = 1;
        periodoFieldsPanel.add(new JLabel("Data fine:"), periodoGbc);
        periodoGbc.gridx = 1;
        periodoFieldsPanel.add(createDataFineSpinner(), periodoGbc);
        
        periodoPanel.add(periodoFieldsPanel, BorderLayout.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        formPanel.add(periodoPanel, gbc);
        
        // Submit button
        JButton submitButton = new JButton("Salva Militanza");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.addActionListener(_ -> salvaMilitanza());
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(submitButton, gbc);
        
        // ScrollPane for the entire form
        JScrollPane scrollPane = new JScrollPane(formPanel);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void addLabelAndComponent(JPanel panel, String labelText, JComponent component, 
                                     int x, int y, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        panel.add(label, gbc);
        
        gbc.gridx = x + 1;
        panel.add(component, gbc);
    }
    
    private JComboBox<CalciatoreComboItem> createCalciatoreComboBox() {
        calciatoreComboBox = new JComboBox<>();
        calciatoreComboBox.setPreferredSize(new Dimension(300, 25));
        
        try {
            List<Calciatore> calciatori = controller.getCalciatori();
            for (Calciatore calciatore : calciatori) {
                calciatoreComboBox.addItem(new CalciatoreComboItem(calciatore));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Errore nel caricamento dei calciatori: " + e.getMessage(), 
                "Errore", JOptionPane.ERROR_MESSAGE);
        }
        
        return calciatoreComboBox;
    }
    
    private JComboBox<Squadra> createSquadraComboBox() {
        squadraComboBox = new JComboBox<>();
        squadraComboBox.setPreferredSize(new Dimension(300, 25));
        
        try {
            squadre = controller.getSquadre();
            for (Squadra squadra : squadre) {
                squadraComboBox.addItem(squadra);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Errore nel caricamento delle squadre: " + e.getMessage(), 
                "Errore", JOptionPane.ERROR_MESSAGE);
        }
        
        return squadraComboBox;
    }
    
    private JSpinner createPartiteGiocateSpinner() {
        partiteGiocateSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        return partiteGiocateSpinner;
    }
    
    private JSpinner createGoalSegnatiSpinner() {
        goalSegnatiSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        return goalSegnatiSpinner;
    }
    
    private JCheckBox createPortiereCheckBox() {
        portiereMilitanzaCheckBox = new JCheckBox();
        portiereMilitanzaCheckBox.addActionListener(_ -> {
            goalSubitiSpinner.setEnabled(portiereMilitanzaCheckBox.isSelected());
        });
        return portiereMilitanzaCheckBox;
    }
    
    private JSpinner createGoalSubitiSpinner() {
        goalSubitiSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        goalSubitiSpinner.setEnabled(false);
        return goalSubitiSpinner;
    }
    
    private JSpinner createDataInizioSpinner() {
        // Default to 5 years ago
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -5);
        Date defaultStartDate = cal.getTime();
        
        SpinnerDateModel startModel = new SpinnerDateModel(defaultStartDate, null, new Date(), Calendar.YEAR);
        dataInizioSpinner = new JSpinner(startModel);
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(dataInizioSpinner, "dd/MM/yyyy");
        dataInizioSpinner.setEditor(startEditor);
        return dataInizioSpinner;
    }
    
    private JSpinner createDataFineSpinner() {
        SpinnerDateModel endModel = new SpinnerDateModel(new Date(), null, null, Calendar.YEAR);
        dataFineSpinner = new JSpinner(endModel);
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(dataFineSpinner, "dd/MM/yyyy");
        dataFineSpinner.setEditor(endEditor);
        return dataFineSpinner;
    }
    
    private void salvaMilitanza() {
        try {
            // Validazione e raccolta dati
            if (calciatoreComboBox.getSelectedItem() == null || squadraComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, 
                    "Seleziona un calciatore e una squadra", 
                    "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            CalciatoreComboItem selectedCalciatore = (CalciatoreComboItem) calciatoreComboBox.getSelectedItem();
            Squadra selectedSquadra = (Squadra) squadraComboBox.getSelectedItem();
            int partiteGiocate = (Integer) partiteGiocateSpinner.getValue();
            int goalSegnati = (Integer) goalSegnatiSpinner.getValue();
            boolean isPortiere = portiereMilitanzaCheckBox.isSelected();
            int goalSubiti = (Integer) goalSubitiSpinner.getValue();
            Date dataInizio = (Date) dataInizioSpinner.getValue();
            Date dataFine = (Date) dataFineSpinner.getValue();
            
            // Validazioni
            if (dataInizio.after(dataFine)) {
                JOptionPane.showMessageDialog(this, 
                    "La data di inizio deve essere precedente alla data di fine", 
                    "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Creare l'oggetto Periodo
            List<Periodo> periodi = new ArrayList<>();
            periodi.add(new Periodo(dataInizio, dataFine));
            
            // Creare l'oggetto Militanza con il periodo
            Militanza militanza;
            if (isPortiere) {
                militanza = new MilitanzaPortiere(selectedSquadra, partiteGiocate, goalSegnati, goalSubiti, periodi);
            } else {
                militanza = new Militanza(selectedSquadra, partiteGiocate, goalSegnati, periodi);
            }
            
            // Usa il metodo addMilitanza del controller
            controller.addMilitanza(selectedCalciatore.getId(), militanza);
            
            // Mostra messaggio di successo
            JOptionPane.showMessageDialog(
                this,
                "Militanza aggiunta con successo!",
                "Operazione Completata",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Reset del form
            resetForm();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Errore: " + e.getMessage(), 
                "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void resetForm() {
        if (calciatoreComboBox.getItemCount() > 0) {
            calciatoreComboBox.setSelectedIndex(0);
        }
        if (squadraComboBox.getItemCount() > 0) {
            squadraComboBox.setSelectedIndex(0);
        }
        partiteGiocateSpinner.setValue(0);
        goalSegnatiSpinner.setValue(0);
        portiereMilitanzaCheckBox.setSelected(false);
        goalSubitiSpinner.setValue(0);
        goalSubitiSpinner.setEnabled(false);
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -5);
        dataInizioSpinner.setValue(cal.getTime());
        dataFineSpinner.setValue(new Date());
    }
    
    private static class CalciatoreComboItem {
        private final Calciatore calciatore;
        
        public CalciatoreComboItem(Calciatore calciatore) {
            this.calciatore = calciatore;
        }
        
        public int getId() {
            return calciatore.getId();
        }
        
        @Override
        public String toString() {
            return calciatore.getNome() + " " + calciatore.getCognome();
        }
    }
}