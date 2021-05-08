package ar.com.flow.persistence.sql.script;

import ar.com.flow.persistence.jdbc.connection.Connection;
import ar.com.flow.persistence.jdbc.result.CommandResult;
import ar.com.flow.persistence.jdbc.result.ResultObserver;
import lombok.RequiredArgsConstructor;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Entry point for running SQL scripts.
 */
@RequiredArgsConstructor
public class ScriptRunner {
    private final ScriptParser scriptParser = new ScriptParser();
    private final Connection connection;
    private List<ResultObserver> commandResultObservers = new ArrayList<>();
    private int executedCommandCount;

    public void addResultObserver(ResultObserver observer) {
        commandResultObservers.add(observer);
    }

    public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
        scriptParser.setDelimiter(delimiter, fullLineDelimiter);
    }

    public void runScript(String scriptPath) throws IOException, SQLException {
        var commands = scriptParser.parse(new FileReader(scriptPath)).stream()
                .map(lineCommand -> new ScriptCommand(lineCommand.getCommand(), connection))
                .collect(toList());
        execute(commands);
    }

    private void execute(List<ScriptCommand> commands) throws SQLException {
        executedCommandCount = commands.size();

        connection.beginTransaction();

        for (ScriptCommand command : commands) {
            var resultSet = command.execute();

            var commandResult = new CommandResult(command, resultSet);

            for (ResultObserver observer : commandResultObservers) {
                observer.handle(commandResult);
            }
        }

        connection.commitTransaction();
    }

    public int executedCommandCount() {
        return executedCommandCount;
    }
}
