package com.projuris.gamescatalog.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String developer;
    private String publisher;
    private String genre;
    private Integer releaseYear;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
