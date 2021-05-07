package ar.com.flow.persistence.jdbc.connection;

import java.sql.SQLException;
import java.sql.Statement;

public interface Connection {
    void beginTransaction() throws SQLException;

    Statement createStatement() throws SQLException;

    void commitTransaction() throws SQLException;

    void handleError(SQLException exception) throws SQLException;

    void rollbackTransaction() throws SQLException;
}
