package ar.com.kamikaze.persistence.jdbc.result;

import java.sql.SQLException;

public class PrintResultObserver implements ResultObserver {
	private ResultSetPrinter resultSetPrinter = new ResultSetPrinter();

	@Override
	public void handle(CommandResult commandResult) throws SQLException {
		resultSetPrinter.print(commandResult.getResultSet());
	}
}
