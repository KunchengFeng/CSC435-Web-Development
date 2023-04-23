package servlets;

import com.google.gson.Gson;
import database.Database;
import database.Person;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import presentation.Message;
import presentation.ResponseHelper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "Create", value = "/create")
public class Create extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Connection connection = Database.getConnection();
            Database.setUpTables(connection);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ResponseHelper.present(response, new Message("Tables are ready."));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String occupation = request.getParameter("occupation");
        String ageString = request.getParameter("age");
        int age;

        // Terminates if no name
        if (name == null) {
            ResponseHelper.present(response, new Message("Person needs a name."));
        }

        // Filter parameter and create the pop
        if (occupation == null) {
            occupation = "unemployed";
        }
        if (ageString == null) {
            age = -1;
        } else {
            age = Integer.parseInt(ageString);
        }
        Person person = new Person(name, occupation, age);

        try {
            Connection connection = Database.getConnection();
            int id = Database.create(connection, person);
            if (id != -1) {
                ResponseHelper.present(response, new Message("Created successfully, id = " + id));
            } else {
                ResponseHelper.present(response, new Message("Failed to create this person."));
            }
        } catch (Exception e) {
            System.out.println("Failed to establish an connection...");
            e.printStackTrace();
        }
    }
}
