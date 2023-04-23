package presentation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ResponseHelper {

    private static final Gson gson = new Gson();

    public static <T> void present(HttpServletResponse response, T object) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        PrintWriter out = response.getWriter();
        out.write(gson.toJson(object));
    }
}
