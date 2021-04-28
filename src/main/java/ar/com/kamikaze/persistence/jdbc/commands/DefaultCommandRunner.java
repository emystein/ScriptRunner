package ar.com.kamikaze.persistence.jdbc.commands;

import ar.com.kamikaze.persistence.jdbc.connection.Connection;
import ar.com.kamikaze.persistence.jdbc.result.*;
import ar.com.kamikaze.persistence.jdbc.script.ScriptCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class DefaultCommandRunner implements CommandRunner {
    private final Connection connection;
    private List<ResultObserver> resultObservers = new ArrayList<>();

    @Override
    public void addResultObserver(ResultObserver eventListener) {
        resultObservers.add(eventListener);
    }

    @Override
    public void execute(List<ScriptCommand> commands) throws SQLException {
        connection.setUpExecution();

        for (ScriptCommand command : commands) {
            execute(command);
        }

        connection.commit();

        connection.endExecution();
    }

    @Override
    public ResultSet execute(ScriptCommand command) throws SQLException {
        return execute(command.getCommand());
    }

    @Override
    public ResultSet execute(String command) throws SQLException {
        log.debug(command);

        ResultSet resultSet = new EmptyResultSet();

        try {
            Statement statement = connection.createStatement();

            statement.execute(command);

            resultSet = statement.getResultSet() == null ? new EmptyResultSet() : new DefaultResultSet(statement.getResultSet());

            triggerCommandResultEvent(command, resultSet);
        } catch (SQLException e) {
            log.error("Error executing {}: {}", command, e.getMessage(), e);
            connection.handleError(e);
        }

        return resultSet;
    }

    private void triggerCommandResultEvent(String command, ResultSet resultSet) throws SQLException {
        var commandResult = new CommandResult(command, resultSet);

        for (ResultObserver eventListener : resultObservers) {
            eventListener.handle(commandResult);
        }
    }
}
