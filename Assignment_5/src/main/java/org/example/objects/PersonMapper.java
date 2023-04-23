package org.example.objects;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<Person> {
    @Override
    public Person map(ResultSet rs, StatementContext context) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String occupation = rs.getString("occupation");
        int age = rs.getInt("age");

        return new Person(id, age, name, occupation);
    }
}
