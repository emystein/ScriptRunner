package ar.com.kamikaze.persistence.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectedResultSetMetaData {
	private ResultSetMetaData wrappedMetadata;

	public CollectedResultSetMetaData(ResultSetMetaData resultSetMetaData) {
		wrappedMetadata = resultSetMetaData;
	}

	public List<String> getColumnLabels()throws SQLException {
		List<String> labels = new ArrayList<>();

		for (int i = 1; i <= wrappedMetadata.getColumnCount(); i++) {
			labels.add(wrappedMetadata.getColumnLabel(i));
		}

		return labels;
	}
}
