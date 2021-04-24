package ar.com.kamikaze.persistence.jdbc.script;

import ar.com.kamikaze.persistence.jdbc.commands.CommandRunner;

import java.sql.Connection;
import java.sql.SQLException;

import static ar.com.kamikaze.persistence.jdbc.commands.CommandRunnerFactory.createAutoCommitCommandRunner;
import static ar.com.kamikaze.persistence.jdbc.commands.CommandRunnerFactory.createManualCommitCommandRunner;

public class ScriptRunnerBuilder {
	private final Connection connection;
	private boolean autoCommit;
	private boolean stopOnError;

	public static ScriptRunnerBuilder forConnection(Connection connection) {
		return new ScriptRunnerBuilder(connection);
	}

	private ScriptRunnerBuilder(Connection connection) {
		this.connection = connection;
	}

	public ScriptRunnerBuilder autoCommit() {
		this.autoCommit = true;
		return this;
	}

	public ScriptRunnerBuilder stopOnError() {
		this.stopOnError = true;
		return this;
	}

	public ScriptRunner build() throws SQLException {
		return new ScriptRunner(commandRunner());
	}

	private CommandRunner commandRunner() throws SQLException {
		CommandRunner commandRunner = autoCommit ?
				createAutoCommitCommandRunner(connection, stopOnError) :
				createManualCommitCommandRunner(connection, stopOnError);

		return commandRunner;
	}
}
