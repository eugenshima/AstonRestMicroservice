package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    private UUID id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<Tag> tags; // Связь Many-to-Many (реальные объекты)
}
