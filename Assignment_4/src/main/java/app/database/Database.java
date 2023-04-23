package app.database;

import app.objects.Person;

import java.sql.*;

public class Database {
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null) {
            Class.forName("org.postgresql.Driver");     // Used to load Postgres driver
            String url = "jdbc:postgresql://localhost:5432/MyDatabase";
            String username = "MyUser";
            String password = "MyPassword";
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }

    // Two tables will be selected, one called serfs for basic usage,
    // one called summary to help keep track of things.
    public static void setUpTables() {
        try {
            Statement statement = connection.createStatement();

            String s = "CREATE TABLE IF NOT EXISTS population" +
                    "(id SERIAL PRIMARY KEY, " +
                    " name VARCHAR(255), " +
                    " occupation VARCHAR(255), " +
                    " age INTEGER);";
            statement.executeUpdate(s);
            System.out.println("Table have been set up...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int create(Person person) {
        try {
            String sql = "INSERT INTO population (name, occupation, age) VALUES (?, ?, ?) RETURNING id";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, person.getName());
            statement.setString(2, person.getOccupation());
            statement.setInt(3, person.getAge());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Person review(int id) {
        try {
            String sql = "SELECT * FROM population WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String occupation = rs.getString("occupation");
                int age = rs.getInt("age");
                return new Person(id, name, occupation, age);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void update(Person person) {
        try {
            String name = person.getName();
            String occupation = person.getOccupation();
            int age = person.getAge();
            int id = person.getId();
            String sql = "UPDATE population SET name = ?, occupation = ?, age = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, occupation);
            statement.setInt(3, age);
            statement.setInt(4, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean delete(int id) {
        try {
            String sql = "DELETE FROM population WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            // If more than one person is deleted then it is a success, although IDs are supposed to be unique.
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
