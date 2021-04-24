package ar.com.kamikaze.persistence.jdbc.result;

import ar.com.kamikaze.persistence.jdbc.script.ScriptRunner;
import ar.com.kamikaze.persistence.jdbc.script.ScriptRunnerBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

public class ResultSetCurrentRowTest {
	private static Connection connection;

	@BeforeAll
	public static void beforeClass() throws Exception {
		connection = DriverManager.getConnection("jdbc:h2:mem:test");
		ScriptRunner scriptRunner = ScriptRunnerBuilder
				.forConnection(connection)
				.autoCommit()
				.stopOnError()
				.build();
		scriptRunner.runScript("src/test/resources/schema.sql");
		scriptRunner.runScript("src/test/resources/insert-posts.sql");
	}

	@Test
	public void getValues() throws SQLException {
		Statement statement = connection.createStatement();
		statement.execute("SELECT id, title, author_id FROM post WHERE id = 1");
		ResultSet resultSet = statement.getResultSet();
		resultSet.next();

		ResultSetCurrentRow row = new ResultSetCurrentRow(resultSet);
		List<String> values = row.getValues();
		Assertions.assertThat(values.get(0)).isEqualTo("1");
		Assertions.assertThat(values.get(1)).isEqualTo("author 1 post 1");
		Assertions.assertThat(values.get(2)).isEqualTo("1");
	}

}