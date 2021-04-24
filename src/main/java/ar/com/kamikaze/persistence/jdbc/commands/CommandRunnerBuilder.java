package ar.com.kamikaze.persistence.jdbc.commands;

import ar.com.kamikaze.persistence.jdbc.result.ResultObserver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static ar.com.kamikaze.persistence.jdbc.commands.CommandRunnerFactory.createAutoCommitCommandRunner;
import static ar.com.kamikaze.persistence.jdbc.commands.CommandRunnerFactory.createManualCommitCommandRunner;

public class CommandRunnerBuilder {
    private final Connection connection;
    private boolean autoCommit;
    private boolean stopOnError;
    private Collection<ResultObserver> resultObservers = new ArrayList<>();

    public static CommandRunnerBuilder wrap(Connection connection) {
        return new CommandRunnerBuilder(connection);
    }

    public CommandRunnerBuilder(Connection connection) {
        this.connection = connection;
    }

    public CommandRunnerBuilder autoCommit() {
        this.autoCommit = true;
        return this;
    }

    public CommandRunnerBuilder stopOnError() {
        this.stopOnError = true;
        return this;
    }

    public CommandRunnerBuilder addResultObserver(ResultObserver observer) {
        resultObservers.add(observer);
        return this;
    }

    public CommandRunner build() throws SQLException {
        CommandRunner commandRunner = autoCommit ?
                createAutoCommitCommandRunner(connection, stopOnError) :
                createManualCommitCommandRunner(connection, stopOnError);

        resultObservers.forEach(commandRunner::addResultObserver);

        return commandRunner;
    }
}
