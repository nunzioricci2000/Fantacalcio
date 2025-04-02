package GUI.Calciatori;

import Control.Controller;
import Model.Calciatore;

import javax.swing.*;
import java.util.List;

public class CalciatoriTable extends JTable {
    private final CalciatoriTableModel tableModel;
    
    public CalciatoriTable(Controller controller) {
        this.tableModel = new CalciatoriTableModel();
        setModel(tableModel);
        
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoCreateRowSorter(true);
        
        getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = getSelectedRow();
                if (selectedRow >= 0) {
                    int id = tableModel.getIdAtRow(getSelectedRow());
                    try {
                        controller.vediCaratteristicheCalciatore(id);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    
    public void displayCalciatori(List<Calciatore> calciatori) {
        tableModel.displayCalciatori(calciatori);
    }
}