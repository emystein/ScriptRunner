package ar.com.flow.persistence.jdbc.result;

import ar.com.flow.persistence.sql.script.ScriptCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

@ExtendWith(MockitoExtension.class)
public class PrintResultObserverTest {
	@InjectMocks
	private PrintResultObserver observer;

	@Mock
	private ResultSetPrinter resultSetPrinter;

	@Mock
	private DefaultResultSet resultSet;

	@Test
	public void handle() throws SQLException {
		observer.handle(new ScriptCommand("", null), resultSet);

		Mockito.verify(resultSetPrinter).print(resultSet);
	}
}
