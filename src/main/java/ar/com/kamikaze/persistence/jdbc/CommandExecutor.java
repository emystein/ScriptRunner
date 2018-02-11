package ar.com.kamikaze.persistence.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CommandExecutor {
	private final ConnectionWrapper connection;
	private final ResultSetPrinter resultSetPrinter;

	public void execute(String command) throws SQLException {
		Statement statement = connection.createStatement();

		log.debug(command);

		statement.execute(command);

		ResultSet resultSet = Optional.ofNullable(statement.getResultSet()).orElse(new NullResultSet());

		resultSetPrinter.print(resultSet);
	}
}
