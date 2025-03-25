package DAO;

import Model.Calciatore;
import Model.Militanza;
import Model.Squadra;

import java.sql.SQLException;
import java.util.List;

public interface MilitanzaDAO {
    List<Militanza> getCarrieraDi(int idCalciatore) throws SQLException;
    void delete(int idCalciatore, Squadra squadra) throws SQLException;
    Militanza create(int idCalciatore, Militanza militanza) throws SQLException;
}
