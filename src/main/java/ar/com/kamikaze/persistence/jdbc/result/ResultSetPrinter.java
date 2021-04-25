package ar.com.kamikaze.persistence.jdbc.result;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class ResultSetPrinter {
	public void print(JdbcResultSet resultSet) throws SQLException {
		var resultSetMetaData = resultSet.getMetaData();

		log.debug(separateValues(resultSetMetaData.getColumnLabels()) + "\n");

		// TODO: implement JdbcResultSet.iterator
		var next = resultSet.nextRow();

		while (next.hasValues()) {
			log.debug(separateValues(next.getValues()));
			next = resultSet.nextRow();
		}
	}

	private String separateValues(List<String> columnLabels) {
		return String.join("\t", columnLabels);
	}
}
