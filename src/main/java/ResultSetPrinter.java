import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetPrinter {
	private final ResultSet resultSet;
	private final PrintWriter printWriter;
	private CollectedResultSetMetaData resultSetMetaData;

	public ResultSetPrinter(ResultSet resultSet, PrintWriter printWriter) throws SQLException {
		this.resultSet = resultSet;
		this.printWriter = printWriter;
		if (resultSet != null)
			resultSetMetaData = new CollectedResultSetMetaData(resultSet.getMetaData());
	}

	public void print() throws SQLException {
		if (resultSet == null) {
			return;
		}

		printWriter.println(String.join("\t", resultSetMetaData.getColumnLabels()));

		printWriter.println("");

		while (resultSet.next()) {
			ResultSetCurrentRow row = new ResultSetCurrentRow(resultSet);
			printWriter.println(String.join("\t", row.getValues()));
		}
	}
}
