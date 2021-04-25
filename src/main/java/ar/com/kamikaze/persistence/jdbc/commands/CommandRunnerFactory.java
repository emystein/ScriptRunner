package ar.com.kamikaze.persistence.jdbc.commands;

import ar.com.kamikaze.persistence.jdbc.commit.CommitStrategy;
import ar.com.kamikaze.persistence.jdbc.connection.DefaultConnectionControl;
import ar.com.kamikaze.persistence.jdbc.connection.JdbcConnectionWrapper;
import ar.com.kamikaze.persistence.jdbc.error.ContinueExecution;
import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;
import ar.com.kamikaze.persistence.jdbc.error.Rollback;

import java.sql.Connection;
import java.sql.SQLException;

public class CommandRunnerFactory {
    public static CommandRunner createCommandRunner(Connection connection,
                                                    CommitStrategy commitStrategy,
                                                    boolean stopOnError) throws SQLException {
        return createCommandRunner(connection, commitStrategy, errorHandler(connection, stopOnError));
    }

    public static CommandRunner createCommandRunner(Connection connection,
                                                    CommitStrategy commitStrategy,
                                                    ErrorHandler errorHandler) throws SQLException {
        var connectionWrapper = new JdbcConnectionWrapper(connection, commitStrategy.isManual(), errorHandler);
        var connectionControl = new DefaultConnectionControl(connectionWrapper, commitStrategy);
        return new DefaultCommandRunner(connectionControl);
    }

    public static ErrorHandler errorHandler(Connection connection, boolean stopOnError) {
        return stopOnError ? new Rollback(connection) : new ContinueExecution();
    }
}