package ar.com.flow.persistence.jdbc.result;

import java.sql.SQLException;
import java.util.List;

public interface ResultSetMetaData {
    int getColumnCount() throws SQLException;

    List<String> getColumnLabels() throws SQLException;
}
