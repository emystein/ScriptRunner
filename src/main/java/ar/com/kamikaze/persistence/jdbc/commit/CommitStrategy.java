package ar.com.kamikaze.persistence.jdbc.commit;

import java.sql.SQLException;

public interface CommitStrategy {
    void commit() throws SQLException;
}
