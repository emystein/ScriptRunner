package ar.com.kamikaze.persistence.jdbc.connection;

import ar.com.kamikaze.persistence.jdbc.result.CommandResultListener;
import ar.com.kamikaze.persistence.jdbc.script.ScriptCommand;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Commands {
    void addCommandResultListener(CommandResultListener eventListener);

    void execute(List<ScriptCommand> commands) throws SQLException;

    ResultSet execute(ScriptCommand command) throws SQLException;

    ResultSet execute(String command) throws SQLException;
}
