package ar.com.kamikaze.persistence.jdbc;

import java.sql.SQLException;

public class PrintCommandResultEventListener implements CommandResultEventListener {
	private ResultSetPrinter resultSetPrinter = new ResultSetPrinter();


	@Override
	public void onCommandResult(CommandResult commandResult) throws SQLException {
		resultSetPrinter.print(commandResult.getResultSet());
	}
}
