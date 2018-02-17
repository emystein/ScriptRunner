package ar.com.kamikaze.persistence.jdbc;

import java.sql.SQLException;

public interface CommandResultListener {
	void handle(CommandResult commandResult) throws SQLException;
}
