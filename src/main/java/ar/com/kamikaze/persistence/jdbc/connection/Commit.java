package ar.com.kamikaze.persistence.jdbc.connection;

import java.sql.SQLException;

public interface Commit {
    void commit() throws SQLException;
}
