package ar.com.kamikaze.persistence.jdbc.result;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

@ExtendWith(MockitoExtension.class)
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