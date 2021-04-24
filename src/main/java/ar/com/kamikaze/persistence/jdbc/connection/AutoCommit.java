package ar.com.kamikaze.persistence.jdbc.connection;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;


@RequiredArgsConstructor
public class AutoCommit implements Commit {
    private final Connection connection;

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }
}
