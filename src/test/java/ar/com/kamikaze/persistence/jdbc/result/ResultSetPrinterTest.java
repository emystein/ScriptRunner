package ar.com.kamikaze.persistence.jdbc.result;

import ar.com.kamikaze.persistence.jdbc.script.MockLoggerTest;
import ar.com.kamikaze.persistence.jdbc.script.ScriptRunner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ResultSetPrinterTest extends MockLoggerTest {
	private ResultSetPrinter resultSetPrinter;

	private static Connection connection;

	@BeforeAll
	public static void beforeClass() throws Exception {
		connection = DriverManager.getConnection("jdbc:h2:mem:test");
		ScriptRunner scriptRunner = new ScriptRunner(connection, true, true);
		scriptRunner.runScript("src/test/resources/schema.sql");
		scriptRunner.runScript("src/test/resources/insert-posts.sql");
	}

	@BeforeEach
	public void setUp() throws Exception {
		super.setUp();
		resultSetPrinter = new ResultSetPrinter();
	}

	@Test
	public void printResultSet() throws SQLException {
		Statement statement = connection.createStatement();
		statement.execute("SELECT post.title, author.name as author FROM post, author WHERE post.author_id = author.id ORDER BY post.title");

		resultSetPrinter.print(statement.getResultSet());

		assertDebugMessages("TITLE\tAUTHOR","","author 1 post 1\temystein","author 1 post 2\temystein");
	}

	@Test
	public void printEmptyResultSet() throws SQLException {
		Statement statement = connection.createStatement();
		statement.execute("SELECT * FROM post WHERE author_id = -1");

		resultSetPrinter.print(statement.getResultSet());
	}

	@Test
	public void printNullResultSet() throws SQLException {
		resultSetPrinter.print(new NullResultSet());
	}

}
