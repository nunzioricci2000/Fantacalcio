package PostgresImplDAO;

import DAO.SkillsDAO;
import Database.DatabaseConnection;
import Model.Skill;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SkillsPostgresImplDAO implements SkillsDAO {
    private final Connection connection;

    public SkillsPostgresImplDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Skill> readSkillsDi(int idCalciatore) throws SQLException {
        ArrayList<Skill> skills = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM skills WHERE id_calciatore = " + idCalciatore);
        while (resultSet.next()) {
            Skill skill = Skill.valueOf(resultSet.getString("skill"));
            skills.add(skill);
        }
        statement.close();
        resultSet.close();
        return skills;
    }

    @Override
    public void delete(Skill skill, int idCalciatore) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "DELETE FROM skills WHERE skill = '" + skill.name() +
                        "' AND id_calciatore = " + idCalciatore);
        statement.close();
    }

    @Override
    public void create(Skill skill, int idCalciatore) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "INSERT INTO skills(id_calciatore, skill) VALUES ("
                        + idCalciatore + ", '" + skill.name() + "')");
        statement.close();
    }
}
