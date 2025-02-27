package DAO;

import Model.Calciatore;
import Model.Militanza;

import java.util.List;

public interface MilitanzaDAO {
    List<Militanza> getCarrieraDi(Calciatore calciatore);
    Militanza update(Militanza militanza);
    void delete(Militanza militanza);
    Militanza create(Militanza militanza);
}
