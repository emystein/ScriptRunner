package ar.com.flow.persistence.jdbc.result;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultResultSetMetaData implements ResultSetMetaData {
	private java.sql.ResultSetMetaData wrappedMetadata;

	public DefaultResultSetMetaData(java.sql.ResultSetMetaData resultSetMetaData) {
		wrappedMetadata = resultSetMetaData;
	}

	@Override
	public int getColumnCount() throws SQLException {
		return wrappedMetadata.getColumnCount();
	}

	@Override
	public List<String> getColumnLabels() throws SQLException {
		var labels = new ArrayList<String>();

		for (int i = 1; i <= getColumnCount(); i++) {
			labels.add(wrappedMetadata.getColumnLabel(i));
		}

		return labels;
	}
}
