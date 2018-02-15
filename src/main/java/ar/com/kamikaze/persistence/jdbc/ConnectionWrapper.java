package ar.com.kamikaze.persistence.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ConnectionWrapper {
	protected final Connection connection;
	private boolean autoCommit;
	private boolean originalAutoCommit;
	private boolean stopOnError;
	private List<CommandResultEventListener> commandResultEventListeners = new ArrayList<>();

	protected ConnectionWrapper(Connection connection, boolean autoCommit, boolean stopOnError) throws SQLException {
		this.connection = connection;
		this.autoCommit = autoCommit;
		this.originalAutoCommit = connection.getAutoCommit();
		this.stopOnError = stopOnError;
	}

	public abstract void commit() throws SQLException;

	public void rollback() throws SQLException {
		connection.rollback();
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		connection.setAutoCommit(autoCommit);
	}

	// TODO: review how to implement pattern: setAutoCommit(autoCommit) -> execute commands -> setAutoCommit(originalAutoCommit)
	public void run(List<ScriptCommand> commands) throws SQLException {
		setAutoCommit(autoCommit);

		for (ScriptCommand command : commands) {
			try {
				execute(command);
			} catch (SQLException e) {
				log.error(e.getMessage(), e);

				if (stopOnError) {
					rollback();
					setAutoCommit(originalAutoCommit);
					throw e;
				}
			}
		}

		commit();

		setAutoCommit(originalAutoCommit);
	}

	public ResultSet execute(ScriptCommand command) throws SQLException {
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
