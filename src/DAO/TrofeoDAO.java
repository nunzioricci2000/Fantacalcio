package DAO;

import Model.*;

import java.sql.SQLException;
import java.util.List;

public interface TrofeoDAO {
    List<TrofeoIndividuale> readTrofeiDi(int idCalciatore) throws SQLException;
    void delete(Trofeo trofeo) throws SQLException;
    void create(Trofeo trofeo) throws SQLException;
}
