package ar.com.kamikaze.persistence.jdbc.connection;

import ar.com.kamikaze.persistence.jdbc.commit.CommitStrategy;
import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;

import java.sql.SQLException;
import java.sql.Statement;

public class DefaultConnection implements Connection {
    private final java.sql.Connection wrappedConnection;
    private boolean autoCommit;
    private boolean originalAutoCommit;
    private ErrorHandler errorHandler;
    private CommitStrategy commitStrategy;

    public DefaultConnection(java.sql.Connection wrappedConnection, CommitStrategy commitStrategy, ErrorHandler errorHandler) throws SQLException {
        this.wrappedConnection = wrappedConnection;
        this.commitStrategy = commitStrategy;
        this.autoCommit = commitStrategy.isAutomatic();
        this.originalAutoCommit = wrappedConnection.getAutoCommit();
        this.errorHandler = errorHandler;
    }

    @Override
    public void setUpExecution() throws SQLException {
        wrappedConnection.setAutoCommit(autoCommit);
    }

    @Override
    public void endExecution() throws SQLException {
        wrappedConnection.setAutoCommit(originalAutoCommit);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return wrappedConnection.createStatement();
    }

    @Override
    public void commit() throws SQLException {
        commitStrategy.commit(this.wrappedConnection);
    }

    @Override
    public void handleError(SQLException exception) throws SQLException {
        errorHandler.handle(exception);
    }

    @Override
    public void rollback() throws SQLException {
        wrappedConnection.rollback();
        endExecution();
    }
}
