package ar.com.kamikaze.persistence.jdbc.script;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import ar.com.kamikaze.persistence.jdbc.script.ScriptRunner;

@RunWith(Parameterized.class)
public class ScriptRunnerTest {
	private Connection connection;

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ false, false, false},
				{ false, false, true },
				{ false, true, false},
				{ false, true, true },
				{ true, false, false},
				{ true, false, true },
				{ true, true, false },
				{ true, true, true }
		});
	}

	@Parameter
	public boolean connectionAutoCommit;
	@Parameter(1)
	public boolean runnerAutoCommit;
	@Parameter(2)
	public boolean runnerStopOnError;

	@Before
	public void setUp() throws Exception {
		connection = DriverManager.getConnection("jdbc:h2:mem:test");
		connection.setAutoCommit(connectionAutoCommit);
	}

	@Test
	public void runScript() throws Exception {
		ScriptRunner scriptRunner = new ScriptRunner(connection, runnerAutoCommit, runnerStopOnError);
		scriptRunner.runScript("src/test/resources/schema.sql");
		scriptRunner.runScript("src/test/resources/insert-posts.sql");

		Statement statement = connection.createStatement();

		// assertions
		statement.execute("SELECT post.title, author.name FROM post, author WHERE post.author_id = author.id ORDER BY post.title");
		ResultSet resultSet = statement.getResultSet();
		resultSet.next();
		assertThat(resultSet.getString("post.title")).isEqualTo("author 1 post 1");
		assertThat(resultSet.getString("author.name")).isEqualTo("emystein");
	}

}
