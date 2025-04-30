package org.example.repository;

import org.example.entity.Note;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteRepository {
    List<Note> GetAllNoteByTag(UUID tagID) throws SQLException;
    Optional<Note> GetNoteByID(UUID noteID) throws SQLException;
    UUID AddNote(Note note) throws SQLException;
    boolean DeleteNote(UUID noteID) throws SQLException;
    UUID UpdateNote(Note note) throws SQLException;
}
