package ar.com.flow.persistence.jdbc;

import ar.com.flow.persistence.jdbc.result.ResultSet;

import java.sql.SQLException;

public interface Statement {
    ResultSet execute(String command) throws SQLException;
}
