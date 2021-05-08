package ar.com.flow.persistence.jdbc.result;

import ar.com.flow.persistence.sql.script.ScriptCommand;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CommandResult {
	private final String command;
	private final ResultSet resultSet;

	public CommandResult(ScriptCommand command, ResultSet resultSet) {
		this.command = command.getCommand();
		this.resultSet = resultSet;
	}
}
