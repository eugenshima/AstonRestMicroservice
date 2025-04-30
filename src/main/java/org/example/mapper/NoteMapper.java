package org.example.mapper;

import org.example.dto.NoteDTO;
import org.example.entity.Note;
import org.example.entity.Tag;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

public class NoteMapper {
    public NoteDTO toDto(Note note) {
        return NoteDTO.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .tagIDs(note.getTags() != null ?
                        note.getTags().stream()
                                .map(Tag::getId)
                                .collect(Collectors.toList()) :
                        Collections.emptyList())
                .build();
    }

    public Note toEntity(NoteDTO noteDTO) {
        return Note.builder()
                .id(noteDTO.getId())
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .createdAt(noteDTO.getCreatedAt() != null ?
                        noteDTO.getCreatedAt() :
                        LocalDateTime.now())
                .tags(noteDTO.getTagIDs() != null ?
                        noteDTO.getTagIDs().stream()
                                .map(tagId -> Tag.builder().id(tagId).build())
                                .collect(Collectors.toList()) :
                        Collections.emptyList())
                .build();
    }
}
