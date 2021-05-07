package ar.com.flow.persistence.jdbc.commit;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;


@RequiredArgsConstructor
public class AutoCommitStrategy implements CommitStrategy {
    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public void commit(Connection connection) throws SQLException {
        // empty
    }
}
