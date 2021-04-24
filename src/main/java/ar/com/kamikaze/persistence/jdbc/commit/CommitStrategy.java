package ar.com.kamikaze.persistence.jdbc.commit;

import java.sql.SQLException;

public interface CommitStrategy {
    boolean isManualCommit();

    void commit() throws SQLException;
}
