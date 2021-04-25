package ar.com.kamikaze.persistence.jdbc.result;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CommandResult {
	private final String command;
	private final ResultSet resultSet;

	public CommandResult(String command, java.sql.ResultSet wrappedResultSet) {
		this.command = command;
		this.resultSet = new ResultSetWrapper(wrappedResultSet);
	}
}
