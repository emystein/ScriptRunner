package ar.com.flow.persistence.sql.script;

import ar.com.flow.persistence.jdbc.commit.AutoCommitStrategy;
import ar.com.flow.persistence.jdbc.commit.ManualCommitStrategy;
import ar.com.flow.persistence.jdbc.connection.DefaultConnection;
import ar.com.flow.persistence.jdbc.error.ContinueExecution;
import ar.com.flow.persistence.jdbc.error.Rollback;
import ar.com.flow.persistence.jdbc.result.PrintResultObserver;

import java.sql.Connection;
import java.sql.SQLException;

public class ScriptRunnerBuilder {
	private final Connection connection;
	private boolean autoCommit;
	private boolean rollbackOnError;

	public static ScriptRunnerBuilder forConnection(Connection connection) {
		return new ScriptRunnerBuilder(connection);
	}

	private ScriptRunnerBuilder(Connection connection) {
		this.connection = connection;
	}

	public ScriptRunnerBuilder autoCommit() {
		return autoCommit(true);
	}

	public ScriptRunnerBuilder autoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
		return this;
	}

	public ScriptRunnerBuilder rollbackOnError() {
		return rollbackOnError(true);
	}

	public ScriptRunnerBuilder rollbackOnError(boolean rollbackOnError) {
		this.rollbackOnError = rollbackOnError;
		return this;
	}

	public ScriptRunner build() throws SQLException {
		var commitStrategy = autoCommit ? new AutoCommitStrategy() : new ManualCommitStrategy();
		var errorHandler = rollbackOnError ? new Rollback(connection) : new ContinueExecution();
		var scriptRunner = new ScriptRunner(new DefaultConnection(connection, commitStrategy, errorHandler));
		scriptRunner.addResultObserver(new PrintResultObserver());
		return scriptRunner;
	}
}
