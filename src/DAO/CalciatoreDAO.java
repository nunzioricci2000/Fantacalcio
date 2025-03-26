package DAO;

import Model.Calciatore;
import Model.Filtro;

import java.sql.SQLException;
import java.util.List;

public interface CalciatoreDAO {
    List<Calciatore> readCalciatori(Filtro filtro) throws SQLException;
    Calciatore read(int id) throws SQLException;
    Calciatore update(Calciatore calciatore) throws SQLException;
    void delete(Calciatore calciatore) throws SQLException;
    Calciatore create(Calciatore calciatore) throws SQLException;
}
