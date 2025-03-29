package PostgresImplDAO;

import DAO.PeriodoDAO;
import Database.DatabaseConnection;
import Model.Periodo;
import Model.Squadra;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PeriodoPostgresImplDAO implements PeriodoDAO {
    private final Connection connection;

    public PeriodoPostgresImplDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }
    @Override
    public List<Periodo> getPeriodiDi(int idCalciatore, Squadra squadra) throws SQLException {
        ArrayList<Periodo> periodi = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM periodo WHERE id_calciatore = "
                + idCalciatore + " AND nome_squadra = '" + squadra.nome()
                + "' AND nazionalitÀ_squadra = '" + squadra.nazionalita() + "'");
        while (resultSet.next()) {
            Periodo periodo = new Periodo(
                    resultSet.getDate("data_inizio"),
                    resultSet.getDate("data_fine")
            );
            periodi.add(periodo);
        }
        statement.close();
        resultSet.close();
        return periodi;
    }

    @Override
    public void delete(Periodo periodo, int idCalciatore, Squadra squadra) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "DELETE FROM ruoli WHERE id_calciatore = " + idCalciatore +
                        " AND squadra = '" + squadra.nome() +
                        "' AND nazionalita = '" + squadra.nazionalita() +
                        "' AND data_inizio = '" + periodo.dataInizio() +
                        "' AND data_fine = '" + periodo.dataFine() + "'");
        statement.close();
    }

    @Override
    public Periodo create(Periodo periodo, int idCalciatore, Squadra squadra) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "INSERT INTO periodo(id_calciatore, nome_squadra, nazionalitÀ, data_inizio, data_fine) VALUES ("
                        + idCalciatore + ", '" + squadra.nome() + "', '"
                        + squadra.nazionalita() + "', '" + periodo.dataInizio() + "', '" + periodo.dataFine() + "')");
        statement.close();
        return periodo;
    }
}
