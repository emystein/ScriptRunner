package ar.com.kamikaze.persistence.jdbc.script;

import ar.com.kamikaze.persistence.jdbc.commands.CommandRunnerBuilder;

import java.sql.Connection;
import java.sql.SQLException;

public class ScriptRunnerBuilder {
	private CommandRunnerBuilder commandRunnerBuilder;

	public static ScriptRunnerBuilder forConnection(Connection connection) {
		return new ScriptRunnerBuilder(connection);
	}

	private ScriptRunnerBuilder(Connection connection) {
		this.commandRunnerBuilder = CommandRunnerBuilder.wrap(connection);
	}

	public ScriptRunnerBuilder autoCommit() {
		this.commandRunnerBuilder.autoCommit();
		return this;
	}

	public ScriptRunnerBuilder stopOnError() {
		this.commandRunnerBuilder.stopOnError();
		return this;
	}

	public ScriptRunner build() throws SQLException {
		return new ScriptRunner(commandRunnerBuilder.build());
	}
}
