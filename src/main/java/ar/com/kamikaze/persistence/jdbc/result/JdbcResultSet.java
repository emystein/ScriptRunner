package ar.com.kamikaze.persistence.jdbc.result;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcResultSet {
    private final ResultSet wrappedResultSet;

    public JdbcResultSet(ResultSet wrappedResultSet) {
        this.wrappedResultSet = wrappedResultSet;
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return new ResultSetMetaData(wrappedResultSet.getMetaData());
    }

    public boolean hasNext() throws SQLException {
        return wrappedResultSet.next();
    }

    public ResultSetCurrentRow nextRow() throws SQLException {
        var resultSet = hasNext() ? wrappedResultSet : new NullResultSet();

        return new ResultSetCurrentRow(resultSet);
    }
}
