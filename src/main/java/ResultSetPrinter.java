import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetPrinter {
	private final ResultSet rs;
	private final PrintWriter printWriter;

	public ResultSetPrinter(ResultSet rs, PrintWriter printWriter) {
		this.rs = rs;
		this.printWriter = printWriter;
	}

	public void print() throws SQLException {
		CollectedResultSetMetaData resultSetMetaData = new CollectedResultSetMetaData(rs.getMetaData());

		printWriter.println(String.join("\t", resultSetMetaData.getColumnLabels()));

		printWriter.println("");

		while (rs.next()) {
			ResultSetCurrentRow row = new ResultSetCurrentRow(rs);
			printWriter.println(String.join("\t", row.getValues()));
		}
	}
}
