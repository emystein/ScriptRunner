package ar.com.kamikaze.persistence.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ar.com.kamikaze.persistence.jdbc.script.ScriptCommand;
import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;

@RunWith(MockitoJUnitRunner.class)
public class ErrorHandlingConnectionWrapperTest {
	@Mock
	private Connection connection;
	@Mock
	private ErrorHandler errorHandler;

	private ConnectionWrapper connectionWrapper;

	@Before
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
