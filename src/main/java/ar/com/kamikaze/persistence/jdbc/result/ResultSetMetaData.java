package ar.com.kamikaze.persistence.jdbc.result;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetMetaData {
	private java.sql.ResultSetMetaData wrappedMetadata;

	public ResultSetMetaData(java.sql.ResultSetMetaData resultSetMetaData) {
		wrappedMetadata = resultSetMetaData;
	}

	public List<String> getColumnLabels() throws SQLException {
		var labels = new ArrayList<String>();

		int columnCount = wrappedMetadata.getColumnCount();

		for (int i = 1; i <= columnCount; i++) {
			labels.add(wrappedMetadata.getColumnLabel(i));
		}

		return labels;
	}
}
