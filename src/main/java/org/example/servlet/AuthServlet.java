package org.example.servlet;

import javax.servlet.DispatcherType;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
        servletNames = {"UserServlet"},        // Или только для конкретных сервлетов
        dispatcherTypes = DispatcherType.REQUEST  // Тип запроса (по умолчанию REQUEST)
)
@WebServlet("/hello")
public class AuthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // Формируем JSON-ответ
            String jsonResponse = "{\"message\": \"Привет, это JSON формат\", \"author\": \"Eugene Shymanski\"}";

            // Отправляем успешный ответ (200 OK)
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonResponse);

        } catch (IOException e) {
            // Ошибка ввода/вывода (например, проблемы с Writer)
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Ошибка при формировании ответа\", \"details\": \"Попробуйте позже\"}");
            e.printStackTrace(); // Логируем для разработчика

        } catch (Exception e) {
            // Любая другая непредвиденная ошибка
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Внутренняя ошибка сервера\"}");
            e.printStackTrace(); // Логируем полный stacktrace
        }
    }
}
