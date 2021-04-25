package ar.com.kamikaze.persistence.jdbc.commit;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;


@RequiredArgsConstructor
public class AutoCommitStrategy implements CommitStrategy {
    @Override
    public boolean isManual() {
        return false;
    }

    @Override
    public void commit(Connection connection) throws SQLException {
        connection.commit();
    }
}
