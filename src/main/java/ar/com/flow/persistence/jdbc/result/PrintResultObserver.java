package ar.com.flow.persistence.jdbc.result;

import java.sql.SQLException;

public class PrintResultObserver implements ResultObserver {
	private ResultSetPrinter resultSetPrinter = new ResultSetPrinter();

	public void handle(CommandResult result) throws SQLException {
		resultSetPrinter.print(result.getResultSet());
	}
}
