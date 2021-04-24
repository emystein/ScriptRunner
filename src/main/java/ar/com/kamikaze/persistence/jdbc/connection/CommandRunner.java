package ar.com.kamikaze.persistence.jdbc.connection;

import ar.com.kamikaze.persistence.jdbc.error.ErrorHandler;
import ar.com.kamikaze.persistence.jdbc.result.CommandResultListener;
import ar.com.kamikaze.persistence.jdbc.script.ScriptCommand;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface CommandRunner {
    void setErrorHandler(ErrorHandler errorHandler);

    void addCommandResultListener(CommandResultListener eventListener);

    void execute(List<ScriptCommand> commands) throws SQLException;

    ResultSet execute(ScriptCommand command) throws SQLException;

    ResultSet execute(String command) throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;
}
