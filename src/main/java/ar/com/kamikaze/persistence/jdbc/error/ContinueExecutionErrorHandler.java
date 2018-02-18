package ar.com.kamikaze.persistence.jdbc.error;

import java.sql.SQLException;

public class ContinueExecutionErrorHandler implements ErrorHandler {
	@Override
	public void handle(SQLException exception) throws SQLException {
		// do nothing
	}
}
