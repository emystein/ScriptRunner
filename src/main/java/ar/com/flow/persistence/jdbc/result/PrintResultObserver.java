package ar.com.flow.persistence.jdbc.result;

import ar.com.flow.persistence.sql.script.ScriptCommand;

import java.sql.SQLException;

public class PrintResultObserver implements ResultObserver {
	private ResultSetPrinter resultSetPrinter = new ResultSetPrinter();

	@Override
	public void handle(ScriptCommand command, ResultSet resultSet) throws SQLException {
		resultSetPrinter.print(resultSet);
	}
}
