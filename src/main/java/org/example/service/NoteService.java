package org.example.service;

import org.example.dto.NoteDTO;
import org.example.entity.Note;

import java.util.List;
import java.util.UUID;

public interface NoteService {
    List<NoteDTO> GetAllNoteByTag(UUID tagID);
    NoteDTO GetNoteByID(UUID noteID);
    UUID AddNote(NoteDTO noteDTO);
    boolean DeleteNote(UUID noteID);
    UUID UpdateNote(NoteDTO noteDTO);
}
