package PostgresImplDAO;

import DAO.CalciatoreDAO;
import Database.DatabaseConnection;
import Model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalciatorePostgresImplDAO implements CalciatoreDAO {
    private final Connection connection;
    private final TrofeoPostgresImplDAO trofeoDAO;
    private final MilitanzaPostgresImplDAO militanzaDAO;
    private final SkillsPostgresImplDAO skillsDAO;
    private final RuoliPostgresImplDAO ruoliDAO;

    public CalciatorePostgresImplDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
        trofeoDAO = new TrofeoPostgresImplDAO();
        militanzaDAO = new MilitanzaPostgresImplDAO();
        skillsDAO = new SkillsPostgresImplDAO();
        ruoliDAO = new RuoliPostgresImplDAO();
    }

    @Override
    public List<Calciatore> readCalciatori(Filtro filtro) throws SQLException {
        ArrayList<Calciatore> calciatori = new ArrayList<>();
        String query = buildQuery(filtro);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                calciatori.add(buildCalciatoreFromResultSet(rs));
            }
        }
        return calciatori;
    }

    private String buildQuery(Filtro filtro) {
        StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT C.* FROM calciatore C ");
        queryBuilder.append("JOIN statistichecalciatore SC ON C.id = SC.id ");
        List<String> conditions = new ArrayList<>();
        if (filtro != null) {
            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                conditions.add("(C.NOME ILIKE '%" + filtro.getNome() + "%' OR C.COGNOME ILIKE '%" + filtro.getNome() + "%')");
            }
            if (filtro.getRuolo() != null && !filtro.getRuolo().isEmpty()) {
                queryBuilder.append("JOIN Ruoli R ON C.ID = R.ID_CALCIATORE ");
                conditions.add("R.RUOLO = '" + filtro.getRuolo() + "'");
            }
            if (filtro.getPiede() != null) {
                conditions.add("C.PIEDE = '" + filtro.getPiede().name() + "'");
            }
            if (filtro.getMinimoGoalSegnati() != null) {
                conditions.add("SC.TOTALE_GOAL_SEGNATI >= " + filtro.getMinimoGoalSegnati());
            }
            if (filtro.getMassimoGoalSegnati() != null) {
                conditions.add("SC.TOTALE_GOAL_SEGNATI <= " + filtro.getMassimoGoalSegnati());
            }
            if (filtro.getMinimoGoalSubiti() != null) {
                conditions.add("SC.TOTALE_GOAL_SUBITI >= " + filtro.getMinimoGoalSubiti());
            }
            if (filtro.getMassimoGoalSubiti() != null) {
                conditions.add("SC.TOTALE_GOAL_SUBITI <= " + filtro.getMassimoGoalSubiti());
            }
            if (filtro.getMinimoEta() != null) {
                conditions.add("SC.eta >= " + filtro.getMinimoEta());
            }
            if (filtro.getMassimoEta() != null) {
                conditions.add("SC.eta <= " + filtro.getMassimoEta());
            }
            if (filtro.getSquadra() != null) {
                conditions.add("(SC.ULTIMA_SQUADRA_NOME = '" + filtro.getSquadra().nome() + "' AND " +
                        "SC.ULTIMA_SQUADRA_NAZIONALITA = '" + filtro.getSquadra().nazionalita() + "')");
            }
        }
        if (!conditions.isEmpty()) {
            queryBuilder.append("WHERE ");
            queryBuilder.append(String.join(" AND ", conditions));
        }
        return queryBuilder.toString();
    }

    @Override
    public Calciatore read(int id) throws SQLException {
        String query = "SELECT * FROM calciatore WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return buildCalciatoreFromResultSet(rs);
                } else {
                    return null;
                }
            }
        }
    }

    private Calciatore buildCalciatoreFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String nome = rs.getString("NOME");
        String cognome = rs.getString("COGNOME");
        Date dataNascita = rs.getDate("DATA_DI_NASCITA");
        Date dataRitiro = rs.getDate("DATA_DI_RITIRO");
        Piede piede = Piede.valueOf(rs.getString("PIEDE"));
        List<Ruolo> ruoli = ruoliDAO.readRuoliDi(id);
        List<Skill> skills = skillsDAO.readSkillsDi(id);
        List<Trofeo> trofei = trofeoDAO.readTrofeiDi(id);
        List<Militanza> carriera = militanzaDAO.getCarrieraDi(id);
        return new Calciatore(id, nome, cognome, dataNascita, dataRitiro, piede, ruoli, skills, trofei, carriera);
    }

    /**
     * Modifica un calciatore nel database
     * @param calciatore il calciatore da modificare
     * @return il calciatore modificato
     * @throws SQLException in caso di errore nel database
     *
     * @implNote Il metodo non modifica gli oggetti a cui il calciatore è correlato!
     *  Se deve essere modificata o eliminata una relazione con Militanza, Ruolo, Skill
     *  o Trofeo, si deve fare tramite le rispettive DAO.
     */
    @Override
    public Calciatore update(Calciatore calciatore) throws SQLException {
        String query = "UPDATE calciatore SET nome = ?, cognome = ?, data_di_nascita = ?, " +
                "data_di_ritiro = ?, piede = ?::T_PIEDE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, calciatore.getNome());
            stmt.setString(2, calciatore.getCognome());
            stmt.setDate(3, new java.sql.Date(calciatore.getDataDiNascita().getTime()));
            if (calciatore.getDataDiRitiro() != null) {
                stmt.setDate(4, new java.sql.Date(calciatore.getDataDiRitiro().getTime()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setString(5, calciatore.getPiede().name());
            stmt.setInt(6, calciatore.getId());
            stmt.executeUpdate();
        }
        return calciatore;
    }

    @Override
    public void delete(Calciatore calciatore) throws SQLException {
        String query = "DELETE FROM calciatore WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, calciatore.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public Calciatore create(Calciatore calciatore) throws SQLException {
        String query = "INSERT INTO calciatore (nome, cognome, data_di_nascita, data_di_ritiro, piede) " +
                "VALUES (?, ?, ?, ?, ?::T_PIEDE)";
        
        // Aggiungi Statement.RETURN_GENERATED_KEYS per ottenere l'ID generato
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, calciatore.getNome());
            stmt.setString(2, calciatore.getCognome());
            stmt.setDate(3, new java.sql.Date(calciatore.getDataDiNascita().getTime()));
            
            if (calciatore.getDataDiRitiro() != null) {
                stmt.setDate(4, new java.sql.Date(calciatore.getDataDiRitiro().getTime()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            
            stmt.setString(5, calciatore.getPiede().name());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Errore nella creazione del calciatore, nessuna riga inserita");
            }
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("ID generato: " + id);
                    for (Ruolo ruolo : calciatore.getRuoli()) {
                        ruoliDAO.create(ruolo, id);
                    }
                    for (Skill skill : calciatore.getSkills()) {
                        skillsDAO.create(skill, id);
                    }
                    return new Calciatore(
                        id, 
                        calciatore.getNome(), 
                        calciatore.getCognome(),
                        calciatore.getDataDiNascita(), 
                        calciatore.getDataDiRitiro(),
                        calciatore.getPiede(), 
                        calciatore.getRuoli(), 
                        calciatore.getSkills(),
                        calciatore.getTrofei(), 
                        calciatore.getCarriera()
                    );
                } else {
                    throw new SQLException("Errore nella creazione del calciatore, nessun ID restituito");
                }
            }
        }
    }
}