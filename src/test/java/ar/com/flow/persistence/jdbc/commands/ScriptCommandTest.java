package ar.com.flow.persistence.jdbc.commands;

import ar.com.flow.persistence.jdbc.commit.AutoCommitStrategy;
import ar.com.flow.persistence.jdbc.connection.DefaultConnection;
import ar.com.flow.persistence.jdbc.error.Rollback;
import ar.com.flow.persistence.jdbc.result.CommandResult;
import ar.com.flow.persistence.jdbc.result.ResultObserver;
import ar.com.flow.persistence.jdbc.result.ResultSet;
import ar.com.flow.persistence.sql.script.ScriptCommand;
import ar.com.flow.persistence.sql.script.ScriptRunner;
import ar.com.flow.persistence.sql.script.ScriptRunnerBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class ScriptCommandTest {
    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception, IOException {
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
        var resultCounter = new ExecutionCounter();
        resultObservers.add(resultCounter);

        var command = new ScriptCommand(1, "SELECT * FROM post;", defaultConnection, resultObservers);

        ResultSet resultSet = command.execute();

        assertThat(resultSet.hasNext()).isTrue();
        assertThat(resultCounter.getCount()).isEqualTo(1);
    }
}

class ExecutionCounter implements ResultObserver {
    private int count;

    public void handle(CommandResult commandResult) throws SQLException {
        count++;
    }

    public int getCount() {
        return count;
    }
}
