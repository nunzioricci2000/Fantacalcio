package PostgresImplDAO;

import DAO.MilitanzaDAO;
import Database.DatabaseConnection;
import Model.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MilitanzaPostgresImplDAO implements MilitanzaDAO {
    private final Connection connection;

    public MilitanzaPostgresImplDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
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
        statement.executeQuery(
                "DELETE FROM militanza WHERE id_calciatore = " + idCalciatore +
                        " AND nome_squadra = '" + squadra.nome() +
                        "' AND nazionalitÀ_squadra = '" + squadra.nazionalita() + "'");
        statement.close();
    }

    @Override
    public Militanza create(int idCalciatore, Militanza militanza) throws SQLException {
        Statement statement = connection.createStatement();
        String goalSubiti = "NULL";
        if (militanza instanceof MilitanzaPortiere) {
            goalSubiti = String.valueOf(((MilitanzaPortiere) militanza).getGoalSubiti());
        }
        statement.executeQuery(
                "INSERT INTO militanza(id_calciatore, nome_squadra, nazionalitÀ_squadra, partite_giocate, goal_segnati, goal_subiti) VALUES ("
                        + idCalciatore + ", '" + militanza.getSquadra().nome() + "', '"
                        + militanza.getSquadra().nazionalita() + "', " + militanza.getPartiteGiocate()
                        + ", " + militanza.getGoalSegnati() + ", " + goalSubiti + ")");
        statement.close();
        return militanza;
    }
}
