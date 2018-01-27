import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetPrinter {
	private final PrintWriter printWriter;

	public ResultSetPrinter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

	public void print(ResultSet resultSet) throws SQLException {
		if (resultSet == null) {
			return;
		}

		CollectedResultSetMetaData resultSetMetaData = new CollectedResultSetMetaData(resultSet.getMetaData());

		printWriter.println(String.join("\t", resultSetMetaData.getColumnLabels()));

		printWriter.println("");

		while (resultSet.next()) {
			ResultSetCurrentRow row = new ResultSetCurrentRow(resultSet);
			printWriter.println(String.join("\t", row.getValues()));
		}
	}
}
