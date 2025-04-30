package org.example.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.Impl.NoteControllerImpl;
import org.example.controller.Impl.TagControllerImpl;
import org.example.controller.NoteController;
import org.example.controller.TagController;
import org.example.dto.NoteDTO;
import org.example.dto.NoteTagDTO;
import org.example.dto.TagDTO;
import org.example.utils.HttpResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/tag/*")
public class TagServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(TagServlet.class);
    private TagController tagController;

    @Override
    public void init() {
        log.info("Initializing TagServlet");
        this.tagController = TagControllerImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();

        try {
            if (path.equals("/create_tag")) {
                TagDTO tagDTO = new ObjectMapper().readValue(req.getReader(), TagDTO.class);
                UUID tagID = tagController.CreateTag(tagDTO);

                new ObjectMapper().writeValue(resp.getWriter(), tagID);
            } else if(path.equals("/add_tag_to_note")) {
                NoteTagDTO noteTagDTO = new ObjectMapper().readValue(req.getReader(), NoteTagDTO.class);
                tagController.AddTagToNote(noteTagDTO.getNoteId(), noteTagDTO.getTagId());

                new ObjectMapper().writeValue(resp.getWriter(), "Note - " + noteTagDTO.getNoteId() + " was fixed to a " + noteTagDTO.getTagId() + " tag");
            }
        } catch (JsonProcessingException e) {
            HttpResponseUtil.sendError(resp, "Invalid JSON format", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            HttpResponseUtil.sendError(resp, "Failed to create tag: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }
}
