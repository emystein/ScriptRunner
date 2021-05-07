package ar.com.flow.persistence.jdbc.connection;

import ar.com.flow.persistence.jdbc.commands.CommandRunner;
import ar.com.flow.persistence.jdbc.commit.AutoCommitStrategy;
import ar.com.flow.persistence.jdbc.error.ErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static ar.com.flow.persistence.jdbc.commands.CommandRunnerFactory.createCommandRunner;

@ExtendWith(MockitoExtension.class)
public class ErrorHandlingCommandRunnerTest {
	@Mock
	private Connection connection;
	@Mock
	private ErrorHandler errorHandler;

	private CommandRunner commandRunner;

	@BeforeEach
	public void setUp() throws Exception {
		commandRunner = createCommandRunner(connection, new AutoCommitStrategy(), errorHandler);
	}

	@Test
	public void whenACommandFailsThenErrorHandlerShouldExecute() throws SQLException {
		var sqlException = new SQLException();

		Mockito.when(connection.createStatement()).thenThrow(sqlException);

		commandRunner.execute("failure");

		Mockito.verify(errorHandler).handle(sqlException);
	}
}
