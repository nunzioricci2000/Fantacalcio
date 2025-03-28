package GUI;

import Control.Controller;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminView extends JPanel {
    private final Controller controller;
    private JTabbedPane adminTabs;
    
    private JTextField nuovoNomeField, nuovoCognomeField, nuovoDataNascitaField, nuovoDataRitiroField;
    private JComboBox<Piede> nuovoPiedeCombo;
    private JList<Ruolo> nuovoRuoliList;
    private JList<Skill> nuovoSkillsList;
    
    private JComboBox<Calciatore> calciatoriCombo;
    private JTextField modNomeField, modCognomeField, modDataNascitaField, modDataRitiroField;
    private JComboBox<Piede> modPiedeCombo;
    private JList<Ruolo> modRuoliList;
    private JList<Skill> modSkillsList;
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public AdminView(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        
        adminTabs = new JTabbedPane();
        
        // Crea le schede per le diverse operazioni amministrative (senza più la tab logout)
        JPanel inserisciPanel = createInserisciPanel();
        JPanel modificaPanel = createModificaPanel();
        JPanel eliminaPanel = createEliminaPanel();
        
        adminTabs.addTab("Inserisci Calciatore", inserisciPanel);
        adminTabs.addTab("Modifica Calciatore", modificaPanel);
        adminTabs.addTab("Elimina Calciatore", eliminaPanel);
        
        add(adminTabs, BorderLayout.CENTER);
    }
    
    private JPanel createInserisciPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campi per l'inserimento
        addLabelAndField(formPanel, gbc, 0, "Nome:", nuovoNomeField = new JTextField(20));
        addLabelAndField(formPanel, gbc, 1, "Cognome:", nuovoCognomeField = new JTextField(20));
        addLabelAndField(formPanel, gbc, 2, "Data di Nascita (dd/MM/yyyy):", nuovoDataNascitaField = new JTextField(20));
        addLabelAndField(formPanel, gbc, 3, "Data di Ritiro (opzionale):", nuovoDataRitiroField = new JTextField(20));
        
        // Piede
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Piede:"), gbc);
        
        nuovoPiedeCombo = new JComboBox<>(Piede.values());
        gbc.gridx = 1;
        formPanel.add(nuovoPiedeCombo, gbc);
        
        // Ruoli (JList con selezione multipla)
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Ruoli:"), gbc);
        
        nuovoRuoliList = new JList<>(Ruolo.values());
        nuovoRuoliList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane ruoliScroll = new JScrollPane(nuovoRuoliList);
        ruoliScroll.setPreferredSize(new Dimension(200, 100));
        gbc.gridx = 1;
        formPanel.add(ruoliScroll, gbc);
        
        // Skills (JList con selezione multipla)
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Skills:"), gbc);
        
        nuovoSkillsList = new JList<>(Skill.values());
        nuovoSkillsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane skillsScroll = new JScrollPane(nuovoSkillsList);
        skillsScroll.setPreferredSize(new Dimension(200, 150));
        gbc.gridx = 1;
        formPanel.add(skillsScroll, gbc);
        
        // Nota: gestione della carriera/militanze richiederebbe UI più complessa
        // Per semplicità, si assume che venga creato un calciatore senza carriera iniziale
        
        // Bottone per inserire
        JButton inserisciButton = new JButton("Inserisci Calciatore");
        inserisciButton.addActionListener(_ -> inserisciCalciatore());
        
        panel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        panel.add(inserisciButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createModificaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Selezione del calciatore da modificare
        JPanel selezionePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selezionePanel.add(new JLabel("Seleziona Calciatore:"));
        
        calciatoriCombo = new JComboBox<>();
        aggiornaListaCalciatori();
        
        calciatoriCombo.addActionListener(_ -> {
            Calciatore selected = (Calciatore) calciatoriCombo.getSelectedItem();
            if (selected != null) {
                popolaCampiModifica(selected);
            }
        });
        
        selezionePanel.add(calciatoriCombo);
        
        // Form per la modifica
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        addLabelAndField(formPanel, gbc, 0, "Nome:", modNomeField = new JTextField(20));
        addLabelAndField(formPanel, gbc, 1, "Cognome:", modCognomeField = new JTextField(20));
        addLabelAndField(formPanel, gbc, 2, "Data di Nascita (dd/MM/yyyy):", modDataNascitaField = new JTextField(20));
        addLabelAndField(formPanel, gbc, 3, "Data di Ritiro (opzionale):", modDataRitiroField = new JTextField(20));
        
        // Piede
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Piede:"), gbc);
        
        modPiedeCombo = new JComboBox<>(Piede.values());
        gbc.gridx = 1;
        formPanel.add(modPiedeCombo, gbc);
        
        // Ruoli
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Ruoli:"), gbc);
        
        modRuoliList = new JList<>(Ruolo.values());
        modRuoliList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane ruoliScroll = new JScrollPane(modRuoliList);
        ruoliScroll.setPreferredSize(new Dimension(200, 100));
        gbc.gridx = 1;
        formPanel.add(ruoliScroll, gbc);
        
        // Skills
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Skills:"), gbc);
        
        modSkillsList = new JList<>(Skill.values());
        modSkillsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane skillsScroll = new JScrollPane(modSkillsList);
        skillsScroll.setPreferredSize(new Dimension(200, 150));
        gbc.gridx = 1;
        formPanel.add(skillsScroll, gbc);
        
        // Bottone per modificare
        JButton modificaButton = new JButton("Salva Modifiche");
        modificaButton.addActionListener(_ -> modificaCalciatore());
        
        panel.add(selezionePanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        panel.add(modificaButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createEliminaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(new JLabel("Seleziona il calciatore da eliminare:"), gbc);
        
        JComboBox<Calciatore> eliminaCombo = new JComboBox<>();
        aggiornaListaCalciatori(eliminaCombo);
        
        gbc.gridy = 1;
        contentPanel.add(eliminaCombo, gbc);
        
        JButton eliminaButton = new JButton("Elimina Calciatore");
        eliminaButton.setBackground(new Color(220, 53, 69));
        eliminaButton.setForeground(Color.WHITE);
        
        eliminaButton.addActionListener(_ -> {
            Calciatore selected = (Calciatore) eliminaCombo.getSelectedItem();
            if (selected != null) {
                int option = JOptionPane.showConfirmDialog(
                        this, 
                        "Sei sicuro di voler eliminare il calciatore " + selected.getNome() + " " + selected.getCognome() + "?", 
                        "Conferma eliminazione", 
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                
                if (option == JOptionPane.YES_OPTION) {
                    controller.eliminaCalciatore(selected);
                    // Aggiorna la lista dopo l'eliminazione
                    aggiornaListaCalciatori(eliminaCombo);
                    aggiornaListaCalciatori(calciatoriCombo);
                }
            }
        });
        
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        contentPanel.add(eliminaButton, gbc);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);
        
        gbc.gridx = 1;
        panel.add(field, gbc);
    }
    
    private void inserisciCalciatore() {
        try {
            // Raccolta dei dati dal form
            String nome = nuovoNomeField.getText().trim();
            String cognome = nuovoCognomeField.getText().trim();
            
            // Validazione campi obbligatori
            if (nome.isEmpty() || cognome.isEmpty() || nuovoDataNascitaField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "I campi Nome, Cognome e Data di nascita sono obbligatori", 
                        "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validazione e parsing date
            Date dataNascita = dateFormat.parse(nuovoDataNascitaField.getText().trim());
            
            Date dataRitiro = null;
            if (!nuovoDataRitiroField.getText().trim().isEmpty()) {
                dataRitiro = dateFormat.parse(nuovoDataRitiroField.getText().trim());
            }
            
            // Ruoli selezionati
            List<Ruolo> ruoli = nuovoRuoliList.getSelectedValuesList();
            if (ruoli.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleziona almeno un ruolo", 
                        "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Skills selezionate
            List<Skill> skills = nuovoSkillsList.getSelectedValuesList();
            
            // Piede selezionato
            Piede piede = (Piede) nuovoPiedeCombo.getSelectedItem();
            
            // Per semplicità, creiamo un calciatore senza carriera
            List<Militanza> carriera = new ArrayList<>();
            
            // Chiamata al controller per inserire il calciatore
            controller.inserisciCalciatore(
                    nome, cognome, dataNascita, dataRitiro,
                    skills, ruoli, piede, carriera);
            
            // Reset dei campi del form dopo l'inserimento
            nuovoNomeField.setText("");
            nuovoCognomeField.setText("");
            nuovoDataNascitaField.setText("");
            nuovoDataRitiroField.setText("");
            nuovoPiedeCombo.setSelectedIndex(0);
            nuovoRuoliList.clearSelection();
            nuovoSkillsList.clearSelection();
            
            // Aggiorna la lista dei calciatori nel pannello di modifica
            aggiornaListaCalciatori();
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, 
                    "Formato data non valido. Usa il formato dd/MM/yyyy", 
                    "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Errore durante l'inserimento: " + e.getMessage(), 
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void modificaCalciatore() {
        Calciatore selectedCalciatore = (Calciatore) calciatoriCombo.getSelectedItem();
        if (selectedCalciatore == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un calciatore da modificare", 
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Raccolta dei dati dal form
            String nome = modNomeField.getText().trim();
            String cognome = modCognomeField.getText().trim();
            
            // Validazione campi obbligatori
            if (nome.isEmpty() || cognome.isEmpty() || modDataNascitaField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "I campi Nome, Cognome e Data di nascita sono obbligatori", 
                        "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validazione e parsing date
            Date dataNascita = dateFormat.parse(modDataNascitaField.getText().trim());
            
            Date dataRitiro = null;
            if (!modDataRitiroField.getText().trim().isEmpty()) {
                dataRitiro = dateFormat.parse(modDataRitiroField.getText().trim());
            }
            
            // Ruoli selezionati
            List<Ruolo> ruoli = modRuoliList.getSelectedValuesList();
            if (ruoli.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleziona almeno un ruolo", 
                        "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Skills selezionate
            List<Skill> skills = modSkillsList.getSelectedValuesList();
            
            // Piede selezionato
            Piede piede = (Piede) modPiedeCombo.getSelectedItem();
            
            // Manteniamo la carriera esistente (gestione completa richiederebbe UI dedicata)
            List<Militanza> carriera = selectedCalciatore.getCarriera();
            
            // Chiamata al controller per modificare il calciatore
            controller.modificaCalciatore(
                    selectedCalciatore, nome, cognome, dataNascita, dataRitiro,
                    skills, ruoli, piede, carriera);
            
            // Aggiorna la lista dei calciatori
            aggiornaListaCalciatori();
            
            JOptionPane.showMessageDialog(this, "Calciatore modificato con successo", 
                    "Successo", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, 
                    "Formato data non valido. Usa il formato dd/MM/yyyy", 
                    "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Errore durante la modifica: " + e.getMessage(), 
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void aggiornaListaCalciatori() {
        aggiornaListaCalciatori(calciatoriCombo);
    }
    
    private void aggiornaListaCalciatori(JComboBox<Calciatore> comboBox) {
        comboBox.removeAllItems();
        try {
            List<Calciatore> calciatori = controller.getCalciatori();
            for (Calciatore calciatore : calciatori) {
                comboBox.addItem(calciatore);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Errore durante il recupero dei calciatori: " + e.getMessage(), 
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void popolaCampiModifica(Calciatore calciatore) {
        modNomeField.setText(calciatore.getNome());
        modCognomeField.setText(calciatore.getCognome());
        modDataNascitaField.setText(dateFormat.format(calciatore.getDataDiNascita()));
        
        if (calciatore.getDataDiRitiro() != null) {
            modDataRitiroField.setText(dateFormat.format(calciatore.getDataDiRitiro()));
        } else {
            modDataRitiroField.setText("");
        }
        
        modPiedeCombo.setSelectedItem(calciatore.getPiede());
        
        // Seleziona i ruoli
        List<Ruolo> ruoli = calciatore.getRuoli();
        int[] ruoliIndices = new int[ruoli.size()];
        for (int i = 0; i < ruoli.size(); i++) {
            ruoliIndices[i] = ruoli.get(i).ordinal();
        }
        modRuoliList.setSelectedIndices(ruoliIndices);
        
        // Seleziona le skills
        List<Skill> skills = calciatore.getSkills();
        int[] skillsIndices = new int[skills.size()];
        for (int i = 0; i < skills.size(); i++) {
            skillsIndices[i] = skills.get(i).ordinal();
        }
        modSkillsList.setSelectedIndices(skillsIndices);
    }
}
