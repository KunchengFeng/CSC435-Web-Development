package population;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired PersonRepository personRepository;

    @Autowired
    private PersonModelAssembler personModelAssembler;

    // Test Create
    @Test
    public void testNewPerson() throws Exception {
        // Person to be used for testing
        Person person = new Person(23, "Kevin", "Feng", "Student");

        // Test endpoint HTTP response
        mockMvc.perform(post("/population")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated());

        // Test if actually saved
        List<Person> people = personRepository.findAll();
        assertEquals(7, people.size());
        Person saved = people.get(6);
        assertEquals(23, saved.getAge());
        assertEquals("Kevin", saved.getFirstName());
        assertEquals("Feng", saved.getLastName());
        assertEquals("Student", saved.getOccupation());
    }

    // Test Review One
    @Test
    public void testGetOne() throws Exception {
        // Save a new person into the database
        Person saved = personRepository.save(new Person(23, "Kevin", "Feng", "Student"));

        // Retrieve that person and check result
        MvcResult result = mockMvc.perform(get("/population/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String expected = "{" +
                "\"id\": " + saved.getId() + "," +
                "\"age\": 23," +
                "\"firstName\": \"Kevin\"," +
                "\"lastName\": \"Feng\"," +
                "\"occupation\": \"Student\"," +
                "\"name\": \"Kevin Feng\"," +
                "\"_links\": {" +
                    "\"self\": {" +
                        "\"href\": \"http://localhost/population/" + saved.getId() + "\"" +
                        "}," +
                    "\"population\": {" +
                        "\"href\": \"http://localhost/population\"" +
                        "}" +
                    "}" +
                "}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    // No test for getAll(), can't convert the JsonPathResultMatcher to ResultMatcher

    // Test Update
    @Test
    public void testReplacePerson() throws Exception {
        // Replace the third preloaded guy
        Person newPerson = new Person(16, "Victoria", "of Hangover", "Princess");

        MvcResult result = mockMvc.perform(put("/population/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPerson)))
                .andExpect(status().isCreated())
                .andReturn();

        String expected = "{" +
                "\"id\":3," +
                "\"age\":16," +
                "\"firstName\":\"Victoria\"," +
                "\"lastName\":\"of Hangover\"," +
                "\"occupation\":\"Princess\"," +
                "\"name\":\"Victoria of Hangover\"," +
                "\"_links\":{" +
                    "\"self\":{" +
                        "\"href\":\"http://localhost/population/3\"" +
                    "}," +
                    "\"population\":{" +
                        "\"href\":\"http://localhost/population\"" +
                    "}" +
                "}" +
                "}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    // Test Delete
    @Test
    public void testDeletePerson() throws Exception {
        // Delete a preloaded person and check if not found
        mockMvc.perform(delete("/population/3"))
                .andExpect(status().isNoContent());
    }
}
