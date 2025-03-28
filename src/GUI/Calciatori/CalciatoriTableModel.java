package GUI.Calciatori;

import Model.Calciatore;
import Model.Militanza;
import Model.Ruolo;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CalciatoriTableModel extends DefaultTableModel {
    
    public CalciatoriTableModel() {
        super(new String[]{"ID", "Nome", "Cognome", "Età", "Ruoli", "Squadra"}, 0);
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // Impedisce la modifica diretta delle celle
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0 || columnIndex == 3) {
            return Integer.class;
        }
        return String.class;
    }
    
    public void displayCalciatori(List<Calciatore> calciatori) {
        setRowCount(0); // Pulisce la tabella
        
        for (Calciatore calciatore : calciatori) {
            Object[] row = new Object[6];
            row[0] = calciatore.getId();
            row[1] = calciatore.getNome();
            row[2] = calciatore.getCognome();
            
            // Calcolo dell'età (approssimativo)
            long eta = (System.currentTimeMillis() - calciatore.getDataDiNascita().getTime()) / (1000L * 60 * 60 * 24 * 365);
            row[3] = (int) eta;
            
            // Ruoli come stringa
            StringBuilder ruoli = new StringBuilder();
            for (Ruolo ruolo : calciatore.getRuoli()) {
                if (ruoli.length() > 0) ruoli.append(", ");
                ruoli.append(ruolo.name());
            }
            row[4] = ruoli.toString();
            
            // Squadra attuale
            List<Militanza> carriera = calciatore.getCarriera();
            row[5] = carriera.isEmpty() ? "Nessuna" : carriera.get(carriera.size() - 1).getSquadra().nome();
            
            addRow(row);
        }
    }
    
    public int getIdAtRow(int row) {
        if (row >= 0 && row < getRowCount()) {
            return (int) getValueAt(row, 0);
        }
        return -1;
    }
}