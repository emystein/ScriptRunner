package ar.com.kamikaze.persistence.jdbc.commit;

import java.sql.Connection;
import java.sql.SQLException;

public interface CommitStrategy {
    boolean isManual();

    void commit(Connection connection) throws SQLException;
}
