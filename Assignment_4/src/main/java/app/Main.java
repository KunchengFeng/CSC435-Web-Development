package app;

import app.database.Database;
import app.objects.Person;
import app.objects.Message;
import com.google.gson.Gson;
import spark.Request;

import java.sql.Connection;

import static spark.Spark.*;

public class Main {

    private static Gson gson;
    private static Connection connection;


    public static void main(String[] args) {
        // ----------------------- Set up ------------------------ //
        try {
            gson = new Gson();
            connection = Database.getConnection();
            Database.setUpTables();
            port(10000);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // ------------------------ Create --------------------------- //
        get("/create", (request, response) -> {
            response.status(405);
            return "Method not allowed.";
        });
        post("/create", (request, response) -> {
            response.type("application/json");
            response.status(200);
            return create(request);
        });

        // --------------------------- Review ------------------------- //
        get("/review", (request, response) -> {
            response.status(405);
            return "Method not allowed.";
        });
        post("/review", (request, response) -> {
            response.type("application/json");
            response.status(200);
            return review(request);
        });

        // --------------------------- Update --------------------------- //
        get("/update", (request, response) -> {
            response.status(405);
            return "Method not allowed.";
        });
        post("/update", (request, response) -> {
            response.type("application/json");
            response.status(200);
            return update(request);
        });

        // ---------------------------- Delete --------------------------- //
        get("/delete", (request, response) -> {
            response.status(405);
            return "Method not allowed.";
        });
        post("/delete", (request, response) -> {
            response.type("application/json");
            response.status(200);
            return delete(request);
        });
    }

    private static String create(Request request) {
        // Get the required information from the request.
        String name = request.queryParams("name");
        if (name == null) {
            return ("Person needs a name.");
        }

        String occupation = request.queryParams("occupation");
        if (occupation == null) {
            occupation = "unemployed";
        }
        String ageString = request.queryParams("age");

        int age;
        if (ageString == null) {
            age = -1;
        } else {
            age = Integer.parseInt(ageString);
        }

        // Create the person class
        Person person = new Person(name, occupation, age);

        // Put into database and return the results.
        int id = Database.create(person);
        if (id == -1) {
            return gson.toJson(new Message("Failed to create this person."));
        } else {
            return gson.toJson(new Message("Created successfully, id = " + id));
        }
    }

    private static String review(Request request) {
        String idString = request.queryParams("id");
        if (idString == null) {
            return "Need an id.";
        }

        int id = Integer.parseInt(idString);
        Person p = Database.review(id);
        if (p == null) {
            return "No such person.";
        }

        return gson.toJson(p);
    }

    private static String update(Request request) {
        String idString = request.queryParams("id");
        if (idString == null) {
            return "Need an id.";
        }

        // First retrieve the person from the database
        int id = Integer.parseInt(idString);
        Person person = Database.review(id);
        if (person == null) {
            return "No such person.";
        }

        // Modify the properties to the new input values
        String name = request.queryParams("name");
        if (name != null) {
            person.setName(name);
        }
        String occupation = request.queryParams("occupation");
        if (occupation != null) {
            person.setOccupation(occupation);
        }
        String ageString = request.queryParams("age");
        if (ageString != null) {
            person.setAge(Integer.parseInt(ageString));
        }

        // Update the new change to the database, and present the new
        Database.update(person);
        return gson.toJson(person);
    }

    private static String delete(Request request) {
        String idString = request.queryParams("id");
        if (idString == null) {
            return ("Need an id.");
        }

        int id = Integer.parseInt(idString);
        if (Database.delete(id)) {
            return gson.toJson(new Message("Successfully deleted this person."));
        } else {
            return gson.toJson(new Message("Failed to delete this person."));
        }
    }
}