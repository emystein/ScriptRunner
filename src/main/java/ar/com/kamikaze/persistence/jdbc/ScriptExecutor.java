package ar.com.kamikaze.persistence.jdbc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

public class ScriptExecutor {
	protected ConnectionWrapper connection;
	private boolean autoCommit;
	private ScriptRunner scriptRunner;
	boolean originalAutoCommit;

	public ScriptExecutor(Connection connection, boolean autoCommit, ScriptRunner scriptRunner) throws SQLException {
		this.connection = autoCommit ? new AutoCommitConnection(connection) : new ManualCommitConnection(connection);
		this.autoCommit = autoCommit;
		this.scriptRunner = scriptRunner;
		this.originalAutoCommit = connection.getAutoCommit();
		this.connection.setAutoCommit(autoCommit);
	}

	public void runScript(String scriptPath) throws IOException, SQLException {
		runScript(new BufferedReader(new FileReader(scriptPath)));
	}

	public void runScript(Reader reader) throws IOException, SQLException {
		try {
			connection.setAutoCommit(autoCommit);
			scriptRunner.runScript(this.connection, reader);
		} finally {
			connection.setAutoCommit(originalAutoCommit);
		}
	}

}
