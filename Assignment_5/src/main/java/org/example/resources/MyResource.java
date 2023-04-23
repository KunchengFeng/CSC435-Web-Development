package org.example.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import org.example.objects.Person;
import org.example.objects.PersonDao;
import org.jdbi.v3.core.Jdbi;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import io.dropwizard.Configuration;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;

import java.util.List;

@Path("/PopManager")
public class MyResource {
    private final PersonDao dao;
    private final Gson gson;

    public MyResource(Jdbi jdbi) {
        this.dao = jdbi.onDemand(PersonDao.class);
        this.gson = new Gson();
    }

    // For testing out the server
    @GET
    @Path("/hello")
    public String hello() {
        return "Hello";
    }

    // Crete
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(
            @FormParam("name") String name,
            @FormParam("occupation") String occupation,
            @FormParam("age") String ageS
    ) {
        if (name == null) {
            return "Person needs a name!";
        }

        if (occupation == null) {
            occupation = "unemployed";
        }

        int age;
        if (ageS == null) {
            age = -1;
        } else {
            age = Integer.parseInt(ageS);
        }

        // Write to database and get id
        int id = dao.create(new Person(age, name, occupation));
        // Make the response with a person instance with new ID;
        Person person = new Person(id, age, name, occupation);
        return gson.toJson(person);
    }

    // Review all
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> listPopulation() {
        return dao.list();
    }

    // Review One
    @POST
    @Path("/review")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String review(@FormParam("id") String idS) {
        int id;
        if (idS == null) {
            return "Need an id!";
        } else {
            id = Integer.parseInt(idS);
        }

        Person person = dao.findById(id);
        if (person == null) {
            return "No such person.";
        } else {
            return gson.toJson(person);
        }
    }

    // update
    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(
            @FormParam("id") String idS,
            @FormParam("age") String ageS,
            @FormParam("name") String name,
            @FormParam("occupation") String occupation
    ) {
        // Try to get the original person from database
        int id;
        if (idS == null) {
            return "Need an id!";
        } else {
            id = Integer.parseInt(idS);
        }
        Person person = dao.findById(id);

        if (person == null) {return "No such person.";}

        // Update any field that have new value (except id)
        // Update age
        if (ageS != null) {
            int age = Integer.parseInt(ageS);
            person.setAge(age);
        }
        // Update name
        if (name != null) {
            person.setName(name);
        }
        // Update Occupation
        if (occupation != null) {
            person.setOccupation(occupation);
        }

        // Write back into the database
        dao.update(person);

        // Return the updated version
        return gson.toJson(person);
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@FormParam("id") String idS) {
        int id;
        if (idS == null) {
            return "Need an id!";
        } else {
            id = Integer.parseInt(idS);
            dao.deleteById(id);
            return "Operation completed.";
        }
    }
}
