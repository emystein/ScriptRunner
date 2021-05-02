package ar.com.kamikaze.persistence.sql.script;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static ar.com.kamikaze.persistence.sql.script.Scripts.AUHTOR_1_DUPLICATE_POSTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class AutoCommitScriptRunnerTest extends ScriptRunnerTestCase {
    private ScriptRunnerBuilder scriptRunnerBuilder;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        scriptRunnerBuilder = ScriptRunnerBuilder.forConnection(connection).autoCommit();
    }

    @Test
    void continueOnError() throws Exception {
        var scriptRunner = scriptRunnerBuilder.build();

        scriptRunner.runScript(AUHTOR_1_DUPLICATE_POSTS);

        assertThat(postCount()).isEqualTo(1);
        // insert of alice happens after duplicate insert of author 1 posts
        assertTrue(authorExist("alice"));
    }

    @Test
    void stopOnError() throws SQLException {
        var scriptRunner = scriptRunnerBuilder.stopOnError().build();

        assertThrows(SQLException.class, () ->
            scriptRunner.runScript(AUHTOR_1_DUPLICATE_POSTS));

        assertThat(postCount()).isEqualTo(1);
        // insert of alice happens after duplicate insert of author 1 posts
        assertFalse(authorExist("alice"));
    }
}
