package org.example;

import org.example.dto.NoteDTO;
import org.example.entity.Note;
import org.example.mapper.NoteMapper;
import org.example.repository.NoteRepository;
import org.example.repository.TagRepository;
import org.example.service.Impl.NoteServiceImpl;
import org.example.service.NoteService;
import org.example.utils.CustomExceptions.DatabaseException;
import org.example.utils.CustomExceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NoteMapper noteMapper;

    @InjectMocks
    private NoteServiceImpl noteService;

    // Тест 1: Успешное получение заметок по тегу
    @Test
    void getAllNoteByTag_shouldReturnListOfNoteDTOs_whenNotesExist() throws SQLException {
        UUID tagId = UUID.randomUUID();
        Note note1 = new Note(UUID.randomUUID(), "Title1", "Content1", LocalDateTime.now(), null);
        Note note2 = new Note(UUID.randomUUID(), "Title2", "Content2", LocalDateTime.now(), null);
        List<Note> notes = Arrays.asList(note1, note2);

        NoteDTO noteDTO1 = new NoteDTO(note1.getId(), "Title1", "Content1", note1.getCreatedAt(), null);
        NoteDTO noteDTO2 = new NoteDTO(note2.getId(), "Title2", "Content2", note2.getCreatedAt(), null);

        when(noteRepository.GetAllNoteByTag(tagId)).thenReturn(notes);
        when(noteMapper.toDto(note1)).thenReturn(noteDTO1);
        when(noteMapper.toDto(note2)).thenReturn(noteDTO2);

        List<NoteDTO> result = noteService.GetAllNoteByTag(tagId);

        assertEquals(2, result.size());
        assertEquals(noteDTO1, result.get(0));
        assertEquals(noteDTO2, result.get(1));
        verify(noteRepository).GetAllNoteByTag(tagId);
    }

    // Тест 2: Получение заметок по тегу с ошибкой БД
    @Test
    void getAllNoteByTag_shouldThrowDatabaseException_whenSQLExceptionOccurs() throws SQLException {
        UUID tagId = UUID.randomUUID();
        when(noteRepository.GetAllNoteByTag(tagId)).thenThrow(new SQLException("DB error"));

        assertThrows(DatabaseException.class, () -> noteService.GetAllNoteByTag(tagId));
    }

    // Тест 3: Успешное получение заметки по ID
    @Test
    void getNoteById_shouldReturnNoteDTO_whenNoteExists() throws SQLException {
        UUID noteId = UUID.randomUUID();
        Note note = new Note(noteId, "Title", "Content", LocalDateTime.now(), null);
        NoteDTO noteDTO = new NoteDTO(noteId, "Title", "Content", note.getCreatedAt(), null);

        when(noteRepository.GetNoteByID(noteId)).thenReturn(Optional.of(note));
        when(noteMapper.toDto(note)).thenReturn(noteDTO);

        NoteDTO result = noteService.GetNoteByID(noteId);

        assertEquals(noteDTO, result);
        verify(noteRepository).GetNoteByID(noteId);
    }

    // Тест 4: Попытка получить несуществующую заметку
    @Test
    void getNoteById_shouldThrowUserNotFoundException_whenNoteNotExists() throws SQLException {
        UUID noteId = UUID.randomUUID();
        when(noteRepository.GetNoteByID(noteId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> noteService.GetNoteByID(noteId));
    }

    // Тест 5: Добавление новой заметки
    @Test
    void addNote_shouldReturnUUID_whenNoteIsValid() throws SQLException {
        NoteDTO noteDTO = new NoteDTO(null, "Title", "Content", null, null);
        Note note = new Note(null, "Title", "Content", null, null);
        Note savedNote = new Note(UUID.randomUUID(), "Title", "Content", LocalDateTime.now(), null);

        when(noteMapper.toDto(note)).thenReturn(noteDTO);
        when(noteRepository.AddNote(note)).thenReturn(savedNote.getId());

        UUID result = noteService.AddNote(noteDTO);

        assertEquals(savedNote.getId(), result);
        verify(noteRepository).AddNote(note);
    }

    // Тест 6: Ошибка при добавлении заметки
    @Test
    void addNote_shouldThrowRuntimeException_whenSQLExceptionOccurs() throws SQLException {
        NoteDTO noteDTO = new NoteDTO(null, "Title", "Content", null, null);
        Note note = new Note(null, "Title", "Content", null, null);

        when(noteMapper.toDto(note)).thenReturn(noteDTO);
        when(noteRepository.AddNote(note)).thenThrow(new SQLException("DB error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> noteService.AddNote(noteDTO));
        assertTrue(exception.getMessage().contains("Failed to create note"));
    }

    // Тест 7: Успешное удаление заметки
    @Test
    void deleteNote_shouldReturnTrue_whenNoteExists() throws SQLException {
        UUID noteId = UUID.randomUUID();
        when(noteRepository.DeleteNote(noteId)).thenReturn(true);

        boolean result = noteService.DeleteNote(noteId);

        assertTrue(result);
        verify(noteRepository).DeleteNote(noteId);
    }

    // Тест 8: Ошибка при удалении заметки
    @Test
    void deleteNote_shouldThrowRuntimeException_whenSQLExceptionOccurs() throws SQLException {
        UUID noteId = UUID.randomUUID();
        when(noteRepository.DeleteNote(noteId)).thenThrow(new SQLException("DB error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> noteService.DeleteNote(noteId));
        assertTrue(exception.getMessage().contains("Failed to delete note"));
    }

    // Тест 9: Успешное обновление заметки
    @Test
    void updateNote_shouldReturnUUID_whenUpdateIsSuccessful() throws SQLException {
        UUID noteId = UUID.randomUUID();
        NoteDTO noteDTO = new NoteDTO(noteId, "New Title", "New Content", null, null);
        Note note = new Note(noteId, "New Title", "New Content", null, null);

        when(noteMapper.toDto(note)).thenReturn(noteDTO);
        when(noteRepository.UpdateNote(note)).thenReturn(noteId);

        UUID result = noteService.UpdateNote(noteDTO);

        assertEquals(noteId, result);
        verify(noteRepository).UpdateNote(note);
    }

    // Тест 10: Ошибка при обновлении заметки
    @Test
    void updateNote_shouldThrowRuntimeException_whenSQLExceptionOccurs() throws SQLException {
        UUID noteId = UUID.randomUUID();
        NoteDTO noteDTO = new NoteDTO(noteId, "New Title", "New Content", null, null);
        Note note = new Note(noteId, "New Title", "New Content", null, null);

        when(noteMapper.toDto(note)).thenReturn(noteDTO);
        when(noteRepository.UpdateNote(note)).thenThrow(new SQLException("DB error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> noteService.UpdateNote(noteDTO));
        assertTrue(exception.getMessage().contains("Failed to create note"));
    }
}

