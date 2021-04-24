package ar.com.kamikaze.persistence.jdbc.commands;

import ar.com.kamikaze.persistence.jdbc.connection.ConnectionControl;
import ar.com.kamikaze.persistence.jdbc.result.CommandResult;
import ar.com.kamikaze.persistence.jdbc.result.CommandResultListener;
import ar.com.kamikaze.persistence.jdbc.result.NullResultSet;
import ar.com.kamikaze.persistence.jdbc.script.ScriptCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CommandExecutor implements CommandRunner {
    private final ConnectionControl connection;
    private List<CommandResultListener> commandResultListeners = new ArrayList<>();

    @Override
    public void addCommandResultListener(CommandResultListener eventListener) {
        commandResultListeners.add(eventListener);
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

        ResultSet resultSet = new NullResultSet();

        try {
            Statement statement = connection.createStatement();

            statement.execute(command);

            resultSet = Optional.ofNullable(statement.getResultSet()).orElse(new NullResultSet());
        } catch (SQLException e) {
            log.error("Error executing " + command + ": " + e.getMessage(), e);
            connection.handleError(e);
        }

        triggerCommandResultEvent(command, resultSet);

        return resultSet;
    }

    private void triggerCommandResultEvent(String command, ResultSet resultSet) throws SQLException {
        CommandResult commandResult = new CommandResult(command, resultSet);

        for (CommandResultListener eventListener : commandResultListeners) {
            eventListener.handle(commandResult);
        }
    }
}
