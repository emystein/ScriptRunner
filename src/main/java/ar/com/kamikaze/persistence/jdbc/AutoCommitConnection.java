package ar.com.kamikaze.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class AutoCommitConnection extends ConnectionWrapper {
	public AutoCommitConnection(Connection connection) {
		super(connection);
	}

	public void commit() throws SQLException {
		// do nothing, since this is auto commit
	}
}
