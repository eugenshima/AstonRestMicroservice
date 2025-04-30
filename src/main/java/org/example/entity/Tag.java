package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    private UUID id;
    private String name;

    // Связь Many-to-Many (реальные объекты)
    @Builder.Default
    private List<Note> notes = new ArrayList<>();
}
