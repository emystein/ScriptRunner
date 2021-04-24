package ar.com.kamikaze.persistence.jdbc.connection;

import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;

import java.sql.SQLException;
import java.sql.Statement;

public interface ConnectionControl {
    void setErrorHandler(ErrorHandler errorHandler);

    void setUpExecution() throws SQLException;

    void endExecution() throws SQLException;

    Statement createStatement() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void handleError(SQLException exception) throws SQLException;
}