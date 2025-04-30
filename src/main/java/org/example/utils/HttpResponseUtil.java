package org.example.utils;



import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpResponseUtil {

    public static void sendResponse(HttpServletResponse resp, Object data, int statusCode) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(statusCode);
        resp.getWriter().write(data.toString());
    }

    public static void sendError(HttpServletResponse resp, String message, int statusCode) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(statusCode);
        resp.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}