package ar.com.kamikaze.persistence.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

//TODO: split this class in two: 1. connection behavior and 2. command execution
@Slf4j
public abstract class ConnectionWrapper {
	protected final Connection connection;
	private boolean autoCommit;
	private boolean originalAutoCommit;
	private List<CommandResultEventListener> commandResultEventListeners = new ArrayList<>();

	protected ConnectionWrapper(Connection connection, boolean autoCommit) throws SQLException {
		this.connection = connection;
		this.autoCommit = autoCommit;
		this.originalAutoCommit = connection.getAutoCommit();
	}

	public abstract void commit() throws SQLException;

	public void rollback() throws SQLException {
		connection.rollback();
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		connection.setAutoCommit(autoCommit);
	}

	public void rollbackAutoCommit() throws SQLException {
		setAutoCommit(originalAutoCommit);
	}


	// TODO: review how to implement pattern: setAutoCommit(autoCommit) -> execute commands -> setAutoCommit(originalAutoCommit)
	public void run(List<ScriptCommand> commands, ErrorHandler errorHandler) throws SQLException {
		setAutoCommit(autoCommit);

		for (ScriptCommand command : commands) {
			execute(command, errorHandler);
		}

		commit();

		setAutoCommit(originalAutoCommit);
	}

	public void execute(ScriptCommand command, ErrorHandler errorHandler) throws SQLException {
		try {
			execute(command);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			errorHandler.handle(e);
		}
	}

	private ResultSet execute(ScriptCommand command) throws SQLException {
		try {
			return execute(command.getCommand());
		} catch (SQLException e) {
			throw new ScriptCommandException(command, e);
		}
	}

	public ResultSet execute(String command) throws SQLException {
		log.debug(command);

		Statement statement = connection.createStatement();

		statement.execute(command);

		ResultSet resultSet = Optional.ofNullable(statement.getResultSet()).orElse(new NullResultSet());

		CommandResult commandResult = new CommandResult(command, resultSet);

		for (CommandResultEventListener eventListener : commandResultEventListeners) {
			eventListener.onCommandResult(commandResult);
		}

		return resultSet;
	}

	public void addCommandResultEventListener(CommandResultEventListener eventListener) {
		commandResultEventListeners.add(eventListener);
	}
}
