package ar.com.kamikaze.persistence.jdbc;

import java.sql.SQLException;

public class ScriptCommandException extends SQLException {
	public ScriptCommandException(ScriptCommand command, SQLException exception) {
		super("Error executing " + command + exception.getMessage());
	}
}
