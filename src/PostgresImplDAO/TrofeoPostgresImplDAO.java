package PostgresImplDAO;

import DAO.TrofeoDAO;
import Database.DatabaseConnection;
import Model.Squadra;
import Model.Trofeo;
import Model.TrofeoDiSquadra;
import Model.TrofeoIndividuale;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrofeoPostgresImplDAO implements TrofeoDAO {
    private final Connection connection;

    public TrofeoPostgresImplDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Trofeo> readTrofeiDi(int idCalciatore) throws SQLException {
        ArrayList<Trofeo> trofei = new ArrayList<>();
        String query = "SELECT * FROM TrofeiCalciatore WHERE ID_CALCIATORE = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idCalciatore);

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    String nome = resultSet.getString("NOME_TROFEO");
                    Date data = resultSet.getDate("DATA_TROFEO");
                    String nomeSquadra = resultSet.getString("NOME_SQUADRA");
                    String nazionalitaSquadra = resultSet.getString("NAZIONALITÀ_SQUADRA");

                    if (nomeSquadra == null) {
                        trofei.add(new TrofeoIndividuale(nome, data, idCalciatore));
                    } else {
                        Squadra squadra = new Squadra(nomeSquadra, nazionalitaSquadra);
                        trofei.add(new TrofeoDiSquadra(nome, data, squadra));
                    }
                }
            }
        }
        return trofei;
    }

    @Override
    public void delete(Trofeo trofeo) throws SQLException {
        String query = "DELETE FROM Trofeo WHERE NOME = ? AND DATA = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trofeo.getNome());
            stmt.setDate(2, new java.sql.Date(trofeo.getData().getTime()));
            stmt.executeUpdate();
        }
    }

    @Override
    public void create(Trofeo trofeo) throws SQLException {
        if (trofeo instanceof TrofeoIndividuale trofeoIndividuale) {
            String query = "INSERT INTO Trofeo (NOME, DATA, ID_CALCIATORE) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, trofeo.getNome());
                stmt.setDate(2, new java.sql.Date(trofeo.getData().getTime()));
                stmt.setInt(3, trofeoIndividuale.getIdCalciatore());
                stmt.executeUpdate();
            }
        } else if (trofeo instanceof TrofeoDiSquadra trofeoDiSquadra) {
            String query = "INSERT INTO Trofeo (NOME, DATA, NOME_SQUADRA, \"nazionalitÀ_squadra\") VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, trofeo.getNome());
                stmt.setDate(2, new java.sql.Date(trofeo.getData().getTime()));
                stmt.setString(3, trofeoDiSquadra.getSquadra().nome());
                stmt.setString(4, trofeoDiSquadra.getSquadra().nazionalita());
                stmt.executeUpdate();
            }
        }
    }
}