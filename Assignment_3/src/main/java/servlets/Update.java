package servlets;

import database.Database;
import database.Person;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import presentation.Message;
import presentation.ResponseHelper;

import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "Update", value = "/update")
public class Update extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idString = request.getParameter("id");
        if (idString == null) {
            ResponseHelper.present(response, new Message("Missing an id."));
        } else {
            int id = Integer.parseInt(idString);
            try {
                // Retrieve from database.
                Connection connection = Database.getConnection();
                Person person = Database.review(connection, id);
                if (person == null) {
                    ResponseHelper.present(response, new Message("No person uses this id."));
                    return;
                }

                // Modifies any new values.
                String name = request.getParameter("name");
                if (name != null) {
                    person.setName(name);
                }

                String occupation = request.getParameter("occupation");
                if (occupation != null) {
                    person.setOccupation(occupation);
                }

                String ageString = request.getParameter("age");
                if (ageString != null) {
                    person.setAge(Integer.parseInt(ageString));
                }

                Database.update(connection, person);
                ResponseHelper.present(response, person);

            } catch (Exception e) {
                System.out.println("Failed to establish an connection...");
                e.printStackTrace();
            }
        }
    }
}
