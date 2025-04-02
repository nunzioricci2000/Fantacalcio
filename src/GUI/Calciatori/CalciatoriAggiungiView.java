package GUI.Calciatori;

import Control.Controller;
import Model.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CalciatoriAggiungiView extends JPanel {
    private final Controller controller;
    
    // Form fields
    private JTextField nomeField;
    private JTextField cognomeField;
    private JSpinner dataNascitaSpinner;
    private JSpinner dataRitiroSpinner;
    private JCheckBox ritiratoCheckBox;
    private JComboBox<Piede> piedeComboBox;
    private JList<Ruolo> ruoliList;
    private JList<Skill> skillsList;
    
    public CalciatoriAggiungiView(Controller controller) {
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
        JLabel titleLabel = new JLabel("Aggiungi Nuovo Calciatore");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);
        
        // Basic Info Section
        addSectionTitle(formPanel, "Informazioni Base", 0, 1, gbc);
        
        // Nome
        addLabelAndComponent(formPanel, "Nome:", createNomeField(), 0, 2, gbc);
        
        // Cognome
        addLabelAndComponent(formPanel, "Cognome:", createCognomeField(), 0, 3, gbc);
        
        // Data di Nascita
        addLabelAndComponent(formPanel, "Data di Nascita:", createDataNascitaSpinner(), 0, 4, gbc);
        
        // Data di Ritiro
        addLabelAndComponent(formPanel, "Ritirato:", createRitiratoCheckBox(), 0, 5, gbc);
        addLabelAndComponent(formPanel, "Data di Ritiro:", createDataRitiroSpinner(), 0, 6, gbc);
        
        // Piede
        addLabelAndComponent(formPanel, "Piede:", createPiedeComboBox(), 0, 7, gbc);
        
        // Ruoli Section - migliorata per evidenziare la selezione multipla
        addSectionTitle(formPanel, "Ruoli", 0, 8, gbc);
        
        JPanel ruoliPanel = new JPanel(new BorderLayout());
        ruoliPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Seleziona uno o più ruoli (CTRL+click per selezione multipla)",
            TitledBorder.LEFT,
            TitledBorder.TOP
        ));
        
        ruoliList = createRuoliList();
        ruoliPanel.add(new JScrollPane(ruoliList), BorderLayout.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        formPanel.add(ruoliPanel, gbc);
        
        // Skills Section - migliorata per evidenziare la selezione multipla
        addSectionTitle(formPanel, "Skills", 0, 10, gbc);
        
        JPanel skillsPanel = new JPanel(new BorderLayout());
        skillsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Seleziona una o più skills (CTRL+click per selezione multipla)",
            TitledBorder.LEFT,
            TitledBorder.TOP
        ));
        
        skillsList = createSkillsList();
        skillsPanel.add(new JScrollPane(skillsList), BorderLayout.CENTER);
        
        // Aggiungi pulsanti per selezionare/deselezionare tutte le skills
        JPanel skillsButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton selectAllSkillsButton = new JButton("Seleziona Tutte");
        JButton deselectAllSkillsButton = new JButton("Deseleziona Tutte");
        
        selectAllSkillsButton.addActionListener(_ -> selectAllSkills());
        deselectAllSkillsButton.addActionListener(_ -> deselectAllSkills());
        
        skillsButtonsPanel.add(selectAllSkillsButton);
        skillsButtonsPanel.add(deselectAllSkillsButton);
        skillsPanel.add(skillsButtonsPanel, BorderLayout.SOUTH);
        
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        formPanel.add(skillsPanel, gbc);
        
        // Nota informativa
        JLabel notaLabel = new JLabel("Nota: Le militanze e i trofei potranno essere aggiunti successivamente");
        notaLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        formPanel.add(notaLabel, gbc);
        
        // Submit button
        JButton submitButton = new JButton("Salva Calciatore");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.addActionListener(_ -> salvaCalciatore());
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(submitButton, gbc);
        
        // ScrollPane for the entire form
        JScrollPane scrollPane = new JScrollPane(formPanel);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void addSectionTitle(JPanel panel, String title, int x, int y, GridBagConstraints gbc) {
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(sectionLabel, gbc);
        gbc.insets = new Insets(5, 5, 5, 5);
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
    
    private JTextField createNomeField() {
        nomeField = new JTextField(20);
        return nomeField;
    }
    
    private JTextField createCognomeField() {
        cognomeField = new JTextField(20);
        return cognomeField;
    }
    
    private JSpinner createDataNascitaSpinner() {
        // Set default to 20 years ago
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -20);
        Date defaultDate = cal.getTime();
        
        SpinnerDateModel model = new SpinnerDateModel(defaultDate, null, new Date(), Calendar.YEAR);
        dataNascitaSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dataNascitaSpinner, "dd/MM/yyyy");
        dataNascitaSpinner.setEditor(editor);
        return dataNascitaSpinner;
    }
    
    private JCheckBox createRitiratoCheckBox() {
        ritiratoCheckBox = new JCheckBox();
        ritiratoCheckBox.addActionListener(_ -> {
            dataRitiroSpinner.setEnabled(ritiratoCheckBox.isSelected());
        });
        return ritiratoCheckBox;
    }
    
    private JSpinner createDataRitiroSpinner() {
        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, new Date(), Calendar.YEAR);
        dataRitiroSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dataRitiroSpinner, "dd/MM/yyyy");
        dataRitiroSpinner.setEditor(editor);
        dataRitiroSpinner.setEnabled(false); // Inizialmente disabilitato
        return dataRitiroSpinner;
    }
    
    private JComboBox<Piede> createPiedeComboBox() {
        piedeComboBox = new JComboBox<>(Piede.values());
        return piedeComboBox;
    }
    
    private JList<Ruolo> createRuoliList() {
        ruoliList = new JList<>(Ruolo.values());
        ruoliList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ruoliList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        ruoliList.setVisibleRowCount(2);
        // Aggiungi un tooltip per spiegare la selezione multipla
        ruoliList.setToolTipText("Tieni premuto CTRL per selezionare più ruoli");
        return ruoliList;
    }
    
    private JList<Skill> createSkillsList() {
        skillsList = new JList<>(Skill.values());
        skillsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        skillsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        skillsList.setVisibleRowCount(5);
        // Aggiungi un tooltip per spiegare la selezione multipla
        skillsList.setToolTipText("Tieni premuto CTRL per selezionare più skills");
        return skillsList;
    }
    
    // Metodi per selezionare/deselezionare tutte le skills
    private void selectAllSkills() {
        int[] indices = new int[Skill.values().length];
        for (int i = 0; i < Skill.values().length; i++) {
            indices[i] = i;
        }
        skillsList.setSelectedIndices(indices);
    }
    
    private void deselectAllSkills() {
        skillsList.clearSelection();
    }
    
    private void salvaCalciatore() {
        try {
            // Validazione e raccolta dati
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            Date dataNascita = (Date) dataNascitaSpinner.getValue();
            Date dataRitiro = ritiratoCheckBox.isSelected() ? (Date) dataRitiroSpinner.getValue() : null;
            Piede piede = (Piede) piedeComboBox.getSelectedItem();
            List<Ruolo> ruoli = ruoliList.getSelectedValuesList();
            List<Skill> skills = skillsList.getSelectedValuesList();
            
            // Validazioni
            if (nome.isEmpty() || cognome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome e cognome sono obbligatori", "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (ruoli.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleziona almeno un ruolo", "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Invio dati al controller - senza militanze e trofei
            List<Militanza> militanzeVuote = new ArrayList<>(); // lista vuota per soddisfare la firma del metodo
            
            controller.inserisciCalciatore(
                nome, cognome, dataNascita, dataRitiro, 
                skills, ruoli, piede, militanzeVuote
            );
            
            // Mostra messaggio di successo
            JOptionPane.showMessageDialog(
                this,
                "Calciatore " + nome + " " + cognome + " aggiunto con successo!",
                "Operazione Completata",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Reset del form
            resetForm();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void resetForm() {
        nomeField.setText("");
        cognomeField.setText("");
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -20);
        dataNascitaSpinner.setValue(cal.getTime());
        
        ritiratoCheckBox.setSelected(false);
        dataRitiroSpinner.setValue(new Date());
        dataRitiroSpinner.setEnabled(false);
        
        piedeComboBox.setSelectedIndex(0);
        ruoliList.clearSelection();
        skillsList.clearSelection();
    }
}