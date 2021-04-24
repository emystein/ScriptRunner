package ar.com.kamikaze.persistence.jdbc.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ar.com.kamikaze.persistence.jdbc.script.ScriptCommand;
import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;
import ar.com.kamikaze.persistence.jdbc.result.CommandResult;
import ar.com.kamikaze.persistence.jdbc.result.CommandResultListener;
import ar.com.kamikaze.persistence.jdbc.result.NullResultSet;
import lombok.extern.slf4j.Slf4j;

//TODO: split this class in two: 1. connection behavior and 2. command execution
@Slf4j
public abstract class CommandRunnerWrapper implements CommandRunner {
	protected final Connection connection;
	private ErrorHandler errorHandler;
	private boolean autoCommit;
	private boolean originalAutoCommit;
	private List<CommandResultListener> commandResultListeners = new ArrayList<>();

	protected CommandRunnerWrapper(Connection connection, boolean autoCommit) throws SQLException {
		this.connection = connection;
		this.autoCommit = autoCommit;
		this.originalAutoCommit = connection.getAutoCommit();
	}

	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	@Override
	public void addCommandResultListener(CommandResultListener eventListener) {
		commandResultListeners.add(eventListener);
	}

	// TODO: review how to implement pattern: setAutoCommit(autoCommit) -> execute commands -> setAutoCommit(originalAutoCommit)
	@Override
	public void execute(List<ScriptCommand> commands) throws SQLException {
		setAutoCommit(autoCommit);

		for (ScriptCommand command : commands) {
			execute(command);
		}

		commit();

		setAutoCommit(originalAutoCommit);
	}

	@Override
	public ResultSet execute(ScriptCommand command) throws SQLException {
		return execute(command.getCommand());
	}

	@Override
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

	@Override
	public void rollback() throws SQLException {
		connection.rollback();
		setAutoCommit(originalAutoCommit);
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
