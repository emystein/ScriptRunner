package ar.com.kamikaze.persistence.jdbc.script;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ScriptParser {
	private static final String DEFAULT_DELIMITER = ";";
	private String delimiter = DEFAULT_DELIMITER;
	private boolean fullLineDelimiter = false;

	public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
		this.delimiter = delimiter;
		this.fullLineDelimiter = fullLineDelimiter;
	}

	public List<ScriptCommand> parse(Reader reader) throws IOException {
		List<ScriptCommand> commands = new ArrayList<>();

		// ignores spaces, allows delimiter in comment, allows an equals-sign
		final Pattern delimiterDetectionPattern = Pattern.compile("^\\s*(--)?\\s*delimiter\\s*=?\\s*([^\\s]+)+\\s*.*$", Pattern.CASE_INSENSITIVE);

		LineNumberReader lineReader = new LineNumberReader(reader);
		StringBuffer command = null;

		try {
			String line;
			while ((line = lineReader.readLine()) != null) {
				if (command == null) {
					command = new StringBuffer();
				}
				String trimmedLine = line.trim();
				final Matcher delimMatch = delimiterDetectionPattern.matcher(trimmedLine);
				if (trimmedLine.length() < 1 || trimmedLine.startsWith("//")) {
					// Do nothing
				} else if (delimMatch.matches()) {
					setDelimiter(delimMatch.group(2), false);
				} else if (trimmedLine.startsWith("--")) {
					log.debug(trimmedLine);
				} else if (!fullLineDelimiter && trimmedLine.endsWith(delimiter)
						|| fullLineDelimiter && trimmedLine.equals(delimiter)) {
					command.append(line.substring(0, line.lastIndexOf(delimiter)));
					command.append(" ");
					commands.add(new ScriptCommand(lineReader.getLineNumber(), command.toString()));
					command = null;
				} else {
					command.append(line);
					command.append("\n");
				}
			}
			if (command != null) {
				commands.add(new ScriptCommand(lineReader.getLineNumber(), command.toString()));
			}
		} catch (IOException e) {
			throw new IOException(String.format("Error parsing line %d: %s", lineReader.getLineNumber(), e.getMessage()), e);
		}

		return commands;
	}
}
