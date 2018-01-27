import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class CollectedResultSetMetaData {
	private ResultSetMetaData wrappedMetadata;

	public CollectedResultSetMetaData(ResultSetMetaData resultSetMetaData) {
		wrappedMetadata = resultSetMetaData;
	}

	public int getColumnCount() throws SQLException {
		return wrappedMetadata.getColumnCount();
	}

	public Collection<String> getColumnLabels()throws SQLException {
		Collection<String> labels = new ArrayList<>();

		for (int i = 0; i < getColumnCount(); i++) {
			labels.add(wrappedMetadata.getColumnLabel(i + 1));
		}

		return labels;
	}
}
