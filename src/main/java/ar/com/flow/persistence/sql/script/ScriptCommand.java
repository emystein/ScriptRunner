package ar.com.flow.persistence.sql.script;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ScriptCommand {
	private final int lineNumber;
	private final String command;
}
