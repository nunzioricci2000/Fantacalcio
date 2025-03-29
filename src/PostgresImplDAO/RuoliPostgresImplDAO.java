package PostgresImplDAO;

import DAO.RuoliDAO;
import Database.DatabaseConnection;
import Model.Ruolo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RuoliPostgresImplDAO implements RuoliDAO {
    private final Connection connection;

    public RuoliPostgresImplDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Ruolo> readRuoliDi(int idCalciatore) throws SQLException {
        ArrayList<Ruolo> ruoli = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM ruoli WHERE id_calciatore = " + idCalciatore);
        while (resultSet.next()) {
            Ruolo ruolo = Ruolo.valueOf(resultSet.getString("ruolo"));
            ruoli.add(ruolo);
        }
        statement.close();
        resultSet.close();
        return ruoli;
    }

    @Override
    public void delete(Ruolo ruolo, int idCalciatore) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "DELETE FROM ruoli WHERE ruolo = '" + ruolo.name() +
                        "' AND id_calciatore = " + idCalciatore);
        statement.close();
    }

    @Override
    public void create(Ruolo ruolo, int idCalciatore) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "INSERT INTO ruoli(id_calciatore, ruolo) VALUES ("
                        + idCalciatore + ", '" + ruolo.name() + "')");
        statement.close();
    }

    @Override
    public void addRuoloCalciatore(int idCalciatore, Ruolo ruolo) throws SQLException {
        // Questo metodo può semplicemente chiamare create se la funzionalità è la stessa
        create(ruolo, idCalciatore);
    }

    @Override
    public void deleteRuoliCalciatore(int idCalciatore) throws SQLException {
        // Elimina tutti i ruoli di un calciatore
        String sql = "DELETE FROM ruoli WHERE id_calciatore = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCalciatore);
            stmt.executeUpdate();
        }
    }
}
