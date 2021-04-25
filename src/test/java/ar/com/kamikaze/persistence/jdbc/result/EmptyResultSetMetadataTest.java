package ar.com.kamikaze.persistence.jdbc.result;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class EmptyResultSetMetadataTest {
	@Test
	public void metadata() throws SQLException {
		var metaData = new EmptyResultSetMetaData();

		assertThat(metaData.getColumnCount()).isEqualTo(0);
		assertThat(metaData.getColumnLabels()).isEmpty();
	}
}
