package GUI.Calciatori;

import Control.Controller;
import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CalciatoriDettaglioPanel extends JPanel {
    
    public CalciatoriDettaglioPanel(Controller controller) {
        setBorder(BorderFactory.createTitledBorder("Dettaglio Calciatore"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Seleziona un calciatore per vedere i dettagli"));
    }
    
    public void displayDettaglioCalciatore(Calciatore calciatore) {
        removeAll();
        setLayout(new BorderLayout());
        JPanel infoPanel = createInfoPanel();
        GridBagConstraints gbc = createGbc();
        addBasicInfo(infoPanel, gbc, calciatore);
        addRuoli(infoPanel, gbc, calciatore);
        addSkills(infoPanel, gbc, calciatore);
        JPanel carrieraPanel = createCarrieraPanel(calciatore);
        JPanel trofeiPanel = createTrofeiPanel(calciatore);
        // Aggiunta dei pannelli al dettaglio
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        
        JPanel tablesPanel = new JPanel(new GridLayout(2, 1));
        tablesPanel.add(carrieraPanel);
        tablesPanel.add(trofeiPanel);
        
        mainPanel.add(tablesPanel, BorderLayout.CENTER);
        
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        return infoPanel;
    }

    private GridBagConstraints createGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private void addRuoli(JPanel infoPanel, GridBagConstraints gbc, Calciatore calciatore) {
        StringBuilder ruoli = new StringBuilder();
        for (Ruolo ruolo : calciatore.getRuoli()) {
            if (ruoli.length() > 0) ruoli.append(", ");
            ruoli.append(ruolo.name());
        }
        addLabelValuePair(infoPanel, gbc, 5, "Ruoli:", ruoli.toString());
    }

    private void addSkills(JPanel infoPanel, GridBagConstraints gbc, Calciatore calciatore) {
        StringBuilder skills = new StringBuilder();
        for (Skill skill : calciatore.getSkills()) {
            if (skills.length() > 0) skills.append(", ");
            skills.append(skill.name());
        }
        addLabelValuePair(infoPanel, gbc, 6, "Skills:", skills.toString());
    }
    
    private JPanel createCarrieraPanel(Calciatore calciatore) {
        JPanel carrieraPanel = new JPanel();
        carrieraPanel.setBorder(BorderFactory.createTitledBorder("Carriera"));
        carrieraPanel.setLayout(new BorderLayout());
        
        String[] carrieraColumns = {"Squadra", "Nazione", "Partite", "Goal"};
        DefaultTableModel carrieraModel = new DefaultTableModel(carrieraColumns, 0);
        
        for (Militanza militanza : calciatore.getCarriera()) {
            Object[] row = new Object[4];
            row[0] = militanza.getSquadra().nome();
            row[1] = militanza.getSquadra().nazionalita();
            row[2] = militanza.getPartiteGiocate();
            row[3] = militanza.getGoalSegnati();
            carrieraModel.addRow(row);
        }
        
        JTable carrieraTable = new JTable(carrieraModel);
        carrieraPanel.add(new JScrollPane(carrieraTable), BorderLayout.CENTER);
        return carrieraPanel;
    }

    private JPanel createTrofeiPanel(Calciatore calciatore) {
        JPanel trofeiPanel = new JPanel();
        trofeiPanel.setBorder(BorderFactory.createTitledBorder("Trofei"));
        trofeiPanel.setLayout(new BorderLayout());
        
        String[] trofeiColumns = {"Nome", "Data", "Tipo"};
        DefaultTableModel trofeiModel = new DefaultTableModel(trofeiColumns, 0);
        
        for (Trofeo trofeo : calciatore.getTrofei()) {
            Object[] row = new Object[3];
            row[0] = trofeo.getNome();
            row[1] = trofeo.getData().toString();
            row[2] = trofeo instanceof TrofeoIndividuale ? "Individuale" : "Squadra";
            trofeiModel.addRow(row);
        }
        
        JTable trofeiTable = new JTable(trofeiModel);
        trofeiPanel.add(new JScrollPane(trofeiTable), BorderLayout.CENTER);
        return trofeiPanel;
    }

    private void addBasicInfo(JPanel infoPanel, GridBagConstraints gbc, Calciatore calciatore) {
        addLabelValuePair(infoPanel, gbc, 0, "Nome:", calciatore.getNome());
        addLabelValuePair(infoPanel, gbc, 1, "Cognome:", calciatore.getCognome());
        addLabelValuePair(infoPanel, gbc, 2, "Data di Nascita:", calciatore.getDataDiNascita().toString());
        addLabelValuePair(infoPanel, gbc, 3, "Data di Ritiro:", 
                calciatore.getDataDiRitiro() != null ? calciatore.getDataDiRitiro().toString() : "In attività");
        addLabelValuePair(infoPanel, gbc, 4, "Piede:", calciatore.getPiede().name());
    }
    
    private void addLabelValuePair(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        panel.add(new JLabel(value), gbc);
    }
}