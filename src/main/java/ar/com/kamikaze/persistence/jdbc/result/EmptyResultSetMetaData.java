package ar.com.kamikaze.persistence.jdbc.result;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmptyResultSetMetaData implements ResultSetMetaData {
	@Override
	public int getColumnCount() throws SQLException {
		return 0;
	}

	@Override
	public List<String> getColumnLabels() throws SQLException {
		return new ArrayList<>();
	}
}
