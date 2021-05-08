package ar.com.flow.persistence.jdbc.connection;

import ar.com.flow.persistence.jdbc.commit.AutoCommitStrategy;
import ar.com.flow.persistence.jdbc.error.ErrorHandler;
import ar.com.flow.persistence.sql.script.ScriptCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

@ExtendWith(MockitoExtension.class)
public class ScriptCommandErrorHandlerTest {
    @Mock
    private Connection connection;
    @Mock
    private ErrorHandler errorHandler;

    @Test
    public void whenACommandFailsThenErrorHandlerShouldExecute() throws SQLException {
        var sqlException = new SQLException();

        Mockito.when(connection.createStatement()).thenThrow(sqlException);

        var connectionWrapper = new DefaultConnection(connection, new AutoCommitStrategy(), errorHandler);

        var commandRunner = new ScriptCommand(1, "failure", connectionWrapper);

        commandRunner.execute();

        Mockito.verify(errorHandler).handle(sqlException);
    }
}
