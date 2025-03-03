package DAO;

import Model.Calciatore;
import Model.Skill;

import java.util.List;

public interface SkillsDAO {
    List<Skill> readSkillsDi(int idCalciatore);
    void delete(Skill skill, Calciatore calciatore);
    void create(Skill skill, Calciatore calciatore);
}
