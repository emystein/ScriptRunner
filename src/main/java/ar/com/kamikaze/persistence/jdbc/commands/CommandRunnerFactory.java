package ar.com.kamikaze.persistence.jdbc.commands;

import ar.com.kamikaze.persistence.jdbc.commit.AutoCommitStrategy;
import ar.com.kamikaze.persistence.jdbc.commit.CommitStrategy;
import ar.com.kamikaze.persistence.jdbc.commit.ManualCommitStrategy;
import ar.com.kamikaze.persistence.jdbc.connection.DefaultConnectionControl;
import ar.com.kamikaze.persistence.jdbc.connection.JdbcConnectionWrapper;
import ar.com.kamikaze.persistence.jdbc.error.ContinueExecution;
import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;
import ar.com.kamikaze.persistence.jdbc.error.Rollback;

import java.sql.Connection;
import java.sql.SQLException;

public class CommandRunnerFactory {
    public static CommandRunner createAutoCommitCommandRunner(Connection wrappedConnection, boolean stopOnError) throws SQLException {
        return createCommandRunner(wrappedConnection, new AutoCommitStrategy(wrappedConnection), stopOnError);
    }

    public static CommandRunner createManualCommitCommandRunner(Connection wrappedConnection, boolean stopOnError) throws SQLException {
        return createCommandRunner(wrappedConnection, new ManualCommitStrategy(), stopOnError);
    }

    public static CommandRunner createCommandRunner(Connection wrappedConnection, CommitStrategy commitStrategy, boolean stopOnError) throws SQLException {
        var errorHandler = errorHandler(wrappedConnection, stopOnError);
        return createCommandRunner(wrappedConnection, commitStrategy, errorHandler);
    }

    public static CommandRunner createCommandRunner(Connection wrappedConnection, CommitStrategy commitStrategy, ErrorHandler errorHandler) throws SQLException {
        var connectionWrapper = new JdbcConnectionWrapper(wrappedConnection, commitStrategy.isManualCommit(), errorHandler);
        var connectionControl = new DefaultConnectionControl(connectionWrapper, commitStrategy);
        return new DefaultCommandRunner(connectionControl);
    }

    private static ErrorHandler errorHandler(Connection connection, boolean stopOnError) {
        return stopOnError ?
                new Rollback(connection) :
                new ContinueExecution();

    }
}
