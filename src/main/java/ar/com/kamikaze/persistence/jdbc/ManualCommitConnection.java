package ar.com.kamikaze.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class ManualCommitConnection extends ConnectionWrapper {
	public ManualCommitConnection(Connection connection) throws SQLException {
		super(connection, false);
	}

	@Override
	public void commit() throws SQLException {
		connection.commit();
	}
}
