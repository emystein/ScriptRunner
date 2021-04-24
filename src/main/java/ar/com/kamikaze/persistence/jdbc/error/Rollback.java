package ar.com.kamikaze.persistence.jdbc.error;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class Rollback implements ErrorHandler {
	private final Connection connection;

	@Override
	public void handle(SQLException exception) throws SQLException {
		connection.rollback();
		throw exception;
	}
}
