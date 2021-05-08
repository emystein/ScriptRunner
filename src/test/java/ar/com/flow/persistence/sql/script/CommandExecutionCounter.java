package ar.com.flow.persistence.sql.script;

import ar.com.flow.persistence.jdbc.result.CommandResult;
import ar.com.flow.persistence.jdbc.result.ResultObserver;

import java.sql.SQLException;

class CommandExecutionCounter implements ResultObserver {
    private int count;

    public void handle(CommandResult commandResult) throws SQLException {
        count++;
    }

    public int getCount() {
        return count;
    }
}