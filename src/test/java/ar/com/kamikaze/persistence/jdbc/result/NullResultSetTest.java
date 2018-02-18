package ar.com.kamikaze.persistence.jdbc.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;

public class NullResultSetTest {
	@Test
	public void nullResultSetShouldNotHaveNext() throws SQLException {
		ResultSet nullResultSet = new NullResultSet();

		assertThat(nullResultSet.next()).isFalse();

	}
}
