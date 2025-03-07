package DAO;

import Model.Squadra;

import java.sql.SQLException;
import java.util.List;

public interface SquadraDAO {
    List<Squadra> readSquadre() throws SQLException;
//    Squadra update(Squadra oldSquadra, Squadra newSquadra);
//    void delete(Squadra oldSquadra);
//    Squadra create(Squadra squadra);
}
