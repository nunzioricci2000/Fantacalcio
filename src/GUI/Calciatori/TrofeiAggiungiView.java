package GUI.Calciatori;

import Control.Controller;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TrofeiAggiungiView extends JPanel {
    private final Controller controller;
    
    // Form fields
    private JComboBox<CalciatoreComboItem> calciatoreComboBox;
    private JTextField nomeField;
    private JSpinner dataSpinner;
    private JComboBox<Squadra> squadraComboBox;
    private JTabbedPane tipologiaPane;
    private JPanel individualePanel;
    private JPanel squadraPanel;
    private List<Squadra> squadre;
    
    public TrofeiAggiungiView(Controller controller) {
        this.controller = controller;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("Aggiungi Nuovo Trofeo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        formPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Panel for common fields
        JPanel commonFieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome trofeo
        addLabelAndComponent(commonFieldsPanel, "Nome trofeo:", createNomeField(), 0, 0, gbc);
        
        // Data
        addLabelAndComponent(commonFieldsPanel, "Data:", createDataSpinner(), 0, 1, gbc);
        
        formPanel.add(commonFieldsPanel, BorderLayout.CENTER);
        
        // Tabbed pane for trophy type
        tipologiaPane = new JTabbedPane();
        
        // Individual trophy panel
        individualePanel = new JPanel(new BorderLayout());
        JPanel individualeFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints indGbc = new GridBagConstraints();
        indGbc.insets = new Insets(5, 5, 5, 5);
        indGbc.anchor = GridBagConstraints.WEST;
        
        // Calciatore selection (moved to individual tab)
        addLabelAndComponent(individualeFormPanel, "Calciatore:", createCalciatoreComboBox(), 0, 0, indGbc);
        
        JLabel individualeInfoLabel = new JLabel("Il trofeo verrà assegnato al calciatore selezionato");
        individualeInfoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        indGbc.gridx = 0;
        indGbc.gridy = 1;
        indGbc.gridwidth = 2;
        individualeFormPanel.add(individualeInfoLabel, indGbc);
        
        individualePanel.add(individualeFormPanel, BorderLayout.CENTER);
        
        // Team trophy panel
        squadraPanel = new JPanel(new BorderLayout());
        JPanel squadraFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints squadraGbc = new GridBagConstraints();
        squadraGbc.insets = new Insets(5, 5, 5, 5);
        squadraGbc.anchor = GridBagConstraints.WEST;
        
        // Squadra selection
        addLabelAndComponent(squadraFormPanel, "Squadra:", createSquadraComboBox(), 0, 0, squadraGbc);
        
        JLabel squadraInfoLabel = new JLabel("Il trofeo verrà assegnato alla squadra selezionata");
        squadraInfoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        squadraGbc.gridx = 0;
        squadraGbc.gridy = 1;
        squadraGbc.gridwidth = 2;
        squadraFormPanel.add(squadraInfoLabel, squadraGbc);
        
        squadraPanel.add(squadraFormPanel, BorderLayout.CENTER);
        
        // Add panels to the tabbed pane
        tipologiaPane.addTab("Individuale", individualePanel);
        tipologiaPane.addTab("Di Squadra", squadraPanel);
        
        formPanel.add(tipologiaPane, BorderLayout.SOUTH);
        
        // Submit button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton submitButton = new JButton("Salva Trofeo");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.addActionListener(_ -> salvaTrofeo());
        buttonPanel.add(submitButton);
        
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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
    
    private JSpinner createDataSpinner() {
        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, new Date(), Calendar.YEAR);
        dataSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dataSpinner, "dd/MM/yyyy");
        dataSpinner.setEditor(editor);
        return dataSpinner;
    }
    
    private JComboBox<CalciatoreComboItem> createCalciatoreComboBox() {
        calciatoreComboBox = new JComboBox<>();
        calciatoreComboBox.setPreferredSize(new Dimension(300, 25));
        
        List<Calciatore> calciatori = controller.getCalciatori();
        for (Calciatore calciatore : calciatori) {
            calciatoreComboBox.addItem(new CalciatoreComboItem(calciatore));
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
    
    private void salvaTrofeo() {
        try {
            // Validazione e raccolta dati comuni
            String nome = nomeField.getText().trim();
            Date data = (Date) dataSpinner.getValue();
            
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Il nome del trofeo è obbligatorio", 
                    "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int selectedIndex = tipologiaPane.getSelectedIndex();
            
            Trofeo trofeo;
            int idCalciatore;
            
            // Crea il tipo corretto di trofeo a seconda della tab selezionata
            if (selectedIndex == 0) {
                // Trofeo Individuale
                if (calciatoreComboBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, 
                        "Seleziona un calciatore", 
                        "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                CalciatoreComboItem selectedCalciatore = (CalciatoreComboItem) calciatoreComboBox.getSelectedItem();
                idCalciatore = selectedCalciatore.getId();
                trofeo = new TrofeoIndividuale(nome, data, idCalciatore);
                
                controller.addTrofeo(trofeo);
            } else {
                // Trofeo di Squadra
                if (squadraComboBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, 
                        "Seleziona una squadra", 
                        "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Squadra selectedSquadra = (Squadra) squadraComboBox.getSelectedItem();
                trofeo = new TrofeoDiSquadra(nome, data, selectedSquadra);
                
                // Salva il trofeo di squadra
                controller.addTrofeo(trofeo);
            }
            
            // Mostra messaggio di successo
            JOptionPane.showMessageDialog(
                this,
                "Trofeo aggiunto con successo!",
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
        nomeField.setText("");
        dataSpinner.setValue(new Date());
        
        if (calciatoreComboBox.getItemCount() > 0) {
            calciatoreComboBox.setSelectedIndex(0);
        }
        
        if (squadraComboBox.getItemCount() > 0) {
            squadraComboBox.setSelectedIndex(0);
        }
        
        tipologiaPane.setSelectedIndex(0);
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