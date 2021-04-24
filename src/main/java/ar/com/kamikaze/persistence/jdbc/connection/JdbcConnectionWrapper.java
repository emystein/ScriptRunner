package ar.com.kamikaze.persistence.jdbc.connection;

import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcConnectionWrapper {
    protected final Connection connection;
    private ErrorHandler errorHandler;
    private boolean autoCommit;
    private boolean originalAutoCommit;

    protected JdbcConnectionWrapper(Connection connection, boolean autoCommit) throws SQLException {
        this.connection = connection;
        this.autoCommit = autoCommit;
        this.originalAutoCommit = connection.getAutoCommit();
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void setUpExecution() throws SQLException {
        setAutoCommit(autoCommit);
    }

    public void endExecution() throws SQLException {
        setAutoCommit(originalAutoCommit);
    }

    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void handleError(SQLException exception) throws SQLException {
        errorHandler.handle(exception);
    }

    public void rollback() throws SQLException {
        connection.rollback();
        setAutoCommit(originalAutoCommit);
    }

    private void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }
}
