package ar.com.kamikaze.persistence.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.Before;
import org.junit.Test;

public class ConnectionWrapperTest {
	private Connection connection;
	private ConnectionWrapper connectionWrapper;

	@Before
	public void setUp() throws Exception {
		connection = DriverManager.getConnection("jdbc:h2:mem:test");
		connectionWrapper = new ManualCommitConnection(connection, false);

		Statement statement = connection.createStatement();
		statement.execute("DROP TABLE IF EXISTS author;");
		statement.execute("CREATE TABLE author(id INTEGER, name TEXT, PRIMARY KEY (id));");
		statement.execute("INSERT INTO author(id, name) VALUES(1, 'emenendez');");
	}

	@Test
	public void executingQueryOnExistingDataShouldReturnResultSet() throws Exception {
		ResultSet resultSet = connectionWrapper.execute("SELECT * FROM author");

		resultSet.next();
		assertThat(resultSet.getInt("id")).isEqualTo(1);
		assertThat(resultSet.getString("name")).isEqualTo("emenendez");
	}

	@Test
	public void executingInsertShouldReturnEmptyResultSet() throws Exception {
		ResultSet resultSet = connectionWrapper.execute("INSERT INTO author(id, name) VALUES(2, 'fbaron');");

		assertThat(resultSet).isInstanceOf(NullResultSet.class);
	}
}
