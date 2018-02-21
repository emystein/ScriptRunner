package ar.com.kamikaze.persistence.jdbc.script;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ar.com.kamikaze.persistence.jdbc.script.ScriptRunner;

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
	public void whenAutoCommitIsOnThenConnectionShouldExecuteCommit() throws Exception {
		scriptRunner = createScriptRunnerWithAutoCommitSetTo(true);

		scriptRunner.runScript(scriptPath);

		Mockito.verify(connection).commit();
	}

	@Test
	public void whenAutoCommitIsOffThenConnectionShouldNotExecuteCommit() throws Exception {
		scriptRunner = createScriptRunnerWithAutoCommitSetTo(false);

		scriptRunner.runScript(scriptPath);

		Mockito.verify(connection, never()).commit();
	}

	private ScriptRunner createScriptRunnerWithAutoCommitSetTo(boolean autoCommit) throws SQLException {
		expectAutoCommitSetTo(autoCommit);

		return new ScriptRunner(connection, autoCommit, false);
	}

	private void expectAutoCommitSetTo(boolean autoCommit) throws SQLException {
		Mockito.when(connection.getAutoCommit()).thenReturn(autoCommit);
	}
}
