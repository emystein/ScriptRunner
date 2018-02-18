package ar.com.kamikaze.persistence.jdbc.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import ar.com.kamikaze.persistence.jdbc.script.MockLoggerTest;
import ar.com.kamikaze.persistence.jdbc.script.ScriptRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResultSetPrinterTest extends MockLoggerTest {
	private ResultSetPrinter resultSetPrinter;

	private static Connection connection;

	@BeforeClass
	public static void beforeClass() throws Exception {
		connection = DriverManager.getConnection("jdbc:h2:mem:test");
		ScriptRunner scriptRunner = new ScriptRunner(connection, true, true);
		scriptRunner.runScript("src/test/resources/schema.sql");
		scriptRunner.runScript("src/test/resources/insert-posts.sql");
	}

	@Before
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
