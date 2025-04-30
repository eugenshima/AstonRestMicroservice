package org.example.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.controller.Impl.UserControllerImpl;
import org.example.controller.UserController;
import org.example.dto.UserDTO;
import org.example.utils.HttpResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.example.utils.HttpResponseUtil.sendResponse;

@WebServlet("/User/*")
public class UserServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);
    private UserController userController;

    @Override
    public void init() {
        log.info("Initializing UserServlet");
        this.userController = UserControllerImpl.getInstance(); // Фабричный метод
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();
        try {
            if (path.equals("/get_all_users")) {
                List<UserDTO> allUsers = userController.FindAllUsers();
                log.info("Retrieved {} users", allUsers.size());
                resp.getWriter().write(allUsers.toString());
            }
            else if (path.equals("/get_user_by_id")) {
                UUID uuid = UUID.fromString(req.getParameter("id"));

                UserDTO user = userController.FindUserByID(uuid);
                HttpResponseUtil.sendResponse(resp, user, HttpServletResponse.SC_OK);
            }
        } catch (IllegalArgumentException e) {
            HttpResponseUtil.sendError(resp, "Invalid UUID format" + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error in GET request", e);
            HttpResponseUtil.sendError(resp, "Invalid endpoint", HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();

        try {
            if (path.equals("/create_user")) {
                UserDTO userDTO = new ObjectMapper().readValue(req.getReader(), UserDTO.class);

                UUID createdID = userController.CreateUser(userDTO);
                new ObjectMapper().writeValue(resp.getWriter(), createdID);
            }
        } catch (JsonProcessingException e) {
            HttpResponseUtil.sendError(resp, "Invalid JSON format", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            HttpResponseUtil.sendError(resp, "Failed to create user: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();
        try {
            if(path.equals("/update_user")) {
                UserDTO userDTO = new ObjectMapper().readValue(req.getReader(), UserDTO.class);

                UUID updatedID = userController.UpdateUser(userDTO);
                new ObjectMapper().writeValue(resp.getWriter(), updatedID);
            }
        } catch (Exception e) {
            HttpResponseUtil.sendError(resp, "Failed to update user: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();
        try {
            if(path.equals("/delete_user")) {
                UUID uuid = UUID.fromString(req.getParameter("id"));
                boolean deleted = userController.DeleteUserByID(uuid);

                new ObjectMapper().writeValue(resp.getWriter(), "deleted user - " + uuid + " ---- deleted - " + deleted);
            }
        } catch (IllegalArgumentException e) {
            HttpResponseUtil.sendError(resp, "Invalid UUID format" + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            HttpResponseUtil.sendError(resp, "Failed to delete user: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
