package ar.com.kamikaze.persistence.sql.script;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class QueryResultScriptRunnerTest extends MockLoggerTest {
	private Connection connection;
	private ScriptRunner setupScriptRunner;

	@BeforeEach
	public void setUp() throws Exception {
		connection = DriverManager.getConnection("jdbc:h2:mem:test");
		setupScriptRunner = ScriptRunnerBuilder.forConnection(connection).autoCommit().build();
		setupScriptRunner.runScript("src/test/resources/schema.sql");
		setupScriptRunner.runScript("src/test/resources/insert-posts.sql");
		// this needs to be after executing the setup SQL in order to avoid the logger mock capturing log messages not being tested
		super.setUp();
	}

	@AfterEach
	public void tearDown() throws Exception {
		super.tearDown();
		setupScriptRunner.runScript("src/test/resources/drop-schema.sql");
	}

	@Test
	public void printQueryResult() throws IOException, SQLException {
		ScriptRunner scriptRunner = ScriptRunnerBuilder.forConnection(connection).build();

		scriptRunner.runScript("src/test/resources/select-posts.sql");

		// ScriptRunner adds a space at the end of the statement read from the .sql file
		assertDebugMessages("SELECT post.title, author.name as author FROM post, author WHERE post.author_id = author.id ORDER BY post.title ", "TITLE\tAUTHOR\n", "author 1 post 1\temystein","author 1 post 2\temystein");
	}
}
