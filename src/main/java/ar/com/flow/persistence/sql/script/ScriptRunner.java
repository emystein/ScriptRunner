package ar.com.flow.persistence.sql.script;

import ar.com.flow.persistence.jdbc.connection.Connection;
import ar.com.flow.persistence.jdbc.result.CommandResult;
import ar.com.flow.persistence.jdbc.result.PrintResultObserver;
import ar.com.flow.persistence.jdbc.result.ResultObserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Entry point for running SQL scripts.
 */
public class ScriptRunner {
	private final ScriptParser scriptParser = new ScriptParser();
	private final Connection connection;
	private List<ResultObserver> resultObservers = new ArrayList<>();

	public ScriptRunner(Connection connection) {
		this.connection = connection;
		this.addResultObserver(new PrintResultObserver());
	}

	public void addResultObserver(ResultObserver observer) {
		resultObservers.add(observer);
	}

	public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
		scriptParser.setDelimiter(delimiter, fullLineDelimiter);
	}

	public void runScript(String scriptPath) throws IOException, SQLException {
		runScript(new BufferedReader(new FileReader(scriptPath)));
	}

	public void runScript(Reader reader) throws IOException, SQLException {
		execute(scriptParser.parse(reader));
	}

	private void execute(List<LineCommand> commands) throws SQLException {
		connection.beginTransaction();

		var scriptCommands = commands.stream()
				.map(c -> new ScriptCommand(c.getLineNumber(), c.getCommand(), connection))
				.collect(toList());

		for (ScriptCommand command : scriptCommands) {
			var resultSet = command.execute();

			var commandResult = new CommandResult(command, resultSet);

			for (ResultObserver observer : resultObservers) {
				observer.handle(commandResult);
			}
		}

		connection.commitTransaction();
	}
}