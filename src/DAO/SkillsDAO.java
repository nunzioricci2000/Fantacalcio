package DAO;

import Model.Calciatore;
import Model.Skill;

import java.sql.SQLException;
import java.util.List;

public interface SkillsDAO {
    List<Skill> readSkillsDi(int idCalciatore) throws SQLException;
    void delete(Skill skill, Calciatore calciatore) throws SQLException;
    void create(Skill skill, Calciatore calciatore) throws SQLException;
}
