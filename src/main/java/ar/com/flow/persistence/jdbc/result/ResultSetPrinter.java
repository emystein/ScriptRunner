package ar.com.flow.persistence.jdbc.result;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class ResultSetPrinter {
	public void print(ResultSet resultSet) throws SQLException {
		log.debug(join(resultSet.getMetaData().getColumnLabels()) + "\n");

		// TODO: implement JdbcResultSet.iterator
		var next = resultSet.nextRow();

		while (next.hasValues()) {
			log.debug(join(next.getValues()));
			next = resultSet.nextRow();
		}
	}

	private String join(List<String> values) {
		return String.join("\t", values);
	}
}
