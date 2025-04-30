package org.example.service.Impl;

import org.example.dto.TagDTO;
import org.example.entity.Note;
import org.example.entity.Tag;
import org.example.mapper.NoteMapper;
import org.example.mapper.TagMapper;
import org.example.repository.Impl.NoteRepositoryImpl;
import org.example.repository.Impl.TagRepositoryImpl;
import org.example.repository.NoteRepository;
import org.example.repository.TagRepository;
import org.example.service.NoteService;
import org.example.service.TagService;
import org.example.utils.CustomExceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TagServiceImpl implements TagService{
    private static final Logger log = LoggerFactory.getLogger(TagServiceImpl.class);
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    private static final TagService INSTANCE = new TagServiceImpl();
    public TagServiceImpl() {
        this.tagRepository = TagRepositoryImpl.getInstance();
        this.tagMapper = new TagMapper();
    }
    public static TagService getInstance() {return INSTANCE;}

    @Override
    public UUID CreateTag(TagDTO tagDTO) {
        try {
            Tag tag = tagMapper.toEntity(tagDTO);
            log.info("Tag: " + tag + " added!");
            return tagRepository.CreateTag(tag);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create tag: " + e.getMessage(), e);
        }
    }

    @Override
    public List<TagDTO> FindAllTags() {
        return List.of();
    }

    @Override
    public TagDTO FindTagByID(UUID tagID) {
        try {
            Optional<Tag> tag = tagRepository.FindTagByID(tagID);
            if (tag.isPresent()) {
                log.info("User found: {}", tag.get());
                return tagMapper.toDto(tag.get());
            } else {
                throw new UserNotFoundException("User not found with ID: " + tagID);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create note: " + e.getMessage(), e);
        }
    }

    @Override
    public UUID UpdateTag(TagDTO tagDTO) {
        return null;
    }

    @Override
    public boolean DeleteTag(UUID TagID) {
        return false;
    }

    @Override
    public void AddTagToNote(UUID NoteID, UUID tagID) {
        try {
            log.info("Note + tag IDs: " + NoteID + " & " + tagID);
            tagRepository.AddTagToNote(NoteID, tagID);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add tag to a note: " + e.getMessage(), e);
        }
    }

    @Override
    public void RemoveTagFromNote(UUID NoteID, UUID tagID) {

    }
}
