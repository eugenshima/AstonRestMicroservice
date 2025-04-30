package org.example.dto;

import lombok.*;
import org.example.entity.Note;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {
    private UUID id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<UUID> tagIDs; //Для связи Many-to-many (Только идентификаторы)
}
