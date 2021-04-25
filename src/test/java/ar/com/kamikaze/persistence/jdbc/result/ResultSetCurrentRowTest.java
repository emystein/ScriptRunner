package ar.com.kamikaze.persistence.jdbc.result;

import ar.com.kamikaze.persistence.jdbc.script.ScriptRunnerBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

public class ResultSetCurrentRowTest {
    private static Connection connection;

    @BeforeAll
    public static void beforeClass() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:test");
        var scriptRunner = ScriptRunnerBuilder.forConnection(connection).autoCommit().build();
        scriptRunner.runScript("src/test/resources/schema.sql");
        scriptRunner.runScript("src/test/resources/insert-posts.sql");
    }

    @Test
    public void getValues() throws SQLException {
        var statement = connection.createStatement();
        statement.execute("SELECT id, title, author_id FROM post WHERE id = 1");
        var resultSet = statement.getResultSet();
        resultSet.next();
        var row = new ResultSetCurrentRow(resultSet);

        assertThat(row.getValues()).isEqualTo(list("1", "author 1 post 1", "1"));
    }
}