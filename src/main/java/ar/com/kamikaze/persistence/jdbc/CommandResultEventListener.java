package ar.com.kamikaze.persistence.jdbc;

import java.sql.SQLException;

public interface CommandResultEventListener {
	void onCommandResult(CommandResult commandResult) throws SQLException;
}
