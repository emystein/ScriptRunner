package ar.com.kamikaze.persistence.jdbc;

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

	private final ScriptExecutor scriptExecutor;
	private final boolean stopOnError;

	private String delimiter = DEFAULT_DELIMITER;
	private boolean fullLineDelimiter = false;

	public ScriptRunner(Connection connection, boolean autoCommit, boolean stopOnError) throws SQLException {
		this.scriptExecutor = new ScriptExecutor(connection, autoCommit, this);
		this.stopOnError = stopOnError;
	}


	public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
		this.delimiter = delimiter;
		this.fullLineDelimiter = fullLineDelimiter;
	}

	public void runScript(String scriptPath) throws IOException, SQLException {
		scriptExecutor.runScript(scriptPath);
	}

	/**
	 * Runs an SQL script (read in using the Reader parameter) using the
	 * scriptExecutor passed in
	 *
	 * @param conn   - the scriptExecutor to use for the script
	 * @param reader - the source of the script
	 * @throws SQLException if any SQL errors occur
	 * @throws IOException  if there is an error reading from the Reader
	 */
	public void runScript(ConnectionWrapper conn, Reader reader) throws IOException, SQLException {
		LineNumberReader lineReader = new LineNumberReader(reader);
		CommandExecutor commandExecutor = new CommandExecutor(conn, new ResultSetPrinter());
		StringBuffer command = null;
		
		try {
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
					commandExecutor.execute(command.toString());
					command = null;
				} else {
					command.append(line);
					command.append("\n");
				}
			}
			if (command != null) {
				commandExecutor.execute(command.toString());
			}
			conn.commit();
		} catch (SQLException e) {
			log.error("Error executing '{}' (line {}): {}", command, lineReader.getLineNumber(), e.getMessage());
			if (stopOnError) {
				throw e;
			}
		} catch (IOException e) {
			throw new IOException(String.format("Error executing '%s': %s", command, e.getMessage()), e);
		} finally {
			conn.rollback();
		}
	}


}
