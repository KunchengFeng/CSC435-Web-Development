package org.example.objects;

import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterRowMapper(PersonMapper.class)
public interface PersonDao extends SqlObject {
    // Create
    @SqlUpdate("INSERT INTO population (name, occupation, age) VALUES (:name, :occupation, :age)")
    @GetGeneratedKeys
    int create(@BindBean Person person);

    // Review
    @SqlQuery("SELECT * FROM population WHERE id = :id")
    Person findById(@Bind("id") int id);

    @SqlQuery("SELECT * FROM population")
    List<Person> list();

    // Update
    @SqlUpdate("UPDATE population SET name = :name, occupation = :occupation, age = :age WHERE id = :id")
    void update(@BindBean Person person);

    // Delete
    @SqlUpdate("DELETE FROM population WHERE id = :id")
    void deleteById(@Bind("id") int id);
}
