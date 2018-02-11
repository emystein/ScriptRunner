package ar.com.kamikaze.persistence.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ConnectionWrapper {
	protected final Connection connection;

	protected ConnectionWrapper(Connection connection) {
		this.connection = connection;
	}

	public abstract void commit() throws SQLException;

	public void rollback() throws SQLException {
		connection.rollback();
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		connection.setAutoCommit(autoCommit);
	}

	public ResultSet execute(String command) throws SQLException {
		Statement statement = connection.createStatement();

		log.debug(command);

		statement.execute(command);

		return Optional.ofNullable(statement.getResultSet()).orElse(new NullResultSet());
	}
}
