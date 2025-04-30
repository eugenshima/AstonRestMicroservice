package org.example.repository.Impl;

import org.example.entity.Tag;
import org.example.entity.User;
import org.example.repository.TagRepository;
import org.example.utils.DBConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TagRepositoryImpl implements TagRepository{
    private static final Logger log = LoggerFactory.getLogger(TagRepositoryImpl.class);
    private static final TagRepository INSTANCE = new TagRepositoryImpl();

    public TagRepositoryImpl() {}

    public static TagRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public UUID CreateTag(Tag tag) throws SQLException {
        String createTag = "INSERT INTO notetag.tags (id, name) VALUES (?, ?)";

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(createTag)) {

            UUID tagId = UUID.randomUUID();

            statement.setObject(1, tagId);
            statement.setString(2, tag.getName());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating tag failed, no rows affected.");
            }

            return tagId;
        }
    }

    @Override
    public List<Tag> FindAllTags() throws SQLException {
        List<Tag> tags = new ArrayList<>();
        String SelectString = "Select id, name from notetag.tags";
        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SelectString);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Tag tag = new Tag();
                String uuidStr = resultSet.getString("id");
                UUID uuid = UUID.fromString(uuidStr);
                tag.setId(uuid);
                tag.setName(resultSet.getString("name"));
                tags.add(tag);
            }
        }
        return tags;
    }

    @Override
    public Optional<Tag> FindTagByID(UUID tagID) throws SQLException {
        return Optional.empty();
    }

    @Override
    public UUID UpdateTag(Tag tag) throws SQLException {
        return null;
    }

    @Override
    public boolean DeleteTag(UUID TagID) throws SQLException {
        return false;
    }

    @Override
    public void AddTagToNote(UUID noteID, UUID tagID) throws SQLException {
        String addTagToNote = "INSERT INTO notetag.note_tag (note_id, tag_id) VALUES (?, ?)";

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addTagToNote)) {

            preparedStatement.setObject(1, noteID);
            preparedStatement.setObject(2, tagID);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Adding tag to note failed, no rows affected.");
            }
        }
    }

    @Override
    public void RemoveTagFromNote(UUID NoteID, UUID tagID) throws SQLException {

    }

}
