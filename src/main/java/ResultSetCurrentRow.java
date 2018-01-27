import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class ResultSetCurrentRow {
	private ResultSet resultSet;

	public ResultSetCurrentRow(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public Collection<String> getValues() throws SQLException {
		Collection<String> values = new ArrayList<>();

		for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
			values.add(resultSet.getString(i + 1));
		}

		return values;
	}
}
