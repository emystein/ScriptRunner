package ar.com.kamikaze.persistence.jdbc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * Entry point for running SQL scripts.
 */
@Slf4j
public class ScriptRunner {
	protected ConnectionWrapper connection;
	private boolean autoCommit;
	boolean originalAutoCommit;
	private boolean stopOnError;
	private final ScriptParser scriptParser = new ScriptParser();

	public ScriptRunner(Connection connection, boolean autoCommit, boolean stopOnError) throws SQLException {
		this.connection = autoCommit ? new AutoCommitConnection(connection) : new ManualCommitConnection(connection);
		this.autoCommit = autoCommit;
		this.originalAutoCommit = connection.getAutoCommit();
		this.connection.setAutoCommit(autoCommit);
		this.stopOnError = stopOnError;
	}

	public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
		scriptParser.setDelimiter(delimiter, fullLineDelimiter);
	}

	public void runScript(String scriptPath) throws IOException, SQLException {
		runScript(new BufferedReader(new FileReader(scriptPath)));
	}

	public void runScript(Reader reader) throws IOException, SQLException {
		try {
			connection.setAutoCommit(autoCommit);
			List<ScriptCommand> commands = scriptParser.parse(reader);
			run(commands);
		} finally {
			connection.setAutoCommit(originalAutoCommit);
		}
	}

	private void run(List<ScriptCommand> commands) throws SQLException {
		ResultSetPrinter resultSetPrinter = new ResultSetPrinter();

		for (ScriptCommand command : commands) {
			try {
				ResultSet resultSet = connection.execute(command);
				resultSetPrinter.print(resultSet);
			} catch (SQLException e) {
				log.error(e.getMessage(), e);

				if (stopOnError) {
					connection.rollback();
					throw e;
				}
			}
		}

		connection.commit();
	}
}
