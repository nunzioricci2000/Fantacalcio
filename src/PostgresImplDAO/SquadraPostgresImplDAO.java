package PostgresImplDAO;

import DAO.SquadraDAO;
import Database.DatabaseConnection;
import Model.Squadra;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SquadraPostgresImplDAO implements SquadraDAO {
    private final Connection connection;

    public SquadraPostgresImplDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Squadra> readSquadre() throws SQLException {
        ArrayList<Squadra> squadre = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM squadra");
        while (resultSet.next()) {
            String nome = resultSet.getString("NOME");
            String nazionalita = resultSet.getString("NAZIONALITÀ");
            Squadra squadra = new Squadra(nome, nazionalita);
            squadre.add(squadra);
        }
        statement.close();
        resultSet.close();
        return squadre;
    }
}
