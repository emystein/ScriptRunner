package ar.com.kamikaze.persistence.jdbc;

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
	private final ScriptExecutor scriptExecutor;

	public ScriptRunner(Connection connection, boolean autoCommit, boolean stopOnError) throws SQLException {
		this.scriptExecutor = new ScriptExecutor(connection, autoCommit, stopOnError);
	}

	public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
		scriptExecutor.setDelimiter(delimiter, fullLineDelimiter);
	}

	public void runScript(String scriptPath) throws IOException, SQLException {
		scriptExecutor.runScript(scriptPath);
	}

}
