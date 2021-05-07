package ar.com.flow.persistence.jdbc.commit;

import java.sql.Connection;
import java.sql.SQLException;

public class ManualCommitStrategy implements CommitStrategy {
    @Override
    public boolean isAutomatic() {
        return false;
    }

    @Override
    public void commit(Connection connection) throws SQLException {
        connection.commit();
    }
}
