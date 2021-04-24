package ar.com.kamikaze.persistence.jdbc.result;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;

@Data
@RequiredArgsConstructor
public class CommandResult {
	private final String command;
	private final ResultSet resultSet;
}
