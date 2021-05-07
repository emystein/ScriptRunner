package ar.com.flow.persistence.sql.script;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScriptRunnerTestCase {
    protected Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:test");

        var scriptRunner = ScriptRunnerBuilder.forConnection(connection).autoCommit().build();

        scriptRunner.runScript("src/test/resources/schema.sql");
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    protected java.sql.ResultSet query(String query) throws SQLException {
        var statement = connection.createStatement();
        statement.execute(query);
        return statement.getResultSet();
    }

    protected int postCount() {
        try {
            ResultSet resultSet = query("SELECT COUNT(*) AS count FROM post");
            resultSet.next();
            return resultSet.getInt("count");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    protected boolean authorExist(String author) {
        try {
            ResultSet resultSet = query("SELECT COUNT(*) AS count FROM author where name = '" + author + "'");
            resultSet.next();
            return resultSet.getInt("count") > 0;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
