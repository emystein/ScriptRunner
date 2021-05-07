package ar.com.flow.persistence.jdbc.result;

import java.sql.SQLException;

public class DefaultResultSet implements ResultSet {
    private final java.sql.ResultSet wrappedResultSet;

    public DefaultResultSet(java.sql.ResultSet wrappedResultSet) {
        this.wrappedResultSet = wrappedResultSet;
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return new DefaultResultSetMetaData(wrappedResultSet.getMetaData());
    }

    public boolean hasNext() throws SQLException {
        return wrappedResultSet.next();
    }

    public ResultSetCurrentRow nextRow() throws SQLException {
        var resultSet = hasNext() ? this : new EmptyResultSet();

        return new ResultSetCurrentRow(resultSet);
    }

    public String getString(int columnIndex) throws SQLException {
        return wrappedResultSet.getString(columnIndex);
    }

    public String getString(String columnName) throws SQLException {
        return wrappedResultSet.getString(columnName);
    }

    public Integer getInt(String columnName) throws SQLException {
        return wrappedResultSet.getInt(columnName);
    }
}
