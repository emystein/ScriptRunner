package ar.com.kamikaze.persistence.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import ar.com.kamikaze.persistence.jdbc.script.ScriptCommand;
import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ErrorHandlingConnectionWrapperTest {
	@Mock
	private Connection connection;
	@Mock
	private ErrorHandler errorHandler;

	private ConnectionWrapper connectionWrapper;

	@BeforeEach
	public void setUp() throws Exception {
		connectionWrapper = new AutoCommitConnection(connection);
		connectionWrapper.setErrorHandler(errorHandler);
	}

	@Test
	public void whenACommandFailsThenErrorHandlerShouldExecute() throws SQLException {
		SQLException sqlException = new SQLException();

		Mockito.when(connection.createStatement()).thenThrow(sqlException);

		connectionWrapper.execute(new ScriptCommand(1, "failure"));

		Mockito.verify(errorHandler).handle(sqlException);
	}
}
