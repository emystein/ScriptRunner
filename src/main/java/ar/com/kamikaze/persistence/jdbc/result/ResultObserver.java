package ar.com.kamikaze.persistence.jdbc.result;

import java.sql.SQLException;

public interface ResultObserver {
	void handle(CommandResult commandResult) throws SQLException;
}
