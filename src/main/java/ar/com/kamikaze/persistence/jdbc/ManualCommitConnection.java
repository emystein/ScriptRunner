package ar.com.kamikaze.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class ManualCommitConnection extends ConnectionWrapper {
	public ManualCommitConnection(Connection connection, boolean stopOnError) throws SQLException {
		super(connection, false, stopOnError);
	}

	@Override
	public void commit() throws SQLException {
		connection.commit();
	}
}
