package ar.com.flow.persistence.jdbc.result;

import java.sql.SQLException;
import java.util.Iterator;

public interface ResultSet extends Iterator<ResultSetCurrentRow> {
    ResultSetMetaData getMetaData() throws SQLException;

    boolean hasNext();

    ResultSetCurrentRow next();

    String getString(int columnIndex) throws SQLException;

    String getString(String columnName) throws SQLException;

    Integer getInt(String columnName) throws SQLException;
}
