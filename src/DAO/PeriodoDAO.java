package DAO;

import Model.Periodo;
import Model.Squadra;

import java.sql.SQLException;
import java.util.List;

public interface PeriodoDAO {
    List<Periodo> getPeriodiDi(int idCalciatore, Squadra squadra) throws SQLException;
    void delete(Periodo periodo, int idCalciatore, Squadra squadra) throws SQLException;
    Periodo create(Periodo periodo, int idCalciatore, Squadra squadra) throws SQLException;
}
