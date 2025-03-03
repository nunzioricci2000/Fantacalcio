package DAO;

import Model.Calciatore;
import Model.Ruolo;

import java.util.List;

public interface RuoliDAO {
    List<Ruolo> readRuoliDi(int idCalciatore);
    void delete(Ruolo ruolo, Calciatore calciatore);
    void create(Ruolo ruolo, Calciatore calciatore);
}
