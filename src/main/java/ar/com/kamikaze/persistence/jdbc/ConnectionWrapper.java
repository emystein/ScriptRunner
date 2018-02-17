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
	private ErrorHandler errorHandler;
	private boolean autoCommit;
	private boolean originalAutoCommit;
	private List<CommandResultListener> commandResultListeners = new ArrayList<>();

	protected ConnectionWrapper(Connection connection, boolean autoCommit) throws SQLException {
		this.connection = connection;
		this.autoCommit = autoCommit;
		this.originalAutoCommit = connection.getAutoCommit();
	}

	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	public void addCommandResultListener(CommandResultListener eventListener) {
		commandResultListeners.add(eventListener);
	}

	public abstract void commit() throws SQLException;

	public void rollback() throws SQLException {
		connection.rollback();
	}

	public void rollbackAutoCommit() throws SQLException {
		setAutoCommit(originalAutoCommit);
	}

	// TODO: review how to implement pattern: setAutoCommit(autoCommit) -> execute commands -> setAutoCommit(originalAutoCommit)
	public void run(List<ScriptCommand> commands) throws SQLException {
		setAutoCommit(autoCommit);

		for (ScriptCommand command : commands) {
			execute(command);
		}

		commit();

		setAutoCommit(originalAutoCommit);
	}

	public ResultSet execute(ScriptCommand command) throws SQLException {
		return execute(command.getCommand());
	}

	public ResultSet execute(String command) throws SQLException {
		log.debug(command);

		ResultSet resultSet = new NullResultSet();

		try {
			Statement statement = connection.createStatement();

			statement.execute(command);

			resultSet = Optional.ofNullable(statement.getResultSet()).orElse(new NullResultSet());
		} catch (SQLException e) {
			log.error("Error executing " + command + ": " + e.getMessage(), e);
			errorHandler.handle(e);
		}

		triggerCommandResultEvent(command, resultSet);

		return resultSet;
	}

	private void triggerCommandResultEvent(String command, ResultSet resultSet) throws SQLException {
		CommandResult commandResult = new CommandResult(command, resultSet);

		for (CommandResultListener eventListener : commandResultListeners) {
			eventListener.handle(commandResult);
		}
	}

	private void setAutoCommit(boolean autoCommit) throws SQLException {
		connection.setAutoCommit(autoCommit);
	}
}
