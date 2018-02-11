package ar.com.kamikaze.persistence.jdbc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScriptRunner {

	private static final String DEFAULT_DELIMITER = ";";
	/**
	 * regex to detect delimiter.
	 * ignores spaces, allows delimiter in comment, allows an equals-sign
	 */
	public static final Pattern delimP = Pattern.compile("^\\s*(--)?\\s*delimiter\\s*=?\\s*([^\\s]+)+\\s*.*$", Pattern.CASE_INSENSITIVE);

	private final Connection connection;
	private final boolean stopOnError;
	private final boolean autoCommit;

	private String delimiter = DEFAULT_DELIMITER;
	private boolean fullLineDelimiter = false;
	private ResultSetPrinter resultSetPrinter;

	public ScriptRunner(Connection connection, boolean autoCommit, boolean stopOnError) {
		this.connection = connection;
		this.autoCommit = autoCommit;
		this.stopOnError = stopOnError;
		resultSetPrinter = new ResultSetPrinter();
	}


	public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
		this.delimiter = delimiter;
		this.fullLineDelimiter = fullLineDelimiter;
	}

	public void runScript(String scriptPath) throws IOException, SQLException {
		runScript(new BufferedReader(new FileReader(scriptPath)));
	}

	/**
	 * Runs an SQL script (read in using the Reader parameter)
	 *
	 * @param reader - the source of the script
	 */
	public void runScript(Reader reader) throws IOException, SQLException {
		boolean originalAutoCommit = connection.getAutoCommit();

		try {
			connection.setAutoCommit(this.autoCommit);
			runScript(connection, reader);
		} finally {
			connection.setAutoCommit(originalAutoCommit);
		}
	}

	/**
	 * Runs an SQL script (read in using the Reader parameter) using the
	 * connection passed in
	 *
	 * @param conn   - the connection to use for the script
	 * @param reader - the source of the script
	 * @throws SQLException if any SQL errors occur
	 * @throws IOException  if there is an error reading from the Reader
	 */
	private void runScript(Connection conn, Reader reader) throws IOException, SQLException {
		StringBuffer command = null;
		try {
			LineNumberReader lineReader = new LineNumberReader(reader);
			String line;
			while ((line = lineReader.readLine()) != null) {
				if (command == null) {
					command = new StringBuffer();
				}
				String trimmedLine = line.trim();
				final Matcher delimMatch = delimP.matcher(trimmedLine);
				if (trimmedLine.length() < 1 || trimmedLine.startsWith("//")) {
					// Do nothing
				} else if (delimMatch.matches()) {
					setDelimiter(delimMatch.group(2), false);
				} else if (trimmedLine.startsWith("--")) {
					log.debug(trimmedLine);
				} else if (trimmedLine.length() < 1 || trimmedLine.startsWith("--")) {
					// Do nothing
				} else if (!fullLineDelimiter && trimmedLine.endsWith(delimiter)
						|| fullLineDelimiter && trimmedLine.equals(delimiter)) {
					command.append(line.substring(0, line
							.lastIndexOf(delimiter)));
					command.append(" ");
					this.execCommand(conn, command, lineReader);
					command = null;
				} else {
					command.append(line);
					command.append("\n");
				}
			}
			if (command != null) {
				this.execCommand(conn, command, lineReader);
			}
			if (!conn.getAutoCommit()) {
				conn.commit();
			}
		} catch (IOException e) {
			throw new IOException(String.format("Error executing '%s': %s", command, e.getMessage()), e);
		} finally {
			conn.rollback();
		}
	}

	private void execCommand(Connection conn, StringBuffer command, LineNumberReader lineReader) throws SQLException {
		Statement statement = conn.createStatement();

		log.debug(command.toString());

		try {
			statement.execute(command.toString());
		} catch (SQLException e) {
			log.error("Error executing '{}' (line {}): {}", command, lineReader.getLineNumber(), e.getMessage());
			if (stopOnError) {
				throw e;
			}
		}

		if (!conn.getAutoCommit()) {
			conn.commit();
		}

		ResultSet resultSet = Optional.ofNullable(statement.getResultSet()).orElse(new NullResultSet());

		resultSetPrinter.print(resultSet);

		statement.close();
	}

}
