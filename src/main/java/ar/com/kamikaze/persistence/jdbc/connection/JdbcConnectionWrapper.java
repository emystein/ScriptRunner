package ar.com.kamikaze.persistence.jdbc.connection;

import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcConnectionWrapper implements JdbcConnection {
    private final Connection connection;
    private boolean autoCommit;
    private boolean originalAutoCommit;
    private ErrorHandler errorHandler;

    public JdbcConnectionWrapper(Connection connection, boolean autoCommit, ErrorHandler errorHandler) throws SQLException {
        this.connection = connection;
        this.autoCommit = autoCommit;
        this.originalAutoCommit = connection.getAutoCommit();
        this.errorHandler = errorHandler;
    }

    @Override
    public void setUpExecution() throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    @Override
    public void endExecution() throws SQLException {
        connection.setAutoCommit(originalAutoCommit);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }

    @Override
    public void handleError(SQLException exception) throws SQLException {
        errorHandler.handle(exception);
    }

    @Override
    public void rollback() throws SQLException {
        connection.rollback();
        endExecution();
    }
}
