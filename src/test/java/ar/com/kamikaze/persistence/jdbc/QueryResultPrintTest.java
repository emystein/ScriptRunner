package ar.com.kamikaze.persistence.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryResultPrintTest extends MockLoggerTest {
	private Connection connection;
	private ScriptRunner setupScriptRunner;

	@Before
	public void setUp() throws Exception {
		connection = DriverManager.getConnection("jdbc:h2:mem:test");
		setupScriptRunner = new ScriptRunner(connection, true, true);
		setupScriptRunner.runScript("src/test/resources/schema.sql");
		setupScriptRunner.runScript("src/test/resources/insert-posts.sql");
		// this needs to be after executing the setup SQL in order to avoid the logger mock capturing log messages not being tested
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		setupScriptRunner.runScript("src/test/resources/drop-schema.sql");
	}

	@Test
	public void printQueryResult() throws IOException, SQLException {
		ScriptRunner scriptRunner = new ScriptRunner(connection, true, true);
		scriptRunner.runScript("src/test/resources/select-posts.sql");

		// ar.com.kamikaze.persistence.jdbc.ScriptRunner adds a space at the end of the statement read from the .sql file
		assertDebugMessages("SELECT post.title, author.name as author FROM post, author WHERE post.author_id = author.id ORDER BY post.title ", "TITLE\tAUTHOR", "", "author 1 post 1\temystein","author 1 post 2\temystein");
	}
}
