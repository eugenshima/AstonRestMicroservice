package org.example.mapper;

import org.example.dto.TagDTO;
import org.example.entity.Note;
import org.example.entity.Tag;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class TagMapper {
    public TagDTO toDto(Tag tag) {
        if (tag == null) {
            return null;
        }

        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .noteIds(Optional.ofNullable(tag.getNotes())
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(Objects::nonNull)
                        .map(Note::getId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .build();
    }

    public Tag toEntity(TagDTO dto) {
        if (dto == null) {
            return null;
        }

        return Tag.builder()
                .id(dto.getId() != null ? dto.getId() : UUID.randomUUID())
                .name(dto.getName())
                .notes(Optional.ofNullable(dto.getNoteIds())
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(Objects::nonNull)
                        .map(noteId -> Note.builder().id(noteId).build())
                        .collect(Collectors.toList()))
                .build();
    }
}
