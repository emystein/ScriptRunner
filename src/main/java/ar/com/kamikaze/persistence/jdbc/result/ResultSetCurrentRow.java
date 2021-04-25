package ar.com.kamikaze.persistence.jdbc.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetCurrentRow {
	private ResultSet resultSet;

	public ResultSetCurrentRow(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public boolean hasValues() throws SQLException {
		return !getValues().isEmpty();
	}

	public List<String> getValues() throws SQLException {
		var values = new ArrayList<String>();

		for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
			values.add(resultSet.getString(i + 1));
		}

		return values;
	}
}
