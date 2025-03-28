package GUI.Calciatori;

import Control.Controller;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CalciatoriView extends JPanel {
    private CalciatoriTable calciatoriTable;
    private CalciatoriDettaglioPanel dettaglioPanel;
    private CalciatoriFiltriPanel filtriPanel;
    
    public CalciatoriView(Controller controller) {
        setLayout(new BorderLayout());
        
        // Inizializzazione dei componenti
        filtriPanel = new CalciatoriFiltriPanel(controller);
        calciatoriTable = new CalciatoriTable(controller);
        dettaglioPanel = new CalciatoriDettaglioPanel(controller);
        
        // Configurazione del layout
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(calciatoriTable),
                dettaglioPanel);
        splitPane.setResizeWeight(0.6);
        
        add(filtriPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
    
    public void displayCalciatori(List<Calciatore> calciatori) {
        calciatoriTable.displayCalciatori(calciatori);
    }
    
    public void displayDettaglioCalciatore(Calciatore calciatore) {
        dettaglioPanel.displayDettaglioCalciatore(calciatore);
    }
}
