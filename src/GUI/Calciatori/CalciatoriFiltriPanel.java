package GUI.Calciatori;

import java.util.List;

import javax.swing.*;

import Control.Controller;

import java.awt.*;

import Model.Filtro;
import Model.Piede;
import Model.Squadra;

public class CalciatoriFiltriPanel extends JPanel {
    private Controller controller;

    private JTextField nomeField;
    private JComboBox<String> ruoloCombo;
    private JComboBox<Piede> piedeCombo;
    private JSpinner minGoalSegnatiSpinner;
    private JSpinner maxGoalSegnatiSpinner;
    private JSpinner minEtaSpinner;
    private JSpinner maxEtaSpinner;
    private JComboBox<Squadra> squadraCombo;

    public CalciatoriFiltriPanel(Controller controller) {
        super();
        this.controller = controller;
        init();
    }

    private void init() {
        this.setBorder(BorderFactory.createTitledBorder("Filtri"));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        addNomeCognomeFilter(gbc);
        addRuoloFilter(gbc);
        addPiedeFilter(gbc);
        addGoalFilter(gbc);
        addEtaFilter(gbc);
        addSquadraFilter(gbc);
        addActionButtons(gbc);
    }

    private void addToFilterPanel(GridBagConstraints gbc, Component component, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        this.add(component, gbc);
    }

    private void addFieldToFilerPanel(GridBagConstraints gbc, String label, Component component, int x, int y) {
        addToFilterPanel(gbc, new JLabel(label), x, y);
        addToFilterPanel(gbc, component, x + 1, y);
    }
    
    private void addNomeCognomeFilter(GridBagConstraints gbc) {
        nomeField = new JTextField(15);
        addFieldToFilerPanel(gbc, "Nome/Cognome:", nomeField, 0, 0);
    }
    
    private void addRuoloFilter(GridBagConstraints gbc) {
        String[] ruoli = {"", "PORTIERE", "DIFENSORE", "CENTROCAMPISTA", "ATTACCANTE"};
        ruoloCombo = new JComboBox<>(ruoli);
        addFieldToFilerPanel(gbc, "Ruolo:", ruoloCombo, 2, 0);
    }
    
    private void addPiedeFilter(GridBagConstraints gbc) {
        piedeCombo = new JComboBox<>(Piede.values());
        piedeCombo.insertItemAt(null, 0);
        piedeCombo.setSelectedIndex(0);
        addFieldToFilerPanel(gbc, "Piede:", piedeCombo, 4, 0);
    }
    
    private void addGoalFilter(GridBagConstraints gbc) {
        minGoalSegnatiSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        addFieldToFilerPanel(gbc, "Min Goal:", minGoalSegnatiSpinner, 0, 1);
        maxGoalSegnatiSpinner = new JSpinner(new SpinnerNumberModel(1000, 0, 1000, 1));
        addFieldToFilerPanel(gbc, "Max Goal:", maxGoalSegnatiSpinner, 2, 1);
    }
    
    private void addEtaFilter(GridBagConstraints gbc) {
        minEtaSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        addFieldToFilerPanel(gbc, "Min Età:", minEtaSpinner, 2, 2);
        maxEtaSpinner = new JSpinner(new SpinnerNumberModel(100, 0, 100, 1));
        addFieldToFilerPanel(gbc, "Max Età:", maxEtaSpinner, 0, 2);
    }
    
    private void addSquadraFilter(GridBagConstraints gbc) {
        squadraCombo = new JComboBox<>();
        try {
            List<Squadra> squadre = controller.getSquadre();
            squadraCombo.addItem(null);
            for (Squadra squadra : squadre) {
                squadraCombo.addItem(squadra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        addFieldToFilerPanel(gbc, "Squadra:", squadraCombo, 4, 1);
    }
    
    private void addActionButtons(GridBagConstraints gbc) {
        JButton cercaButton = new JButton("Cerca");
        JButton resetButton = new JButton("Reset");
        cercaButton.addActionListener(_ -> applicaFiltri());
        resetButton.addActionListener(_ -> resetFiltri());
        addToFilterPanel(gbc, cercaButton, 5, 2);
        addToFilterPanel(gbc, resetButton, 4, 2);
    }

    private void applicaFiltri() {
        Filtro.Builder builder = new Filtro.Builder();
        if (!nomeField.getText().isEmpty()) {
            builder.buildNome(nomeField.getText());
        }
        if (ruoloCombo.getSelectedIndex() > 0) {
            builder.buildRuolo((String) ruoloCombo.getSelectedItem());
        }
        if (piedeCombo.getSelectedIndex() > 0) {
            builder.buildPiede((Piede) piedeCombo.getSelectedItem());
        }
        builder.buildMinimoGoalSegnati((Integer) minGoalSegnatiSpinner.getValue());
        builder.buildMassimoGoalSegnati((Integer) maxGoalSegnatiSpinner.getValue());
        builder.buildMinimoEta((Integer) minEtaSpinner.getValue());
        builder.buildMassimoEta((Integer) maxEtaSpinner.getValue());
        if (squadraCombo.getSelectedIndex() > 0) {
            builder.buildSquadra((Squadra) squadraCombo.getSelectedItem());
        }
        Filtro filtro = builder.getResult();
        controller.filtraPer(filtro);
    }
    
    private void resetFiltri() {
        nomeField.setText("");
        ruoloCombo.setSelectedIndex(0);
        piedeCombo.setSelectedIndex(0);
        minGoalSegnatiSpinner.setValue(0);
        maxGoalSegnatiSpinner.setValue(1000);
        minEtaSpinner.setValue(0);
        maxEtaSpinner.setValue(100);
        squadraCombo.setSelectedIndex(0);
        
        controller.vediElencoCalciatori();
    }
}
