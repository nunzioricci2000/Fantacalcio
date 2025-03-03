package DAO;

import Model.Militanza;
import Model.Periodo;

import java.util.List;

public interface PeriodoDAO {
    List<Periodo> getPeriodiDi(Militanza militanza);
    Periodo update(Periodo periodo);
    void delete(Periodo periodo);
    Periodo create(Periodo periodo);
}
