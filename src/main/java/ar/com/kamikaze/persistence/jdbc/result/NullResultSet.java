package ar.com.kamikaze.persistence.jdbc.result;

import java.sql.SQLException;

public class NullResultSet implements ResultSet {
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return new NullResultSetMetaData();
	}

	@Override
	public boolean hasNext() throws SQLException {
		return false;
	}

	@Override
	public ResultSetCurrentRow nextRow() throws SQLException {
		return new ResultSetCurrentRow(this);
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public String getString(String columnName) throws SQLException {
		return null;
	}

	@Override
	public Integer getInt(String id) {
		return null;
	}
}
