package ar.com.flow.persistence.jdbc.error;

import java.sql.SQLException;

public class ContinueExecution implements ErrorHandler {
	@Override
	public void handle(SQLException exception) throws SQLException {
		// do nothing
	}
}
