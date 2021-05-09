package ar.com.flow.persistence.sql.script;

import ar.com.flow.persistence.jdbc.result.ResultObserver;
import ar.com.flow.persistence.jdbc.result.ResultSet;

import java.sql.SQLException;

public class CommandExecutionCounter implements ResultObserver {
    private int count;

    public void handle(ScriptCommand command, ResultSet resultSet) throws SQLException {
        count++;
    }

    public int getCount() {
        return count;
    }
}