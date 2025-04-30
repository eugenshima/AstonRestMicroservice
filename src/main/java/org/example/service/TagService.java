package org.example.service;

import org.example.dto.TagDTO;
import org.example.entity.Tag;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagService {
    UUID CreateTag(TagDTO tagDTO);
    List<TagDTO> FindAllTags();
    TagDTO FindTagByID(UUID tagID);
    UUID UpdateTag(TagDTO tagDTO);
    boolean DeleteTag(UUID TagID);
    void AddTagToNote(UUID NoteID, UUID tagID);
    void RemoveTagFromNote(UUID NoteID, UUID tagID);
}
