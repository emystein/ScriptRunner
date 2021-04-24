package ar.com.kamikaze.persistence.jdbc.connection;

import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;
import ar.com.kamikaze.persistence.jdbc.result.CommandResultListener;
import ar.com.kamikaze.persistence.jdbc.script.ScriptCommand;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ManualCommitCommandRunner implements ConnectionControl, Commands {
	private final ConnectionControl connection;
	private final Commit commit;
	private final Commands commands;

	public ManualCommitCommandRunner(Connection wrappedConnection) throws SQLException {
		commit = new ManualCommit();
		connection = new CommandRunnerWrapper(wrappedConnection, true, commit);
		commands = new CommandExecutor(connection);
	}

	public ManualCommitCommandRunner(Connection wrappedConnection, ErrorHandler errorHandler) throws SQLException {
		this(wrappedConnection);
		setErrorHandler(errorHandler);
	}

	// Connection

	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		connection.setErrorHandler(errorHandler);
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

	public void commit() throws SQLException {
		commit.commit();
	}

	@Override
	public void rollback() throws SQLException {
		connection.rollback();
	}

	@Override
	public void handleError(SQLException exception) throws SQLException {
		connection.handleError(exception);
	}

	// Commands

	@Override
	public void addCommandResultListener(CommandResultListener eventListener) {
		commands.addCommandResultListener(eventListener);
	}

	@Override
	public void execute(List<ScriptCommand> commands) throws SQLException {
		this.commands.execute(commands);
	}

	@Override
	public ResultSet execute(ScriptCommand command) throws SQLException {
		return commands.execute(command);
	}

	@Override
	public ResultSet execute(String command) throws SQLException {
		return commands.execute(command);
	}
}
