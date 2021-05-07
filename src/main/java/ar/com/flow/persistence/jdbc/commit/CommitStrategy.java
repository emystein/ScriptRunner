package ar.com.flow.persistence.jdbc.commit;

import java.sql.Connection;
import java.sql.SQLException;

public interface CommitStrategy {
    boolean isAutomatic();

    void commit(Connection connection) throws SQLException;
}
