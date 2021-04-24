package ar.com.kamikaze.persistence.jdbc.commands;

import ar.com.kamikaze.persistence.jdbc.commit.AutoCommitStrategy;
import ar.com.kamikaze.persistence.jdbc.commit.ManualCommitStrategy;
import ar.com.kamikaze.persistence.jdbc.connection.DefaultConnectionControl;
import ar.com.kamikaze.persistence.jdbc.connection.JdbcConnectionWrapper;
import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class CommandRunnerFactory {
    public static CommandRunner createAutoCommitCommandRunner(Connection wrappedConnection, ErrorHandler errorHandler) throws SQLException {
        var commitStrategy = new AutoCommitStrategy(wrappedConnection);
        var connectionWrapper = new JdbcConnectionWrapper(wrappedConnection, false, errorHandler);
        var connection = new DefaultConnectionControl(connectionWrapper, commitStrategy);
        return new DefaultCommandRunner(connection);
    }

    public static CommandRunner createManualCommitCommandRunner(Connection wrappedConnection, ErrorHandler errorHandler) throws SQLException {
        var commitStrategy = new ManualCommitStrategy();
        var connectionWrapper = new JdbcConnectionWrapper(wrappedConnection, true, errorHandler);
        var connection = new DefaultConnectionControl(connectionWrapper, commitStrategy);
        return new DefaultCommandRunner(connection);
    }
}
