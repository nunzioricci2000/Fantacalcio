package DAO;

import Model.Squadra;

import java.util.List;

public interface SquadraDAO {
    List<Squadra> readSquadre();
    Squadra update(Squadra oldSquadra, Squadra newSquadra);
    void delete(Squadra oldSquadra);
    Squadra create(Squadra squadra);
}
