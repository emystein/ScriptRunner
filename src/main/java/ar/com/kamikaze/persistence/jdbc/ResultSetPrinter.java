package ar.com.kamikaze.persistence.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResultSetPrinter {
	public void print(ResultSet resultSet) throws SQLException {
		CollectedResultSetMetaData resultSetMetaData = new CollectedResultSetMetaData(resultSet.getMetaData());

		log.debug(separateValues(resultSetMetaData.getColumnLabels()));

		log.debug("");

		while (resultSet.next()) {
			ResultSetCurrentRow row = new ResultSetCurrentRow(resultSet);
			log.debug(separateValues(row.getValues()));
		}
	}

	private String separateValues(List<String> columnLabels) {
		return String.join("\t", columnLabels);
	}
}
