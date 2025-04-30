package org.example.controller;

import org.example.dto.TagDTO;

import java.util.List;
import java.util.UUID;

public interface TagController {
    UUID CreateTag(TagDTO tagDTO);
    List<TagDTO> FindAllTags();
    TagDTO FindTagByID(UUID tagID);
    UUID UpdateTag(TagDTO tagDTO);
    boolean DeleteTag(UUID TagID);
    void AddTagToNote(UUID NoteID, UUID tagID);
    void RemoveTagFromNote(UUID NoteID, UUID tagID);
}
