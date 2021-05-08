package ar.com.flow.persistence.jdbc.result;

import ar.com.flow.persistence.sql.script.ScriptCommand;

import java.sql.SQLException;

public interface ResultObserver {
	void handle(ScriptCommand command, ResultSet resultSet) throws SQLException;
}
