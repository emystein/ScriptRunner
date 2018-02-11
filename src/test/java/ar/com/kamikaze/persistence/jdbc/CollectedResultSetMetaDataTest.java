package ar.com.kamikaze.persistence.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CollectedResultSetMetaDataTest {
	@Mock
	private ResultSetMetaData resultSetMetaData;
	@InjectMocks
	private CollectedResultSetMetaData collectedMetadata;

	@Test
	public void getColumnLabels() throws SQLException {
		when(resultSetMetaData.getColumnCount()).thenReturn(2);
		when(resultSetMetaData.getColumnLabel(1)).thenReturn("Label 1");
		when(resultSetMetaData.getColumnLabel(2)).thenReturn("Label 2");

		assertThat(collectedMetadata.getColumnLabels()).containsExactly("Label 1", "Label 2");
	}
}
