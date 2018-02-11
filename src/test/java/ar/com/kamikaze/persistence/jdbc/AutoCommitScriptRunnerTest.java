package ar.com.kamikaze.persistence.jdbc;

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
public class AutoCommitScriptRunnerTest {
	private ScriptRunner scriptRunner;
	@Mock
	private Connection connection;
	@Mock
	private Statement statement;
	private String scriptPath = "src/test/resources/schema.sql";

	@Before
	public void setUp() throws Exception {
		Mockito.when(connection.createStatement()).thenReturn(statement);
	}

	@Test
	public void whenAutoCommitIsOffThenConnectionShouldExecuteCommit() throws Exception {
		scriptRunner = createScriptRunnerWithAutoCommitSetTo(false);

		scriptRunner.runScript(scriptPath);

		Mockito.verify(connection, Mockito.atLeastOnce()).commit();
	}

	@Test
	public void whenAutoCommitIsOnThenConnectionShouldNotExecuteCommit() throws Exception {
		scriptRunner = createScriptRunnerWithAutoCommitSetTo(true);

		scriptRunner.runScript(scriptPath);

		Mockito.verify(connection, Mockito.never()).commit();
	}

	private ScriptRunner createScriptRunnerWithAutoCommitSetTo(boolean autoCommit) throws SQLException {
		expectAutoCommitSetTo(autoCommit);

		return new ScriptRunner(connection, autoCommit, false);
	}

	private void expectAutoCommitSetTo(boolean autoCommit) throws SQLException {
		Mockito.when(connection.getAutoCommit()).thenReturn(autoCommit);
	}
}
