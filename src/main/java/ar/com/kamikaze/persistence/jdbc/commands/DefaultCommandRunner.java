package ar.com.kamikaze.persistence.jdbc.commands;

import ar.com.kamikaze.persistence.jdbc.DefaultStatement;
import ar.com.kamikaze.persistence.jdbc.Statement;
import ar.com.kamikaze.persistence.jdbc.connection.Connection;
import ar.com.kamikaze.persistence.jdbc.result.*;
import ar.com.kamikaze.persistence.jdbc.script.ScriptCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class DefaultCommandRunner implements CommandRunner {
    private final Connection connection;
    private List<ResultObserver> resultObservers = new ArrayList<>();

    public void addResultObserver(ResultObserver observer) {
        resultObservers.add(observer);
    }

    public void execute(List<ScriptCommand> commands) throws SQLException {
        connection.beginTransaction();

        for (ScriptCommand command : commands) {
            execute(command);
        }

        connection.commitTransaction();
    }

    public ResultSet execute(ScriptCommand command) throws SQLException {
        return execute(command.getCommand());
    }

    public ResultSet execute(String command) throws SQLException {
        log.debug(command);

        try {
            return createStatement().execute(command);
        } catch (SQLException exception) {
            log.error("Error executing: {}. Error message: {}", command, exception.getMessage(), exception);
            connection.handleError(exception);
        }

        return new EmptyResultSet();
    }

    private Statement createStatement() {
        return new DefaultStatement(connection, resultObservers);
    }
}
