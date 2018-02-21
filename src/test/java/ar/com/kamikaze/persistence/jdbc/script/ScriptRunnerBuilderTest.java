package ar.com.kamikaze.persistence.jdbc.script;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScriptRunnerBuilderTest {
	@Mock
	private Connection connection;
	@Mock
	private Statement statement;
	private String scriptPath = "src/test/resources/schema.sql";

	@Before
	public void setUp() throws Exception {
		Mockito.when(connection.getAutoCommit()).thenReturn(false);
		Mockito.when(connection.createStatement()).thenReturn(statement);
	}

	@Test
	public void createScriptRunnerWithDefaults() throws SQLException {
		ScriptRunner scriptRunner = ScriptRunnerBuilder.forConnection(connection).build();

		assertThat(scriptRunner).isNotNull();
	}

	@Test
	public void givenCreatedScriptRunnerWithNoAutoCommitWhenRunningScriptThenScriptRunnerShouldNotCommit() throws Exception {
		ScriptRunner scriptRunner = ScriptRunnerBuilder.forConnection(connection).build();

		scriptRunner.runScript(scriptPath);

		Mockito.verify(connection, never()).commit();
	}

	@Test
	public void givenCreatedScriptRunnerWithAutoCommitWhenRunningScriptThenScriptRunnerShouldCommit() throws Exception {
		ScriptRunner scriptRunner = ScriptRunnerBuilder.forConnection(connection).autoCommit().build();

		scriptRunner.runScript(scriptPath);

		Mockito.verify(connection).commit();
	}

	@Test(expected = SQLException.class)
	public void givenCreatedScriptRunnerWithStopOnErrorWhenRunningScriptThenScriptRunnerShouldThrowException() throws Exception {
		expectScriptStatementThrowException();

		ScriptRunner scriptRunner = ScriptRunnerBuilder.forConnection(connection).stopOnError().build();

		scriptRunner.runScript(scriptPath);
	}

	@Test
	public void givenCreatedScriptRunnerWithoutStopOnErrorWhenRunningScriptThenScriptRunnerShouldNotThrowException() throws Exception {
		expectScriptStatementThrowException();

		ScriptRunner scriptRunner = ScriptRunnerBuilder.forConnection(connection).build();

		scriptRunner.runScript(scriptPath);
	}

	private void expectScriptStatementThrowException() throws SQLException {
		Mockito.when(statement.execute("DROP TABLE IF EXISTS post ")).thenThrow(SQLException.class);
	}

}
