package ar.com.kamikaze.persistence.jdbc;

import ar.com.kamikaze.persistence.jdbc.connection.Connection;
import ar.com.kamikaze.persistence.jdbc.result.*;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class DefaultStatement implements Statement {
    private final Connection connection;
    private final List<ResultObserver> resultObservers;

    public ResultSet execute(String command) throws SQLException {
        var statement = connection.createStatement();

        statement.execute(command);

        var resultSet = statement.getResultSet() == null ?
                new EmptyResultSet() :
                new DefaultResultSet(statement.getResultSet());

        var commandResult = new CommandResult(command, resultSet);

        for (ResultObserver observer : resultObservers) {
            observer.handle(commandResult);
        }

        return resultSet;
    }
}
