package ar.com.kamikaze.persistence.jdbc.result;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class NullResultSetTest {
	@Test
	public void nullResultSetShouldNotHaveNext() throws SQLException {
		var nullResultSet = new NullResultSet();

		assertThat(nullResultSet.hasNext()).isFalse();
	}
}
