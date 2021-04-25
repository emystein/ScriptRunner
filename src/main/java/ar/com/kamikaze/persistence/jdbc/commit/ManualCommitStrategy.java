package ar.com.kamikaze.persistence.jdbc.commit;

import ar.com.kamikaze.persistence.jdbc.connection.JdbcConnection;

import java.sql.SQLException;

public class ManualCommitStrategy implements CommitStrategy {
    @Override
    public boolean isManual() {
        return true;
    }

    @Override
    public void commit(JdbcConnection connection) throws SQLException {
        // do nothing
    }
}
