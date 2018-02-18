package ar.com.kamikaze.persistence.jdbc.result;

import java.sql.ResultSet;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CommandResult {
	private final String command;
	private final ResultSet resultSet;
}
