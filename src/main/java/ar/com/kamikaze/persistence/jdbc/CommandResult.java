package ar.com.kamikaze.persistence.jdbc;

import java.sql.ResultSet;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CommandResult {
	private final String command;
	private final ResultSet resultSet;
}
