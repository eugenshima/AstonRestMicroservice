package org.example.controller.Impl;

import org.example.controller.NoteController;
import org.example.dto.NoteDTO;
import org.example.entity.Note;
import org.example.service.Impl.NoteServiceImpl;
import org.example.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class NoteControllerImpl implements NoteController {
    private static final Logger log = LoggerFactory.getLogger(NoteControllerImpl.class);
    private final NoteService noteService;
    private static final NoteController INSTANCE = new NoteControllerImpl();

    public NoteControllerImpl() {
        this.noteService = NoteServiceImpl.getInstance();
    }

    public static NoteController getInstance() {
        return INSTANCE;
    }

    @Override
    public List<NoteDTO> GetAllNoteByTag(UUID tagID) {
        log.info("GetAllNoteByTag");
        return noteService.GetAllNoteByTag(tagID);
    }

    @Override
    public NoteDTO GetNoteByID(UUID noteID) {
        log.info("GetNoteByID");
        return noteService.GetNoteByID(noteID);
    }

    @Override
    public UUID AddNote(NoteDTO noteDTO) {
        log.info("CreateUser");
        return noteService.AddNote(noteDTO);
    }

    @Override
    public boolean DeleteNote(UUID noteID) {
        log.info("DeleteNote");
        return noteService.DeleteNote(noteID);
    }

    @Override
    public UUID UpdateNote(NoteDTO noteDTO) {
        log.info("UpdateNote");
        return noteService.UpdateNote(noteDTO);
    }

}
