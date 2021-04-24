package ar.com.kamikaze.persistence.jdbc.connection;

import ar.com.kamikaze.persistence.jdbc.commands.CommandRunner;
import ar.com.kamikaze.persistence.jdbc.commands.CommandRunnerFactory;
import ar.com.kamikaze.persistence.jdbc.commit.AutoCommitStrategy;
import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;
import ar.com.kamikaze.persistence.jdbc.script.ScriptCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

@ExtendWith(MockitoExtension.class)
public class ErrorHandlingDefaultConnectionControlTest {
	@Mock
	private Connection wrappedConnection;
	@Mock
	private ErrorHandler errorHandler;

	private CommandRunner commandRunner;

	@BeforeEach
	public void setUp() throws Exception {
		commandRunner = CommandRunnerFactory.createCommandRunner(wrappedConnection, new AutoCommitStrategy(wrappedConnection), errorHandler);
	}

	@Test
	public void whenACommandFailsThenErrorHandlerShouldExecute() throws SQLException {
		SQLException sqlException = new SQLException();

		Mockito.when(wrappedConnection.createStatement()).thenThrow(sqlException);

		commandRunner.execute(new ScriptCommand(1, "failure"));

		Mockito.verify(errorHandler).handle(sqlException);
	}
}
