package ar.com.kamikaze.persistence.jdbc.error;

import java.sql.SQLException;

import ar.com.kamikaze.persistence.jdbc.connection.CommandRunner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RollbackTransactionErrorHandler implements ErrorHandler {
	private final CommandRunner commandRunner;

	@Override
	public void handle(SQLException exception) throws SQLException {
		commandRunner.rollback();
		throw exception;
	}
}
