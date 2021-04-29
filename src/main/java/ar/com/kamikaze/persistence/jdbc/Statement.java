package ar.com.kamikaze.persistence.jdbc;

import ar.com.kamikaze.persistence.jdbc.result.ResultSet;

import java.sql.SQLException;

public interface Statement {
    ResultSet execute(String command) throws SQLException;
}
