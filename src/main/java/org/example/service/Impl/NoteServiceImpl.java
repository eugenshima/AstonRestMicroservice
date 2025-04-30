package org.example.service.Impl;

import org.example.dto.NoteDTO;
import org.example.entity.Note;
import org.example.entity.User;
import org.example.mapper.NoteMapper;
import org.example.repository.Impl.NoteRepositoryImpl;
import org.example.repository.NoteRepository;
import org.example.service.NoteService;
import org.example.utils.CustomExceptions.DatabaseException;
import org.example.utils.CustomExceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NoteServiceImpl implements NoteService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    private static final NoteService INSTANCE = new NoteServiceImpl();
    public NoteServiceImpl() {
        this.noteRepository = NoteRepositoryImpl.getInstance();
        this.noteMapper = new NoteMapper();
    }
    public static NoteService getInstance() {return INSTANCE;}

    @Override
    public List<NoteDTO> GetAllNoteByTag(UUID tagID) {
        log.info("Looking for notes with particular tag");
        try {
            List<Note> notes = noteRepository.GetAllNoteByTag(tagID);
            log.info("Users found: {}", notes);
            return notes.stream()
                    .map(noteMapper::toDto)
                    .toList();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch user from database", e);
        }
    }

    @Override
    public NoteDTO GetNoteByID(UUID noteID) {
        try {
            Optional<Note> note = noteRepository.GetNoteByID(noteID);
            if (note.isPresent()) {
                log.info("User found: {}", note.get());
                return noteMapper.toDto(note.get());
            } else {
                throw new UserNotFoundException("User not found with ID: " + noteID);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create note: " + e.getMessage(), e);
        }
    }

    @Override
    public UUID AddNote(NoteDTO noteDTO) {
        try {
            Note note = noteMapper.toEntity(noteDTO);
            log.info("Note: " + note + " added!");
            return noteRepository.AddNote(note);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create note: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean DeleteNote(UUID noteID) {
        try {
            return noteRepository.DeleteNote(noteID);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete note: " + e.getMessage(), e);
        }
    }

    @Override
    public UUID UpdateNote(NoteDTO noteDTO) {
        try {
            Note note = noteMapper.toEntity(noteDTO);
            log.info("updating this info - " + note);
            return noteRepository.UpdateNote(note);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create note: " + e.getMessage(), e);
        }
    }

}
