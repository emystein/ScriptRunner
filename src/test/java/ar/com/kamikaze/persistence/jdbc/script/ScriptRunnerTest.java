package ar.com.kamikaze.persistence.jdbc.script;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class ScriptRunnerTest {
	public static Collection<Object[]> runScriptDataProvider() {
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

	@ParameterizedTest
	@MethodSource("runScriptDataProvider")
	public void runScript(boolean connectionAutoCommit, boolean runnerAutoCommit, boolean runnerStopOnError) throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:h2:mem:test");
		connection.setAutoCommit(connectionAutoCommit);

		var scriptRunnerBuilder = ScriptRunnerBuilder.forConnection(connection);
		if (runnerAutoCommit) {
			scriptRunnerBuilder.autoCommit();
		}
		if (runnerStopOnError) {
			scriptRunnerBuilder.stopOnError();
		}
		ScriptRunner scriptRunner = scriptRunnerBuilder.build();
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
