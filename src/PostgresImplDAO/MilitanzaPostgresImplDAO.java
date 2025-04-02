package PostgresImplDAO;

import DAO.MilitanzaDAO;
import Database.DatabaseConnection;
import Model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MilitanzaPostgresImplDAO implements MilitanzaDAO {
    private final Connection connection;
    private final PeriodoPostgresImplDAO periodoDAO;

    public MilitanzaPostgresImplDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
        periodoDAO = new PeriodoPostgresImplDAO();
    }

    @Override
    public List<Militanza> getCarrieraDi(int idCalciatore) throws SQLException {
        PeriodoPostgresImplDAO periodoPostgresImplDAO = new PeriodoPostgresImplDAO();
        ArrayList<Militanza> carriera = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM militanza WHERE id_calciatore = " + idCalciatore);
        while (resultSet.next()) {
            Squadra squadra = new Squadra(
                    resultSet.getString("nome_squadra"),
                    resultSet.getString("nazionalitÀ_squadra")
            );
            Militanza militanza;
            try {
                int goalSubiti = resultSet.getInt("goal_subiti");
                militanza = new MilitanzaPortiere(
                    squadra,
                    resultSet.getInt("partite_giocate"),
                    resultSet.getInt("goal_segnati"),
                    goalSubiti,
                    periodoPostgresImplDAO.getPeriodiDi(idCalciatore, squadra)
                );
            } catch (SQLException _) {
                militanza = new Militanza(
                    squadra,
                    resultSet.getInt("partite_giocate"),
                    resultSet.getInt("goal_segnati"),
                    periodoPostgresImplDAO.getPeriodiDi(idCalciatore, squadra)
                );
            }
            carriera.add(militanza);
        }
        return carriera;
    }

    @Override
    public void delete(int idCalciatore, Squadra squadra) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "DELETE FROM militanza WHERE id_calciatore = " + idCalciatore +
                        " AND nome_squadra = '" + squadra.nome() +
                        "' AND nazionalitÀ_squadra = '" + squadra.nazionalita() + "'");
        statement.close();
    }

    @Override
    public Militanza create(int idCalciatore, Militanza militanza) throws SQLException {
        String sql = "INSERT INTO militanza(id_calciatore, nome_squadra, nazionalitÀ_squadra, partite_giocate, goal_segnati, goal_subiti) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCalciatore);
            stmt.setString(2, militanza.getSquadra().nome());
            stmt.setString(3, militanza.getSquadra().nazionalita());
            stmt.setInt(4, militanza.getPartiteGiocate());
            stmt.setInt(5, militanza.getGoalSegnati());
            
            if (militanza instanceof MilitanzaPortiere) {
                stmt.setInt(6, ((MilitanzaPortiere) militanza).getGoalSubiti());
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }
            
            stmt.executeUpdate();
            
            for (Periodo periodo : militanza.getPeriodi()) {
                periodoDAO.create(periodo, idCalciatore, militanza.getSquadra());
            }
            
            return militanza;
        }
    }
}
