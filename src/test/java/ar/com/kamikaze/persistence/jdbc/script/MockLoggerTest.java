package ar.com.kamikaze.persistence.jdbc.script;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

public class MockLoggerTest {
	@Mock
	protected Appender mockAppender;
	@Captor
	protected ArgumentCaptor<LoggingEvent> captorLoggingEvent;

	@Before
	public void setUp() throws Exception {
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.addAppender(mockAppender);
		root.setLevel(Level.DEBUG);
	}

	@After
	public void tearDown() throws Exception {
		final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.detachAppender(mockAppender);
	}

	protected void assertLogMessage(LoggingEvent loggingEvent, String expectedMessage, Level expectedLevel) {
		assertThat(loggingEvent.getLevel()).isEqualTo(expectedLevel);
		assertThat(loggingEvent.getMessage()).isEqualTo(expectedMessage);
	}

	protected void assertDebugMessages(String... expectedMessages) {
		Mockito.verify(mockAppender, Mockito.times(expectedMessages.length)).doAppend(captorLoggingEvent.capture());

		for (int i = 0; i < expectedMessages.length; i++) {
			LoggingEvent loggingEvent = captorLoggingEvent.getAllValues().get(i);
			assertLogMessage(loggingEvent, expectedMessages[i], Level.DEBUG);
		}
	}
}
