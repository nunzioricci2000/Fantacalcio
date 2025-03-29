package GUI.Calciatori;

import Control.Controller;
import Model.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CalciatoriModificaView extends JPanel {
    private final Controller controller;
    private CalciatoriTable calciatoriTable;
    private JPanel modificaPanel;
    private CalciatoriFiltriPanel filtriPanel;
    
    // Calciatore correntemente selezionato
    private Calciatore calciatoreSelezionato;
    
    public CalciatoriModificaView(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        
        // Inizializzazione dei componenti
        filtriPanel = new CalciatoriFiltriPanel(controller);
        calciatoriTable = new CalciatoriTable(controller);
        modificaPanel = new JPanel();
        modificaPanel.setBorder(BorderFactory.createTitledBorder("Seleziona un calciatore per modificarlo"));
        
        // Configurazione del layout
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(calciatoriTable),
                new JScrollPane(modificaPanel));
        splitPane.setResizeWeight(0.4);
        
        add(filtriPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        
        // Override del comportamento della tabella per mostrare il form di modifica
        calciatoriTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = calciatoriTable.getSelectedRow();
                if (selectedRow >= 0) {
                    try {
                        int id = ((CalciatoriTableModel)calciatoriTable.getModel()).getIdAtRow(selectedRow);
                        calciatoreSelezionato = controller.getCalciatore(id);
                        mostraTabModifica(calciatoreSelezionato);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, 
                            "Errore nel recupero dei dati del calciatore: " + ex.getMessage(), 
                            "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
    
    private void mostraTabModifica(Calciatore calciatore) {
        modificaPanel.removeAll();
        modificaPanel.setLayout(new BorderLayout());
        
        // Crea un JTabbedPane per le diverse categorie di modifica
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Tab per informazioni di base
        tabbedPane.addTab("Informazioni Base", creaInfoBasePanel(calciatore));
        
        // Tab per skills
        tabbedPane.addTab("Skills", creaSkillsPanel(calciatore));
        
        // Tab per ruoli
        tabbedPane.addTab("Ruoli", creaRuoliPanel(calciatore));
        
        // Tab per militanze
        tabbedPane.addTab("Militanze", creaMilitanzePanel(calciatore));
        
        // Aggiunge il TabbedPane al panel di modifica
        modificaPanel.add(tabbedPane, BorderLayout.CENTER);
        
        modificaPanel.revalidate();
        modificaPanel.repaint();
    }
    
    // PANNELLO INFORMAZIONI BASE
    private JPanel creaInfoBasePanel(Calciatore calciatore) {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Titolo
        JLabel titleLabel = new JLabel("Modifica informazioni di " + calciatore.getNome() + " " + calciatore.getCognome());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);
        
        // Nome
        JTextField nomeField = new JTextField(calciatore.getNome(), 20);
        addLabelAndComponent(formPanel, "Nome:", nomeField, 0, 2, gbc);
        
        // Cognome
        JTextField cognomeField = new JTextField(calciatore.getCognome(), 20);
        addLabelAndComponent(formPanel, "Cognome:", cognomeField, 0, 3, gbc);
        
        // Data di Nascita
        SpinnerDateModel nasModel = new SpinnerDateModel(calciatore.getDataDiNascita(), null, new Date(), Calendar.YEAR);
        JSpinner dataNascitaSpinner = new JSpinner(nasModel);
        JSpinner.DateEditor nasEditor = new JSpinner.DateEditor(dataNascitaSpinner, "dd/MM/yyyy");
        dataNascitaSpinner.setEditor(nasEditor);
        addLabelAndComponent(formPanel, "Data di Nascita:", dataNascitaSpinner, 0, 4, gbc);
        
        // Ritirato e Data di Ritiro
        JCheckBox ritiratoCheckBox = new JCheckBox();
        ritiratoCheckBox.setSelected(calciatore.getDataDiRitiro() != null);
        addLabelAndComponent(formPanel, "Ritirato:", ritiratoCheckBox, 0, 5, gbc);
        
        Date dataRitiro = calciatore.getDataDiRitiro() != null ? calciatore.getDataDiRitiro() : new Date();
        SpinnerDateModel ritModel = new SpinnerDateModel(dataRitiro, null, new Date(), Calendar.YEAR);
        JSpinner dataRitiroSpinner = new JSpinner(ritModel);
        JSpinner.DateEditor ritEditor = new JSpinner.DateEditor(dataRitiroSpinner, "dd/MM/yyyy");
        dataRitiroSpinner.setEditor(ritEditor);
        dataRitiroSpinner.setEnabled(calciatore.getDataDiRitiro() != null);
        ritiratoCheckBox.addActionListener(_ -> {
            dataRitiroSpinner.setEnabled(ritiratoCheckBox.isSelected());
        });
        addLabelAndComponent(formPanel, "Data di Ritiro:", dataRitiroSpinner, 0, 6, gbc);
        
        // Piede
        JComboBox<Piede> piedeComboBox = new JComboBox<>(Piede.values());
        piedeComboBox.setSelectedItem(calciatore.getPiede());
        addLabelAndComponent(formPanel, "Piede:", piedeComboBox, 0, 7, gbc);
        
        // Pulsanti di azione
        JPanel buttonPanel = new JPanel();
        JButton salvaButton = new JButton("Salva Modifiche");
        
        salvaButton.addActionListener(_ -> {
            try {
                // Raccolta dati
                String nome = nomeField.getText().trim();
                String cognome = cognomeField.getText().trim();
                Date dataNascita = (Date) dataNascitaSpinner.getValue();
                Date dataRitiroValue = ritiratoCheckBox.isSelected() ? (Date) dataRitiroSpinner.getValue() : null;
                Piede piede = (Piede) piedeComboBox.getSelectedItem();
                
                // Validazione
                if (nome.isEmpty() || cognome.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, 
                        "Nome e cognome sono obbligatori", 
                        "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Invio dati al controller per la modifica
                controller.modificaCalciatore(
                    calciatoreSelezionato,
                    nome,
                    cognome,
                    dataNascita,
                    dataRitiroValue,
                    calciatoreSelezionato.getSkills(),
                    calciatoreSelezionato.getRuoli(),   // Manteniamo ruoli invariati
                    piede,
                    calciatoreSelezionato.getCarriera() // Manteniamo la carriera invariata
                );
                
                // Aggiorna la visualizzazione con il calciatore modificato
                calciatoreSelezionato = controller.getCalciatore(calciatoreSelezionato.getId());
                mostraTabModifica(calciatoreSelezionato);
                
                // Aggiorna la tabella
                displayCalciatori(controller.getCalciatori());
                
                // Mostra messaggio di successo
                JOptionPane.showMessageDialog(panel,
                    "Informazioni di base aggiornate con successo!",
                    "Operazione Completata",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                    "Errore durante la modifica: " + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(salvaButton);
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(buttonPanel, gbc);
        
        panel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        return panel;
    }
    
    // PANNELLO SKILLS
    private JPanel creaSkillsPanel(Calciatore calciatore) {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel skillsPanel = new JPanel(new BorderLayout());
        skillsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Seleziona le skills del calciatore",
            TitledBorder.LEFT,
            TitledBorder.TOP
        ));
        
        JList<Skill> skillsList = new JList<>(Skill.values());
        skillsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        skillsList.setToolTipText("Tieni premuto CTRL per selezionare più skills");
        
        // Preseleziona le skills del calciatore
        List<Integer> skillsIndices = new ArrayList<>();
        for (int i = 0; i < Skill.values().length; i++) {
            if (calciatore.getSkills().contains(Skill.values()[i])) {
                skillsIndices.add(i);
            }
        }
        int[] selectedSkillsIndices = skillsIndices.stream().mapToInt(i -> i).toArray();
        skillsList.setSelectedIndices(selectedSkillsIndices);
        
        JPanel skillsButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton selectAllSkillsButton = new JButton("Seleziona Tutte");
        JButton deselectAllSkillsButton = new JButton("Deseleziona Tutte");
        
        selectAllSkillsButton.addActionListener(e -> {
            int[] indices = new int[Skill.values().length];
            for (int i = 0; i < Skill.values().length; i++) {
                indices[i] = i;
            }
            skillsList.setSelectedIndices(indices);
        });
        
        deselectAllSkillsButton.addActionListener(e -> {
            skillsList.clearSelection();
        });
        
        skillsButtonsPanel.add(selectAllSkillsButton);
        skillsButtonsPanel.add(deselectAllSkillsButton);
        
        skillsPanel.add(new JScrollPane(skillsList), BorderLayout.CENTER);
        skillsPanel.add(skillsButtonsPanel, BorderLayout.SOUTH);
        
        // Pulsanti di azione
        JPanel buttonPanel = new JPanel();
        JButton salvaButton = new JButton("Salva Skills");
        
        salvaButton.addActionListener(e -> {
            try {
                List<Skill> selectedSkills = skillsList.getSelectedValuesList();
                
                // Usa il metodo specifico aggiornaSkills invece di modificaCalciatore
                controller.aggiornaSkills(calciatoreSelezionato.getId(), selectedSkills);
                
                // Aggiorna la visualizzazione con il calciatore modificato
                calciatoreSelezionato = controller.getCalciatore(calciatoreSelezionato.getId());
                mostraTabModifica(calciatoreSelezionato);
                
                // Mostra messaggio di successo
                JOptionPane.showMessageDialog(panel,
                    "Skills aggiornate con successo!",
                    "Operazione Completata",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                    "Errore durante la modifica delle skills: " + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(salvaButton);
        
        panel.add(skillsPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // PANNELLO RUOLI
    private JPanel creaRuoliPanel(Calciatore calciatore) {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel ruoliPanel = new JPanel(new BorderLayout());
        ruoliPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Seleziona i ruoli del calciatore",
            TitledBorder.LEFT,
            TitledBorder.TOP
        ));
        
        JList<Ruolo> ruoliList = new JList<>(Ruolo.values());
        ruoliList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ruoliList.setToolTipText("Tieni premuto CTRL per selezionare più ruoli");
        
        // Preseleziona i ruoli del calciatore
        List<Integer> ruoliIndices = new ArrayList<>();
        for (int i = 0; i < Ruolo.values().length; i++) {
            if (calciatore.getRuoli().contains(Ruolo.values()[i])) {
                ruoliIndices.add(i);
            }
        }
        int[] selectedRuoliIndices = ruoliIndices.stream().mapToInt(i -> i).toArray();
        ruoliList.setSelectedIndices(selectedRuoliIndices);
        
        ruoliPanel.add(new JScrollPane(ruoliList), BorderLayout.CENTER);
        
        // Pulsanti di azione
        JPanel buttonPanel = new JPanel();
        JButton salvaButton = new JButton("Salva Ruoli");
        
        salvaButton.addActionListener(e -> {
            try {
                List<Ruolo> selectedRuoli = ruoliList.getSelectedValuesList();
                
                // Validazione
                if (selectedRuoli.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, 
                        "Seleziona almeno un ruolo", 
                        "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Usa il metodo specifico aggiornaRuoli invece di modificaCalciatore
                controller.aggiornaRuoli(calciatoreSelezionato.getId(), selectedRuoli);
                
                // Aggiorna la visualizzazione con il calciatore modificato
                calciatoreSelezionato = controller.getCalciatore(calciatoreSelezionato.getId());
                mostraTabModifica(calciatoreSelezionato);
                
                // Mostra messaggio di successo
                JOptionPane.showMessageDialog(panel,
                    "Ruoli aggiornati con successo!",
                    "Operazione Completata",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                    "Errore durante la modifica dei ruoli: " + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(salvaButton);
        
        panel.add(ruoliPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // PANNELLO MILITANZE
    private JPanel creaMilitanzePanel(Calciatore calciatore) {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Titolo
        JLabel titleLabel = new JLabel("Gestione Militanze");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Tabella delle militanze attuali
        String[] colonne = {"Squadra", "Nazione", "Partite", "Goal", "Goal Subiti", "Periodo"};
        Object[][] dati = new Object[calciatore.getCarriera().size()][6];
        
        int i = 0;
        for (Militanza militanza : calciatore.getCarriera()) {
            dati[i][0] = militanza.getSquadra().nome();
            dati[i][1] = militanza.getSquadra().nazionalita();
            dati[i][2] = militanza.getPartiteGiocate();
            dati[i][3] = militanza.getGoalSegnati();
            
            if (militanza instanceof MilitanzaPortiere) {
                dati[i][4] = ((MilitanzaPortiere) militanza).getGoalSubiti();
            } else {
                dati[i][4] = "N/A";
            }
            
            // Format periodi
            StringBuilder periodi = new StringBuilder();
            for (Periodo periodo : militanza.getPeriodi()) {
                if (periodi.length() > 0) periodi.append(", ");
                periodi.append(formatDate(periodo.dataInizio())).append(" - ").append(formatDate(periodo.dataFine()));
            }
            dati[i][5] = periodi.toString();
            
            i++;
        }
        
        JTable militanzeTable = new JTable(dati, colonne);
        militanzeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(militanzeTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Nota informativa
        JLabel notaLabel = new JLabel("Nota: Per aggiungere nuove militanze, utilizza la scheda 'Aggiungi Militanza'");
        notaLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        panel.add(notaLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private String formatDate(Date date) {
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(date);
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
    
    public void displayCalciatori(List<Calciatore> calciatori) {
        calciatoriTable.displayCalciatori(calciatori);
    }
}