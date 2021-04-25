package ar.com.kamikaze.persistence.jdbc.connection;

import ar.com.kamikaze.persistence.jdbc.commit.CommitStrategy;

import java.sql.SQLException;
import java.sql.Statement;

public interface Connection {
    void setUpExecution() throws SQLException;

    void endExecution() throws SQLException;

    Statement createStatement() throws SQLException;

    void commit(CommitStrategy commitStrategy) throws SQLException;

    void handleError(SQLException exception) throws SQLException;

    void rollback() throws SQLException;
}
