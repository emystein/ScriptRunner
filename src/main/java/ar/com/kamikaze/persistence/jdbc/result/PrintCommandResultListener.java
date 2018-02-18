package ar.com.kamikaze.persistence.jdbc.result;

import java.sql.SQLException;

public class PrintCommandResultListener implements CommandResultListener {
	private ResultSetPrinter resultSetPrinter = new ResultSetPrinter();


	@Override
	public void handle(CommandResult commandResult) throws SQLException {
		resultSetPrinter.print(commandResult.getResultSet());
	}
}
