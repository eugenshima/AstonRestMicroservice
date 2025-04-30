package org.example.controller.Impl;

import org.example.controller.NoteController;
import org.example.controller.TagController;
import org.example.dto.TagDTO;
import org.example.service.Impl.NoteServiceImpl;
import org.example.service.Impl.TagServiceImpl;
import org.example.service.NoteService;
import org.example.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class TagControllerImpl implements TagController{
    private static final Logger log = LoggerFactory.getLogger(TagControllerImpl.class);
    private final TagService tagService;
    private static final TagController INSTANCE = new TagControllerImpl();

    public TagControllerImpl() {
        this.tagService = TagServiceImpl.getInstance();
    }

    public static TagController getInstance() {
        return INSTANCE;
    }

    @Override
    public UUID CreateTag(TagDTO tagDTO) {
        log.info("CreateTag");
        return tagService.CreateTag(tagDTO);
    }

    @Override
    public List<TagDTO> FindAllTags() {
        return List.of();
    }

    @Override
    public TagDTO FindTagByID(UUID tagID) {
        return null;
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
        log.info("AddTagToNote");
        tagService.AddTagToNote(NoteID, tagID);
    }

    @Override
    public void RemoveTagFromNote(UUID NoteID, UUID tagID) {

    }
}
