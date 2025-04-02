package Model;

public interface Utente {
    void accediComeAmministratore(String password);
    void vediElencoCalciatori();
    void vediCaratteristicheCalciatore(int id);
    void filtraPer(Filtro filtro);
}
