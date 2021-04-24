package ar.com.kamikaze.persistence.jdbc.commit;

import java.sql.SQLException;

public class ManualCommit implements Commit {
    @Override
    public void commit() throws SQLException {
        // do nothing
    }
}
