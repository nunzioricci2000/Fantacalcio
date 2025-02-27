package Model;

public interface Utente {
    void accediComeAmministratore(String password);
    void vediElencoCalciatori();
    void vediCaratteristicheCalciatore(Calciatore calciatore);
    void filtraPer(Filtro filtro);
}
