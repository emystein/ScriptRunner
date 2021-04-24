package ar.com.kamikaze.persistence.jdbc.connection;

import ar.com.kamikaze.persistence.jdbc.error.ContinueExecution;
import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;
import ar.com.kamikaze.persistence.jdbc.error.Rollback;
import ar.com.kamikaze.persistence.jdbc.result.CommandResultListener;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class CommandRunnerBuilder {
    private final Connection connection;
    private boolean autoCommit;
    private boolean stopOnError;
    private Collection<CommandResultListener> listeners = new ArrayList<>();

    public static CommandRunnerBuilder wrap(Connection connection) {
        return new CommandRunnerBuilder(connection);
    }

    public CommandRunnerBuilder(Connection connection) {
        this.connection = connection;
    }

    public CommandRunnerBuilder autoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
        return this;
    }

    public CommandRunnerBuilder stopOnError(boolean stopOnError) {
        this.stopOnError = stopOnError;
        return this;
    }

    public CommandRunnerBuilder addCommandResultListener(CommandResultListener listener) {
        listeners.add(listener);
        return this;
    }

    public Commands build() throws SQLException {
        ErrorHandler errorHandler = stopOnError ?
                new Rollback(connection) :
                new ContinueExecution();

        Commands wrapper = autoCommit ?
                new AutoCommitCommandRunner(connection, errorHandler) :
                new ManualCommitCommandRunner(connection, errorHandler);

        listeners.forEach(wrapper::addCommandResultListener);

        return wrapper;
    }
}
