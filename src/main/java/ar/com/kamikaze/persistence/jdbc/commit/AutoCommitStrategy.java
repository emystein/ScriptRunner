package ar.com.kamikaze.persistence.jdbc.commit;

import ar.com.kamikaze.persistence.jdbc.connection.JdbcConnection;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;


@RequiredArgsConstructor
public class AutoCommitStrategy implements CommitStrategy {
    @Override
    public boolean isManual() {
        return false;
    }

    @Override
    public void commit(JdbcConnection connection) throws SQLException {
        connection.commit();
    }
}
