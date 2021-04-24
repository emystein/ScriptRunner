package ar.com.kamikaze.persistence.jdbc.script;

import ar.com.kamikaze.persistence.jdbc.connection.CommandRunnerBuilder;
import ar.com.kamikaze.persistence.jdbc.connection.Commands;
import ar.com.kamikaze.persistence.jdbc.result.PrintCommandResultListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Entry point for running SQL scripts.
 */
public class ScriptRunner {
	private Commands commandRunner;
	private final ScriptParser scriptParser = new ScriptParser();

	public ScriptRunner(Connection connection, boolean autoCommit, boolean stopOnError) throws SQLException {
		this.commandRunner = CommandRunnerBuilder.wrap(connection)
				.autoCommit(autoCommit)
				.stopOnError(stopOnError)
				.addCommandResultListener(new PrintCommandResultListener())
				.build();
	}

	public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
		scriptParser.setDelimiter(delimiter, fullLineDelimiter);
	}

	public void runScript(String scriptPath) throws IOException, SQLException {
		runScript(new BufferedReader(new FileReader(scriptPath)));
	}

	public void runScript(Reader reader) throws IOException, SQLException {
		List<ScriptCommand> commands = scriptParser.parse(reader);
		commandRunner.execute(commands);
	}
}
