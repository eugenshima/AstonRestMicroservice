package org.example.repository.Impl;

import org.example.entity.Note;
import org.example.entity.User;
import org.example.repository.NoteRepository;
import org.example.utils.CustomExceptions.DatabaseException;
import org.example.utils.DBConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NoteRepositoryImpl extends DBConnectionFactory implements NoteRepository {
    private static final Logger log = LoggerFactory.getLogger(NoteRepositoryImpl.class);
    private static final NoteRepository INSTANCE = new NoteRepositoryImpl();

    public NoteRepositoryImpl() {}

    public static NoteRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Note> GetAllNoteByTag(UUID tagID) throws SQLException {
        String getAllNoteByTag = "SELECT id, title, content, created_at FROM notetag.notes JOIN notetag.note_tag ON notes.id = note_tag.note_id WHERE note_tag.tag_id=?";

        try (Connection connection = DBConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getAllNoteByTag)) {

            preparedStatement.setObject(1, tagID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Note> notes = new ArrayList<>();

                while (resultSet.next()) {
                    Note note = Note.builder()
                            .id((UUID) resultSet.getObject("id"))
                            .title(resultSet.getString("title"))
                            .content(resultSet.getString("content"))
                            .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                            .build();

                    notes.add(note);
                }

                return notes;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to get notes by tag: " + e.getMessage(), e);
        }

    }

    @Override
    public Optional<Note> GetNoteByID(UUID noteID) throws SQLException {
        String sql = "SELECT id, title, content, created_at FROM notetag.notes WHERE id=?";

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, noteID);  // Подставляем UUID в SQL-запрос

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Note note = new Note();
                    note.setId(resultSet.getObject("id", UUID.class));
                    note.setTitle(resultSet.getString("title"));
                    note.setContent(resultSet.getString("content"));
                    note.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                    log.info("note: " + note);
                    return Optional.of(note);
                }
            }
        }
        return Optional.empty();  // Если пользователь не найден
    }

    @Override
    public UUID AddNote(Note note) throws SQLException {
        String addNote = "INSERT INTO notetag.notes (id, title, content, created_at) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(addNote)) {

            UUID noteID = UUID.randomUUID();

            preparedStatement.setObject(1, noteID);
            preparedStatement.setString(2, note.getTitle());
            preparedStatement.setString(3, note.getContent());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating note failed, no rows affected.");
            }
            return noteID;
        }
    }

    @Override
    public boolean DeleteNote(UUID noteID) throws SQLException {
        String deleteNote = "DELETE FROM notetag.notes WHERE id=?";
        try(Connection connection = DBConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteNote)) {

            preparedStatement.setObject(1, noteID);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to delete Note");
            }
            return true;
        }
    }

    @Override
    public UUID UpdateNote(Note note) throws SQLException {
        String updateNote = "UPDATE notetag.notes SET title=?, content=? WHERE id=?";
        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateNote)) {
            preparedStatement.setString(1, note.getTitle());
            preparedStatement.setString(2, note.getContent());
            preparedStatement.setObject(3, note.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Note not found or not updated");
            }
        }
        return note.getId();
    }


}
