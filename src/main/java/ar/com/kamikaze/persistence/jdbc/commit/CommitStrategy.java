package ar.com.kamikaze.persistence.jdbc.commit;

import ar.com.kamikaze.persistence.jdbc.connection.JdbcConnection;

import java.sql.SQLException;

public interface CommitStrategy {
    boolean isManual();

    void commit(JdbcConnection connection) throws SQLException;
}
