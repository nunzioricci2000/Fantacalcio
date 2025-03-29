package DAO;

import Model.Skill;

import java.sql.SQLException;
import java.util.List;

public interface SkillsDAO {
    List<Skill> readSkillsDi(int idCalciatore) throws SQLException;
    void delete(Skill skill, int idCalciatore) throws SQLException;
    void create(Skill skill, int idCalciatore) throws SQLException;
    void addSkillCalciatore(int idCalciatore, Skill skill) throws SQLException;
    void deleteSkillsCalciatore(int idCalciatore) throws SQLException;
}
