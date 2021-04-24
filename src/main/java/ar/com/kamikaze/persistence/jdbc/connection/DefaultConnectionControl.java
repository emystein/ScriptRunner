package ar.com.kamikaze.persistence.jdbc.connection;

import ar.com.kamikaze.persistence.jdbc.commit.CommitStrategy;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.sql.Statement;

@RequiredArgsConstructor
public class DefaultConnectionControl implements ConnectionControl {
	private final JdbcConnection connection;
	private final CommitStrategy commitStrategy;

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
		commitStrategy.commit();
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
