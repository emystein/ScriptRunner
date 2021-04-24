package ar.com.kamikaze.persistence.jdbc.commit;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;


@RequiredArgsConstructor
public class AutoCommitStrategy implements CommitStrategy {
    private final Connection connection;

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }
}
