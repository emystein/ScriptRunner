package ar.com.kamikaze.persistence.jdbc;

import java.sql.SQLException;

public interface ErrorHandler {
	void handle(SQLException exception) throws SQLException;
}
