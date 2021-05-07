package ar.com.flow.persistence.jdbc.result;

import java.sql.SQLException;

public interface ResultObserver {
	void handle(CommandResult commandResult) throws SQLException;
}
