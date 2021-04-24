package ar.com.kamikaze.persistence.jdbc.script;

import ar.com.kamikaze.persistence.jdbc.commands.CommandRunner;
import ar.com.kamikaze.persistence.jdbc.result.PrintResultObserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.List;

/**
 * Entry point for running SQL scripts.
 */
public class ScriptRunner {
	private final ScriptParser scriptParser = new ScriptParser();
	private CommandRunner commandRunner;

	public ScriptRunner(CommandRunner commandRunner) {
	    this.commandRunner = commandRunner;
	    this.commandRunner.addResultObserver(new PrintResultObserver());
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
