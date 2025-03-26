package DAO;

import Model.Calciatore;
import Model.Ruolo;

import java.sql.SQLException;
import java.util.List;

public interface RuoliDAO {
    List<Ruolo> readRuoliDi(int idCalciatore) throws SQLException;
    void delete(Ruolo ruolo, int idCalciatore) throws SQLException;
    void create(Ruolo ruolo, int idCalciatore) throws SQLException;
}
