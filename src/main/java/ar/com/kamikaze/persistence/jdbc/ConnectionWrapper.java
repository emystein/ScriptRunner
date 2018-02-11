package ar.com.kamikaze.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ConnectionWrapper {
	protected final Connection connection;

	protected ConnectionWrapper(Connection connection) {
		this.connection = connection;
	}

	public abstract void commit() throws SQLException;

	public void rollback() throws SQLException {
		connection.rollback();
	}

	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		connection.setAutoCommit(autoCommit);
	}
}
