package ar.com.kamikaze.persistence.jdbc.script;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class ScriptRunnerBuilderTest {
	@Mock
	private Connection connection;
	@Mock
	private Statement statement;
	private String scriptPath = "src/test/resources/schema.sql";

	@BeforeEach
	public void setUp() throws Exception {
		Mockito.when(connection.getAutoCommit()).thenReturn(false);
		Mockito.when(connection.createStatement()).thenReturn(statement);
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

	@Test
	public void givenCreatedScriptRunnerWithStopOnErrorWhenRunningScriptThenScriptRunnerShouldThrowException() throws Exception {
		expectScriptStatementThrowException();

		ScriptRunner scriptRunner = ScriptRunnerBuilder.forConnection(connection).stopOnError().build();

		assertThrows(SQLException.class, () -> scriptRunner.runScript(scriptPath));
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
