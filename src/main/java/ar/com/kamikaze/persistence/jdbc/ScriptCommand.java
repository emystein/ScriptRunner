package ar.com.kamikaze.persistence.jdbc;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ScriptCommand {
	private final int lineNumber;
	private final String command;
}
