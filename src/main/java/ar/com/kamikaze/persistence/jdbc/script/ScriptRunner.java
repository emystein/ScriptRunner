package ar.com.kamikaze.persistence.jdbc.script;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import ar.com.kamikaze.persistence.jdbc.connection.AutoCommitConnection;
import ar.com.kamikaze.persistence.jdbc.connection.ConnectionWrapper;
import ar.com.kamikaze.persistence.jdbc.connection.ManualCommitConnection;
import ar.com.kamikaze.persistence.jdbc.error.ContinueExecutionErrorHandler;
import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;
import ar.com.kamikaze.persistence.jdbc.error.RollbackTransactionErrorHandler;
import ar.com.kamikaze.persistence.jdbc.result.PrintCommandResultListener;
import lombok.extern.slf4j.Slf4j;

/**
 * Entry point for running SQL scripts.
 */
@Slf4j
public class ScriptRunner {
	private ConnectionWrapper connection;
	private final ScriptParser scriptParser = new ScriptParser();

	public ScriptRunner(Connection connection, boolean autoCommit, boolean stopOnError) throws SQLException {
		this.connection = autoCommit ? new AutoCommitConnection(connection) : new ManualCommitConnection(connection);
		ErrorHandler errorHandler = stopOnError ? new RollbackTransactionErrorHandler(this.connection) : new ContinueExecutionErrorHandler();
		this.connection.setErrorHandler(errorHandler);
		this.connection.addCommandResultListener(new PrintCommandResultListener());
	}

	public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
		scriptParser.setDelimiter(delimiter, fullLineDelimiter);
	}

	public void runScript(String scriptPath) throws IOException, SQLException {
		runScript(new BufferedReader(new FileReader(scriptPath)));
	}

	public void runScript(Reader reader) throws IOException, SQLException {
		List<ScriptCommand> commands = scriptParser.parse(reader);
		connection.run(commands);
	}

}
