package com.projuris.gamescatalog.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    private String title;

    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    private String description;

    @NotBlank(message = "Desenvolvedora é obrigatória")
    @Size(max = 100, message = "Desenvolvedora deve ter no máximo 100 caracteres")
    private String developer;

    @Size(max = 100, message = "Publicadora deve ter no máximo 100 caracteres")
    private String publisher;

    @NotBlank(message = "Gênero é obrigatório")
    @Size(max = 50, message = "Gênero deve ter no máximo 50 caracteres")
    private String genre;

    @NotNull(message = "Ano de lançamento é obrigatório")
    @Min(value = 1970, message = "Ano de lançamento deve ser maior que 1970")
    @Max(value = 2100, message = "Ano de lançamento deve ser menor que 2100")
    private Integer releaseYear;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.0", message = "Preço deve ser maior ou igual a zero")
    private Double price;
}
