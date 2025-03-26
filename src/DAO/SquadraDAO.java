package DAO;

import Model.Squadra;

import java.sql.SQLException;
import java.util.List;

public interface SquadraDAO {
    List<Squadra> readSquadre() throws SQLException;
}
