package ar.com.kamikaze.persistence.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class ManualCommitConnection extends ConnectionWrapper {
	public ManualCommitConnection(Connection connection) throws SQLException {
		super(connection, true);
	}

	public void commit() throws SQLException {
		// do nothing, since this class is for manual commit
	}
}
