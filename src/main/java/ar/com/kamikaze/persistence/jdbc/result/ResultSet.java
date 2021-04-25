package ar.com.kamikaze.persistence.jdbc.result;

import java.sql.SQLException;

public interface ResultSet {
    ResultSetMetaData getMetaData() throws SQLException;

    boolean hasNext() throws SQLException;

    ResultSetCurrentRow nextRow() throws SQLException;

    String getString(int columnIndex) throws SQLException;

    String getString(String columnName) throws SQLException;

    Integer getInt(String id) throws SQLException;
}
