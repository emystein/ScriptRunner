package ar.com.kamikaze.persistence.jdbc.result;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class EmptyResultSetTest {
	private ResultSet emptyResultSet = new EmptyResultSet();

	@Test
	void metadata() throws SQLException {
		var metaData = emptyResultSet.getMetaData();

		assertThat(metaData.getColumnCount()).isEqualTo(0);
		assertThat(metaData.getColumnLabels()).isEmpty();
	}

	@Test
	public void next() throws SQLException {
		assertThat(emptyResultSet.hasNext()).isFalse();
		assertThat(emptyResultSet.nextRow().getValues()).isEmpty();
	}
}
