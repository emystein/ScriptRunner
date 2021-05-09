package ar.com.flow.persistence.sql.script;

import ar.com.flow.persistence.jdbc.DefaultStatement;
import ar.com.flow.persistence.jdbc.connection.Connection;
import ar.com.flow.persistence.jdbc.result.EmptyResultSet;
import ar.com.flow.persistence.jdbc.result.ResultObserver;
import ar.com.flow.persistence.jdbc.result.ResultSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ScriptCommand {
    @Getter
	private final String command;
    private final Connection connection;
    private final List<ResultObserver> commandResultObservers;

    public ResultSet execute() throws SQLException {
        log.debug(command);

        ResultSet resultSet = new EmptyResultSet();

        try {
            resultSet = new DefaultStatement(connection).execute(command);

            for (ResultObserver observer : commandResultObservers) {
                observer.handle(this, resultSet);
            }
        } catch (SQLException exception) {
            log.error("Error executing: {}. Error message: {}", command, exception.getMessage(), exception);
            connection.handleError(exception);
        }

        return resultSet;
    }
}
