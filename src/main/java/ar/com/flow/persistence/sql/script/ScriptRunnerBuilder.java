package ar.com.flow.persistence.sql.script;

import ar.com.flow.persistence.jdbc.commit.AutoCommitStrategy;
import ar.com.flow.persistence.jdbc.commit.ManualCommitStrategy;
import ar.com.flow.persistence.jdbc.error.ContinueExecution;
import ar.com.flow.persistence.jdbc.error.Rollback;

import java.sql.Connection;
import java.sql.SQLException;

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
		var commitStrategy = autoCommit ? new AutoCommitStrategy() : new ManualCommitStrategy();
		var errorHandler = stopOnError ? new Rollback(connection) : new ContinueExecution();
		return ScriptRunner.create(connection, commitStrategy, errorHandler);
	}
}
