package ar.com.kamikaze.persistence.jdbc;

import java.sql.SQLException;

public class ContinueExecutionErrorHandler implements ErrorHandler {
	@Override
	public void handle(SQLException exception) throws SQLException {
		// do nothing
	}
}
