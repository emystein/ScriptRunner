package ar.com.kamikaze.persistence.jdbc.commands;

import ar.com.kamikaze.persistence.jdbc.commit.AutoCommit;
import ar.com.kamikaze.persistence.jdbc.commit.ManualCommit;
import ar.com.kamikaze.persistence.jdbc.connection.DefaultConnectionControl;
import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class CommandRunnerFactory {
    public static CommandRunner createAutoCommitCommandRunner(Connection wrappedConnection, ErrorHandler errorHandler) throws SQLException {
        var commit = new AutoCommit(wrappedConnection);
        var connection = new DefaultConnectionControl(wrappedConnection, false, commit);
        connection.setErrorHandler(errorHandler);
        return new DefaultCommandRunner(connection);
    }

    public static CommandRunner createManualCommitCommandRunner(Connection wrappedConnection, ErrorHandler errorHandler) throws SQLException {
        var commit = new ManualCommit();
        var connection = new DefaultConnectionControl(wrappedConnection, true, commit);
        connection.setErrorHandler(errorHandler);
        return new DefaultCommandRunner(connection);
    }
}
