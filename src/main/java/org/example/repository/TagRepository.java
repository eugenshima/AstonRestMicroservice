package org.example.repository;

import org.example.entity.Tag;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagRepository {
    UUID CreateTag(Tag tag) throws SQLException;
    List<Tag> FindAllTags() throws SQLException;
    Optional<Tag> FindTagByID(UUID tagID) throws SQLException;
    UUID UpdateTag(Tag tag) throws SQLException;
    boolean DeleteTag(UUID TagID) throws SQLException;
    void AddTagToNote(UUID NoteID, UUID tagID) throws SQLException;
    void RemoveTagFromNote(UUID NoteID, UUID tagID) throws SQLException;
}
