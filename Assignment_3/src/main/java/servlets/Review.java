package servlets;

import database.Database;
import database.Person;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import presentation.ResponseHelper;

import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "Review", value = "/review")
public class Review extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idString = request.getParameter("id");
        if (idString == null) {
            ResponseHelper.present(response, "Missing an id.");
        } else {
            int id = Integer.parseInt(idString);
            try {
                Connection connection = Database.getConnection();
                Person person = Database.review(connection, id);
                ResponseHelper.present(response, person);
            } catch (Exception e) {
                System.out.println("Failed to establish an connection...");
                e.printStackTrace();
            }
        }
    }
}
