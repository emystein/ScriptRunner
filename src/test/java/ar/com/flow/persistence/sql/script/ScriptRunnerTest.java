package ar.com.flow.persistence.sql.script;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.*;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class ScriptRunnerTest {
    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:test");
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    public static Collection<Object[]> runScriptDataProvider() {
        return Arrays.asList(new Object[][]{
                {false, false, false},
                {false, false, true},
                {false, true, false},
                {false, true, true},
                {true, false, false},
                {true, false, true},
                {true, true, false},
                {true, true, true}
        });
    }

    @ParameterizedTest
    @MethodSource("runScriptDataProvider")
    public void runScript(boolean connectionAutoCommit, boolean runnerAutoCommit, boolean runnerStopOnError) throws Exception {
        connection.setAutoCommit(connectionAutoCommit);

        var scriptRunner = ScriptRunnerBuilder.forConnection(connection)
                .autoCommit(runnerAutoCommit)
                .stopOnError(runnerStopOnError)
                .build();

        scriptRunner.runScript("src/test/resources/schema.sql");
        scriptRunner.runScript("src/test/resources/insert-posts.sql");

        Statement statement = connection.createStatement();

        // assertions
        statement.execute("SELECT post.title, author.name FROM post, author WHERE post.author_id = author.id ORDER BY post.title");
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        assertThat(resultSet.getString("post.title")).isEqualTo("author 1 post 1");
        assertThat(resultSet.getString("author.name")).isEqualTo("emystein");
    }
}
