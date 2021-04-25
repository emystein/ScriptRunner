package ar.com.kamikaze.persistence.jdbc.commit;

import java.sql.SQLException;

public class ManualCommitStrategy implements CommitStrategy {
    @Override
    public boolean isManual() {
        return true;
    }

    @Override
    public void commit() throws SQLException {
        // do nothing
    }
}
