package ar.com.kamikaze.persistence.jdbc.result;

import ar.com.kamikaze.persistence.jdbc.script.ScriptRunnerBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

@ExtendWith(MockitoExtension.class)
public class ResultSetWrapperTest {
    private static Connection connection;

    @BeforeAll
    public static void beforeClass() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:test");
        var scriptRunner = ScriptRunnerBuilder.forConnection(connection).autoCommit().build();
        scriptRunner.runScript("src/test/resources/schema.sql");
        scriptRunner.runScript("src/test/resources/insert-posts.sql");
    }

    @Test
    public void getRowValues() throws SQLException {
        var resultSet = wrapResults("SELECT id, title, author_id FROM post WHERE id = 1");

        assertThat(resultSet.nextRow().getValues()).isEqualTo(list("1", "author 1 post 1", "1"));
    }

    @Test
    public void getEmptyResults() throws SQLException {
        var resultSet = wrapResults("SELECT id, title, author_id FROM post WHERE id = -1");

        assertThat(resultSet.nextRow().getValues()).isEqualTo(list());
    }

    @Test
    public void getNullResults() throws SQLException {
        var resultSet = new NullResultSet();

        assertThat(resultSet.nextRow().getValues()).isEqualTo(list());
    }

    @Test
    public void metadata() throws SQLException {
        var resultSet = wrapResults("SELECT id, title, author_id FROM post WHERE id = 1");

        assertThat(resultSet.getMetaData().getColumnLabels()).isEqualTo(list("ID", "TITLE", "AUTHOR_ID"));
    }

    private ResultSetWrapper wrapResults(String aQuery) throws SQLException {
        return new ResultSetWrapper(query(aQuery));
    }

    private java.sql.ResultSet query(String query) throws SQLException {
        var statement = connection.createStatement();
        statement.execute(query);
        return statement.getResultSet();
    }
}
