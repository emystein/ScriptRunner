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

@Slf4j
public class ScriptRunner {
	private final ScriptParser scriptParser = new ScriptParser();
	private final ScriptExecutor scriptExecutor;
	private final boolean stopOnError;

	public ScriptRunner(Connection connection, boolean autoCommit, boolean stopOnError) throws SQLException {
		this.scriptExecutor = new ScriptExecutor(connection, autoCommit, this);
		this.stopOnError = stopOnError;
	}

	public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
		scriptParser.setDelimiter(delimiter, fullLineDelimiter);
	}

	public void runScript(String scriptPath) throws IOException, SQLException {
		scriptExecutor.runScript(scriptPath);
	}

	public void runScript(ConnectionWrapper conn, Reader reader) throws IOException, SQLException {
		List<ScriptCommand> commands = scriptParser.parse(reader);

		ResultSetPrinter resultSetPrinter = new ResultSetPrinter();

		ScriptCommand cmd = new NullScriptCommand();

		try {
			for (int i = 0; i < commands.size(); i++) {
				cmd = commands.get(i);
				ResultSet resultSet = conn.execute(cmd.getCommand());
				resultSetPrinter.print(resultSet);
			}

			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			log.error("Error executing '{}' (line {}): {}", cmd.getCommand(), cmd.getLineNumber(), e.getMessage());
			if (stopOnError) {
				throw e;
			}
		}
	}

}
