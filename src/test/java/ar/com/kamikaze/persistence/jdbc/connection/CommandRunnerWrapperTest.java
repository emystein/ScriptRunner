package ar.com.kamikaze.persistence.jdbc.connection;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import ar.com.kamikaze.persistence.jdbc.result.NullResultSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandRunnerWrapperTest {
	private Connection wrappedConnection;
	private CommandRunner commandRunner;

	@BeforeEach
	public void setUp() throws Exception {
		wrappedConnection = DriverManager.getConnection("jdbc:h2:mem:test");
		commandRunner = new AutoCommitCommandRunner(wrappedConnection);

		Statement statement = wrappedConnection.createStatement();
		statement.execute("DROP TABLE IF EXISTS post;");
		statement.execute("DROP TABLE IF EXISTS author;");
		statement.execute("CREATE TABLE author(id INTEGER, name TEXT, PRIMARY KEY (id));");
		statement.execute("INSERT INTO author(id, name) VALUES(1, 'emenendez');");
	}

	@Test
	public void executingQueryOnExistingDataShouldReturnResultSet() throws Exception {
		ResultSet resultSet = commandRunner.execute("SELECT * FROM author");

		resultSet.next();
		assertThat(resultSet.getInt("id")).isEqualTo(1);
		assertThat(resultSet.getString("name")).isEqualTo("emenendez");
	}

	@Test
	public void executingInsertShouldReturnEmptyResultSet() throws Exception {
		ResultSet resultSet = commandRunner.execute("INSERT INTO author(id, name) VALUES(2, 'fbaron');");

		assertThat(resultSet).isInstanceOf(NullResultSet.class);
	}
}
