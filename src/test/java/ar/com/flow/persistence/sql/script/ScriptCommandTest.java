package ar.com.flow.persistence.sql.script;

import ar.com.flow.persistence.jdbc.commit.AutoCommitStrategy;
import ar.com.flow.persistence.jdbc.connection.DefaultConnection;
import ar.com.flow.persistence.jdbc.error.Rollback;
import ar.com.flow.persistence.jdbc.result.ResultObserver;
import ar.com.flow.persistence.jdbc.result.ResultSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class ScriptCommandTest {
    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:test");

        var scriptRunnerBuilder = ScriptRunnerBuilder.forConnection(connection);
        ScriptRunner scriptRunner = scriptRunnerBuilder.build();
        scriptRunner.runScript("src/test/resources/schema.sql");
        scriptRunner.runScript("src/test/resources/insert-posts.sql");
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void execute() throws SQLException {
        var defaultConnection = new DefaultConnection(connection, new AutoCommitStrategy(), new Rollback(connection));

        var resultObservers = new ArrayList<ResultObserver>();
        var lineExecutedCounter = new CommandExecutionCounter();
        resultObservers.add(lineExecutedCounter);

        var command = new ScriptCommand("SELECT * FROM post;", defaultConnection, resultObservers);

        ResultSet resultSet = command.execute();

        assertThat(resultSet.hasNext()).isTrue();
        assertThat(lineExecutedCounter.getCount()).isEqualTo(1);
    }
}