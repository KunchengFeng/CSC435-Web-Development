package servlets;

import database.Database;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import presentation.Message;
import presentation.ResponseHelper;

import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "Delete", value = "/delete")
public class Delete extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idString = request.getParameter("id");
        if (idString == null) {
            ResponseHelper.present(response, new Message("Missing an id."));

        } else {
            int id = Integer.parseInt(idString);
            try {
                Connection connection = Database.getConnection();
                if (Database.delete(connection, id)) {
                    ResponseHelper.present(response, new Message("Successfully deleted this person."));
                } else {
                    ResponseHelper.present(response, new Message("Failed to delete this person."));
                }

            } catch (Exception e) {
                System.out.println("Failed to establish an connection...");
                e.printStackTrace();
            }
        }
    }
}
