package ar.com.kamikaze.persistence.jdbc.commands;

import ar.com.kamikaze.persistence.jdbc.result.ResultObserver;
import ar.com.kamikaze.persistence.jdbc.script.ScriptCommand;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface CommandRunner {
    void addResultObserver(ResultObserver eventListener);

    void execute(List<ScriptCommand> commands) throws SQLException;

    ResultSet execute(ScriptCommand command) throws SQLException;

    ResultSet execute(String command) throws SQLException;
}
