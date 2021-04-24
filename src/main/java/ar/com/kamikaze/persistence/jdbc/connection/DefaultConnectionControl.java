package ar.com.kamikaze.persistence.jdbc.connection;

import ar.com.kamikaze.persistence.jdbc.commit.Commit;
import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DefaultConnectionControl implements ConnectionControl {
	private final JdbcConnectionWrapper connection;
	private final Commit commit;

	public DefaultConnectionControl(Connection connection, boolean autoCommit, Commit commit) throws SQLException {
		this.connection = new JdbcConnectionWrapper(connection, autoCommit);
		this.commit = commit;
	}

	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.connection.setErrorHandler(errorHandler);
	}

	@Override
	public void setUpExecution() throws SQLException {
		connection.setUpExecution();
	}

	@Override
	public void endExecution() throws SQLException {
		connection.endExecution();
	}

	@Override
	public Statement createStatement() throws SQLException {
		return connection.createStatement();
    }

	@Override
	public void commit() throws SQLException {
		commit.commit();
	}

	@Override
	public void handleError(SQLException exception) throws SQLException {
		connection.handleError(exception);
	}

	@Override
	public void rollback() throws SQLException {
		connection.rollback();
	}
}
