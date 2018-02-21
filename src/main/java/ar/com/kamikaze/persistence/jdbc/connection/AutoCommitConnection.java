package ar.com.kamikaze.persistence.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class AutoCommitConnection extends ConnectionWrapper {
	public AutoCommitConnection(Connection connection) throws SQLException {
		super(connection, false);
	}

	@Override
	public void commit() throws SQLException {
		connection.commit();
	}
}
