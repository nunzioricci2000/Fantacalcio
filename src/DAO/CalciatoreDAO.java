package DAO;

import Model.Calciatore;
import Model.Filtro;

import java.util.List;

public interface CalciatoreDAO {
    List<Calciatore> readCalciatori(Filtro filtro);
    Calciatore read(int id);
    Calciatore update(Calciatore calciatore);
    void delete(Calciatore calciatore);
    Calciatore create(Calciatore calciatore);
}
