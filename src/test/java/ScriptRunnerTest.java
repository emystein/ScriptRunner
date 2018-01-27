import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Before;
import org.junit.Test;

public class ScriptRunnerTest {
	private Connection connection;

	@Before
	public void setUp() throws Exception {
		connection = DriverManager.getConnection("jdbc:h2:mem:test");
	}

	@Test
	public void runScript() throws Exception {
		ScriptRunner scriptRunner = new ScriptRunner(connection, true, true);
		scriptRunner.runScript("src/test/resources/schema.sql");

		Statement statement = connection.createStatement();
		statement.execute("INSERT INTO author(id, name) VALUES(1, 'emystein');");
		statement.execute("INSERT INTO post(title, author_id) VALUES('post 1', 1);");

		// assertions
		statement.execute("SELECT post.title, author.name FROM post, author WHERE post.author_id = author.id");
		ResultSet resultSet = statement.getResultSet();
		resultSet.next();
		assertThat(resultSet.getString("post.title")).isEqualTo("post 1");
		assertThat(resultSet.getString("author.name")).isEqualTo("emystein");
	}

	@Test
	public void createLogFiles() {
		File logFile = new File("create_db.log");
		assertThat(logFile.delete());

		File errorLogFile = new File("create_db_error.log");
		assertThat(errorLogFile.delete());

		// exercise
		ScriptRunner scriptRunner = new ScriptRunner(connection, true, true);

		assertThat(logFile.exists());
		assertThat(errorLogFile.exists());
	}
}
