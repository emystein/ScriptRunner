package ar.com.kamikaze.persistence.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import ar.com.kamikaze.persistence.jdbc.ResultSetCurrentRow;
import ar.com.kamikaze.persistence.jdbc.ScriptRunner;

public class ResultSetCurrentRowTest {
	private static Connection connection;

	@BeforeClass
	public static void beforeClass() throws Exception {
		connection = DriverManager.getConnection("jdbc:h2:mem:test");
		ScriptRunner scriptRunner = new ScriptRunner(connection, true, true);
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