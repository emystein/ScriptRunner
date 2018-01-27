import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tool to run database scripts
 */
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

	@SuppressWarnings("UseOfSystemOutOrSystemErr")
	private final PrintWriter logWriter;
	@SuppressWarnings("UseOfSystemOutOrSystemErr")
	private final PrintWriter errorLogWriter;

	private String delimiter = DEFAULT_DELIMITER;
	private boolean fullLineDelimiter = false;

	/**
	 * Default constructor
	 */
	public ScriptRunner(Connection connection, boolean autoCommit,
						boolean stopOnError) throws IOException {
		this.connection = connection;
		this.autoCommit = autoCommit;
		this.stopOnError = stopOnError;
		logWriter = createLogWriter("create_db.log");
		errorLogWriter = createLogWriter("create_db_error.log");
		String timeStamp = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss").format(new java.util.Date());
		logWriter.println("\n-------\n" + timeStamp + "\n-------\n");
		errorLogWriter.println("\n-------\n" + timeStamp + "\n-------\n");
	}

	public ScriptRunner(Connection connection, boolean autoCommit, boolean stopOnError, PrintWriter logWriter, PrintWriter errorLogWriter) throws IOException {
		this.connection = connection;
		this.autoCommit = autoCommit;
		this.stopOnError = stopOnError;
		this.logWriter = logWriter;
		this.errorLogWriter = errorLogWriter;
//		String timeStamp = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss").format(new java.util.Date());
//		logWriter.println("\n-------\n" + timeStamp + "\n-------\n");
//		errorLogWriter.println("\n-------\n" + timeStamp + "\n-------\n");
	}

	private PrintWriter createLogWriter(String logPath) throws IOException {
		File logFile = new File(logPath);
		return new PrintWriter(new FileWriter(logFile, logFile.exists()));
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
	private void runScript(Connection conn, Reader reader) throws IOException,
			SQLException {
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
					println(trimmedLine);
				} else if (trimmedLine.length() < 1 || trimmedLine.startsWith("--")) {
					// Do nothing
				} else if (!fullLineDelimiter && trimmedLine.endsWith(getDelimiter())
						|| fullLineDelimiter && trimmedLine.equals(getDelimiter())) {
					command.append(line.substring(0, line
							.lastIndexOf(getDelimiter())));
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
			if (!autoCommit) {
				conn.commit();
			}
		} catch (IOException e) {
			throw new IOException(String.format("Error executing '%s': %s", command, e.getMessage()), e);
		} finally {
			conn.rollback();
			flush();
		}
	}

	private void execCommand(Connection conn, StringBuffer command,
							 LineNumberReader lineReader) throws SQLException {
		Statement statement = conn.createStatement();

		println(command.toString());

		try {
			statement.execute(command.toString());
		} catch (SQLException e) {
			final String errText = String.format("Error executing '%s' (line %d): %s",
					command, lineReader.getLineNumber(), e.getMessage());
			printlnError(errText);
			if (stopOnError) {
				throw new SQLException(errText, e);
			}
		}

		// TODO: this can be deleted, assuming the execCommand command method is always called from runScript which sets autoCommit accordingly
		if (autoCommit && !conn.getAutoCommit()) {
			conn.commit();
		}


		ResultSetPrinter resultSetPrinter = new ResultSetPrinter(statement.getResultSet(), logWriter);
		resultSetPrinter.print();

		try {
			statement.close();
		} catch (Exception e) {
			// Ignore to workaround a bug in Jakarta DBCP
		}
	}

	private String getDelimiter() {
		return delimiter;
	}

	@SuppressWarnings("UseOfSystemOutOrSystemErr")

	private void print(String o) {
		if (logWriter != null) {
			logWriter.print(o);
		}
	}

	private void println(String o) {
		if (logWriter != null) {
			logWriter.println(o);
		}
	}

	private void printlnError(String o) {
		if (errorLogWriter != null) {
			errorLogWriter.println(o);
		}
	}

	private void flush() {
		if (logWriter != null) {
			logWriter.flush();
		}
		if (errorLogWriter != null) {
			errorLogWriter.flush();
		}
	}
}
