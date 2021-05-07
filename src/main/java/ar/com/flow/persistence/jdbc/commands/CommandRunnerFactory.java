package ar.com.flow.persistence.jdbc.commands;

import ar.com.flow.persistence.jdbc.commit.CommitStrategy;
import ar.com.flow.persistence.jdbc.connection.DefaultConnection;
import ar.com.flow.persistence.jdbc.error.ContinueExecution;
import ar.com.flow.persistence.jdbc.error.ErrorHandler;
import ar.com.flow.persistence.jdbc.error.Rollback;

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
        var connectionWrapper = new DefaultConnection(connection, commitStrategy, errorHandler);
        return new DefaultCommandRunner(connectionWrapper);
    }

    public static ErrorHandler errorHandler(Connection connection, boolean stopOnError) {
        return stopOnError ? new Rollback(connection) : new ContinueExecution();
    }
}
