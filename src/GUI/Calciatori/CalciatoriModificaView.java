package GUI.Calciatori;

import Control.Controller;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CalciatoriModificaView extends JPanel {
    private final Controller controller;
    private CalciatoriTable calciatoriTable;
    private JPanel modificaPanel;
    private CalciatoriFiltriPanel filtriPanel;
    
    // Form components per la modifica
    private JTextField nomeField;
    private JTextField cognomeField;
    private JSpinner dataNascitaSpinner;
    private JSpinner dataRitiroSpinner;
    private JCheckBox ritiratoCheckBox;
    private JComboBox<Piede> piedeComboBox;
    
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
                        mostraFormModifica(calciatoreSelezionato);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, 
                            "Errore nel recupero dei dati del calciatore: " + ex.getMessage(), 
                            "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
    
    private void mostraFormModifica(Calciatore calciatore) {
        modificaPanel.removeAll();
        modificaPanel.setLayout(new BorderLayout());
        
        // Panel principale del form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Titolo
        JLabel titleLabel = new JLabel("Modifica " + calciatore.getNome() + " " + calciatore.getCognome());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);
        
        // Informazioni di base
        addSectionTitle(formPanel, "Informazioni Base", 0, 1, gbc);
        
        // Nome
        nomeField = new JTextField(calciatore.getNome(), 20);
        addLabelAndComponent(formPanel, "Nome:", nomeField, 0, 2, gbc);
        
        // Cognome
        cognomeField = new JTextField(calciatore.getCognome(), 20);
        addLabelAndComponent(formPanel, "Cognome:", cognomeField, 0, 3, gbc);
        
        // Data di Nascita
        SpinnerDateModel nasModel = new SpinnerDateModel(calciatore.getDataDiNascita(), null, new Date(), Calendar.YEAR);
        dataNascitaSpinner = new JSpinner(nasModel);
        JSpinner.DateEditor nasEditor = new JSpinner.DateEditor(dataNascitaSpinner, "dd/MM/yyyy");
        dataNascitaSpinner.setEditor(nasEditor);
        addLabelAndComponent(formPanel, "Data di Nascita:", dataNascitaSpinner, 0, 4, gbc);
        
        // Ritirato e Data di Ritiro
        ritiratoCheckBox = new JCheckBox();
        ritiratoCheckBox.setSelected(calciatore.getDataDiRitiro() != null);
        addLabelAndComponent(formPanel, "Ritirato:", ritiratoCheckBox, 0, 5, gbc);
        
        Date dataRitiro = calciatore.getDataDiRitiro() != null ? calciatore.getDataDiRitiro() : new Date();
        SpinnerDateModel ritModel = new SpinnerDateModel(dataRitiro, null, new Date(), Calendar.YEAR);
        dataRitiroSpinner = new JSpinner(ritModel);
        JSpinner.DateEditor ritEditor = new JSpinner.DateEditor(dataRitiroSpinner, "dd/MM/yyyy");
        dataRitiroSpinner.setEditor(ritEditor);
        dataRitiroSpinner.setEnabled(calciatore.getDataDiRitiro() != null);
        ritiratoCheckBox.addActionListener(_ -> {
            dataRitiroSpinner.setEnabled(ritiratoCheckBox.isSelected());
        });
        addLabelAndComponent(formPanel, "Data di Ritiro:", dataRitiroSpinner, 0, 6, gbc);
        
        // Piede
        piedeComboBox = new JComboBox<>(Piede.values());
        piedeComboBox.setSelectedItem(calciatore.getPiede());
        addLabelAndComponent(formPanel, "Piede:", piedeComboBox, 0, 7, gbc);
        
        // Nota informativa su ruoli, skills, militanze e trofei
        JLabel notaLabel = new JLabel("Nota: Ruoli, skills, militanze e trofei vanno modificati nelle sezioni dedicate");
        notaLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        formPanel.add(notaLabel, gbc);
        
        // Pulsanti di azione
        JPanel buttonPanel = new JPanel();
        JButton salvaButton = new JButton("Salva Modifiche");
        JButton annullaButton = new JButton("Annulla");
        
        salvaButton.addActionListener(_ -> salvaModifiche());
        annullaButton.addActionListener(_ -> mostraFormModifica(calciatoreSelezionato));
        
        buttonPanel.add(salvaButton);
        buttonPanel.add(annullaButton);
        
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(buttonPanel, gbc);
        
        // Aggiunge il form al panel di modifica
        modificaPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        modificaPanel.revalidate();
        modificaPanel.repaint();
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
    
    private void salvaModifiche() {
        if (calciatoreSelezionato == null) {
            return;
        }
        
        try {
            // Raccolta dati
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            Date dataNascita = (Date) dataNascitaSpinner.getValue();
            Date dataRitiro = ritiratoCheckBox.isSelected() ? (Date) dataRitiroSpinner.getValue() : null;
            Piede piede = (Piede) piedeComboBox.getSelectedItem();
            
            // Validazione
            if (nome.isEmpty() || cognome.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Nome e cognome sono obbligatori", 
                    "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Invio dati al controller per la modifica
            // Nota: manteniamo ruoli, skills e carriera invariati
            controller.modificaCalciatore(
                calciatoreSelezionato,
                nome,
                cognome,
                dataNascita,
                dataRitiro,
                calciatoreSelezionato.getSkills(),  // Manteniamo skills invariate
                calciatoreSelezionato.getRuoli(),   // Manteniamo ruoli invariati
                piede,
                calciatoreSelezionato.getCarriera() // Manteniamo la carriera invariata
            );
            
            // Aggiorna la visualizzazione con il calciatore modificato
            calciatoreSelezionato = controller.getCalciatore(calciatoreSelezionato.getId());
            mostraFormModifica(calciatoreSelezionato);
            
            // Aggiorna la tabella
            displayCalciatori(controller.getCalciatori());
            
            // Mostra messaggio di successo
            JOptionPane.showMessageDialog(this,
                "Calciatore modificato con successo!",
                "Operazione Completata",
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Errore durante la modifica: " + e.getMessage(),
                "Errore",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void displayCalciatori(List<Calciatore> calciatori) {
        calciatoriTable.displayCalciatori(calciatori);
    }
}