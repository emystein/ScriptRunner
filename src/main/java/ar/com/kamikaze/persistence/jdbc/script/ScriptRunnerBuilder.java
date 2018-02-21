package ar.com.kamikaze.persistence.jdbc.script;

import java.sql.Connection;
import java.sql.SQLException;

public class ScriptRunnerBuilder {
	private Connection connection;
	private boolean autoCommit;
	private boolean stopOnError;

	private ScriptRunnerBuilder(Connection connection) {
		this.connection = connection;
	}

	public static ScriptRunnerBuilder forConnection(Connection connection) {
		return new ScriptRunnerBuilder(connection);
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
		return new ScriptRunner(connection, autoCommit, stopOnError);
	}
}
