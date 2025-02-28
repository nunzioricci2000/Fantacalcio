package DAO;

import Model.*;

import java.util.List;

public interface TrofeoDAO {
    List<TrofeoIndividuale> readTrofeiDi(Calciatore calciatore);
    List<TrofeoDiSquadra> readTrofeiDi(Squadra squadra);
    void delete(Trofeo trofeo);
    Trofeo readTrofeo(Calciatore calciatore);
}
