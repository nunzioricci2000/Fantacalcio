package UI;

import Model.Calciatore;

import java.util.List;

import Control.Controller;

public interface UserInterface {
    void initialize();
    void showMessage(String message);
    void showError(String error);
    
    void showLoginView();
    void showCalciatoriView();
    void showAdminView();
    
    void displayCalciatori(List<Calciatore> calciatori);
    void displayDettaglioCalciatore(Calciatore calciatore);
    
    void notifyAdminMode(boolean isAdmin);
    
    void shutdown();

    void setController(Controller controller);
}