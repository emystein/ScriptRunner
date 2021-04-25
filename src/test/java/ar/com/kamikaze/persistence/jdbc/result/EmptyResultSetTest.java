package ar.com.kamikaze.persistence.jdbc.result;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class EmptyResultSetTest {
	private ResultSet nullResultSet = new EmptyResultSet();

	@Test
	void metadata() throws SQLException {
		var metaData = nullResultSet.getMetaData();
		assertThat(metaData.getColumnCount()).isEqualTo(0);
		assertThat(metaData.getColumnLabels()).isEmpty();
	}

	@Test
	public void nullResultSetShouldNotHaveNext() throws SQLException {
		assertThat(nullResultSet.hasNext()).isFalse();
	}

	@Test
	void nextRow() throws SQLException {
		assertThat(nullResultSet.nextRow().getValues()).isEmpty();
	}
}
