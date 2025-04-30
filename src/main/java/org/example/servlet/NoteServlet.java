package org.example.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.Impl.NoteControllerImpl;
import org.example.controller.NoteController;
import org.example.dto.NoteDTO;
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

@WebServlet("/note/*")
public class NoteServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(NoteServlet.class);
    private NoteController noteController;
    @Override
    public void init() {
        log.info("Initializing NoteServlet");
        this.noteController = NoteControllerImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        try {
            if (path.equals("/get_note")) {
                UUID uuid = UUID.fromString(req.getParameter("id"));

                NoteDTO noteDTO = noteController.GetNoteByID(uuid);
                new ObjectMapper().writeValue(resp.getWriter(), noteDTO);
            } else if (path.equals("/get_notes_by_tag")) {
                UUID uuid = UUID.fromString(req.getParameter("id"));

                List<NoteDTO> notesByTag = noteController.GetAllNoteByTag(uuid);
                log.info("Retrieved {} users", notesByTag.size());

                new ObjectMapper().writeValue(resp.getWriter(), notesByTag);
            }
        } catch (IllegalArgumentException e) {
            HttpResponseUtil.sendError(resp, "Invalid UUID format" + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            HttpResponseUtil.sendError(resp, "Failed to get notes: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();

        try {
            if (path.equals("/add_note")) {
                NoteDTO noteDTO = new ObjectMapper().readValue(req.getReader(), NoteDTO.class);
                UUID noteID = noteController.AddNote(noteDTO);

                new ObjectMapper().writeValue(resp.getWriter(), noteID);
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
            if (path.equals("/update_note")) {
                NoteDTO noteDTO = new ObjectMapper().readValue(req.getReader(), NoteDTO.class);
                UUID noteID = noteController.UpdateNote(noteDTO);

                new ObjectMapper().writeValue(resp.getWriter(), noteID);
            }
        } catch (JsonProcessingException e) {
            HttpResponseUtil.sendError(resp, "Invalid JSON format", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            HttpResponseUtil.sendError(resp, "Failed to create user: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();
        try {
            if (path.equals("/delete_note")) {
                UUID req_uuid = UUID.fromString(req.getParameter("id"));
                boolean deleted = noteController.DeleteNote(req_uuid);

                new ObjectMapper().writeValue(resp.getWriter(), "id - " + req_uuid + ", deleted - " + deleted);
            }
        } catch (IllegalArgumentException e) {
            HttpResponseUtil.sendError(resp, "Invalid UUID format" + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            HttpResponseUtil.sendError(resp, "Failed to delete note: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
