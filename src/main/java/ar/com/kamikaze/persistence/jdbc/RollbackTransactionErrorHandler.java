package ar.com.kamikaze.persistence.jdbc;

import java.sql.SQLException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RollbackTransactionErrorHandler implements ErrorHandler {
	private final ConnectionWrapper connection;

	@Override
	public void handle(SQLException exception) throws SQLException {
		connection.rollback();
		connection.rollbackAutoCommit();
		throw exception;
	}
}
