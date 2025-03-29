package DAO;

import Model.Ruolo;

import java.sql.SQLException;
import java.util.List;

public interface RuoliDAO {
    List<Ruolo> readRuoliDi(int idCalciatore) throws SQLException;
    void delete(Ruolo ruolo, int idCalciatore) throws SQLException;
    void create(Ruolo ruolo, int idCalciatore) throws SQLException;
    void addRuoloCalciatore(int idCalciatore, Ruolo ruolo) throws SQLException;
    void deleteRuoliCalciatore(int idCalciatore) throws SQLException;
}
