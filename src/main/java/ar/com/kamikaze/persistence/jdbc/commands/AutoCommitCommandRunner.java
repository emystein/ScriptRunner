package ar.com.kamikaze.persistence.jdbc.commands;

import ar.com.kamikaze.persistence.jdbc.commit.AutoCommit;
import ar.com.kamikaze.persistence.jdbc.commit.Commit;
import ar.com.kamikaze.persistence.jdbc.connection.ConnectionControl;
import ar.com.kamikaze.persistence.jdbc.connection.DefaultConnectionControl;
import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;
import ar.com.kamikaze.persistence.jdbc.result.CommandResultListener;
import ar.com.kamikaze.persistence.jdbc.script.ScriptCommand;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AutoCommitCommandRunner implements CommandRunner {
	private final ConnectionControl connection;
	private final Commit commit;
	private final CommandRunner commandRunner;

	public AutoCommitCommandRunner(Connection wrappedConnection) throws SQLException {
		commit = new AutoCommit(wrappedConnection);
		connection = new DefaultConnectionControl(wrappedConnection, false, commit);
		commandRunner = new CommandExecutor(connection);
	}

	public AutoCommitCommandRunner(Connection wrappedConnection, ErrorHandler errorHandler) throws SQLException {
		this(wrappedConnection);
		connection.setErrorHandler(errorHandler);
	}

	@Override
	public void addCommandResultListener(CommandResultListener eventListener) {
	    commandRunner.addCommandResultListener(eventListener);
	}

	@Override
	public void execute(List<ScriptCommand> commands) throws SQLException {
		this.commandRunner.execute(commands);
	}

	@Override
	public ResultSet execute(ScriptCommand command) throws SQLException {
		return commandRunner.execute(command);
	}

	@Override
	public ResultSet execute(String command) throws SQLException {
		return commandRunner.execute(command);
	}
}
