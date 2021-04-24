package ar.com.kamikaze.persistence.jdbc.commit;

import java.sql.SQLException;

public interface Commit {
    void commit() throws SQLException;
}
