import static org.mockito.Mockito.times;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryResultPrintTest {
	private Connection connection;
	private ScriptRunner setupScriptRunner;
	@Mock
	private PrintWriter logWriter;
	@Mock
	private PrintWriter errorLogWriter;

	@Before
	public void setUp() throws Exception {
		connection = DriverManager.getConnection("jdbc:h2:mem:test");
		setupScriptRunner = new ScriptRunner(connection, true, true);
		setupScriptRunner.runScript("src/test/resources/schema.sql");
		setupScriptRunner.runScript("src/test/resources/insert-posts.sql");
	}

	@After
	public void tearDown() throws Exception {
		setupScriptRunner.runScript("src/test/resources/drop-schema.sql");
	}

	@Test
	public void printQueryResult() throws IOException, SQLException {
		ScriptRunner scriptRunner = new ScriptRunner(connection, true, true, logWriter, errorLogWriter);
		scriptRunner.runScript("src/test/resources/select-posts.sql");

		// ScriptRunner adds a space at the end of the statement read from the .sql file
		Mockito.verify(logWriter).println("SELECT post.title, author.name as author FROM post, author WHERE post.author_id = author.id ORDER BY post.title ");
		Mockito.verify(logWriter).println("");
		Mockito.verify(logWriter).println("TITLE\tAUTHOR");
		Mockito.verify(logWriter).println("author 1 post 1\temystein");
		Mockito.verify(logWriter).println("author 1 post 2\temystein");
	}
}
