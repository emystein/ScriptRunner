package ar.com.kamikaze.persistence.jdbc.result;

import org.junit.jupiter.api.Test;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class NullResultSetMetadataTest {
	private ResultSetMetaData nullResultSetMetaData = new NullResultSetMetaData();

	@Test
	public void whenResultSetIsNullThenColumnCountShouldBe0() throws SQLException {
		assertThat(nullResultSetMetaData.getColumnCount()).isEqualTo(0);
	}
}
