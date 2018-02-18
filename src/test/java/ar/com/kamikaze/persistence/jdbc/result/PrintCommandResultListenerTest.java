package ar.com.kamikaze.persistence.jdbc.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PrintCommandResultListenerTest {
	@Mock
	private ResultSetPrinter resultSetPrinter;

	@InjectMocks
	private PrintCommandResultListener printCommandResultListener;

	@Mock
	private ResultSet resultSet;

	@Test
	public void handle() throws SQLException {
		printCommandResultListener.handle(new CommandResult("", resultSet));

		Mockito.verify(resultSetPrinter).print(resultSet);
	}
}