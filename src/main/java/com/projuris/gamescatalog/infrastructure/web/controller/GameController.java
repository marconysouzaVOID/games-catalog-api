package com.projuris.gamescatalog.infrastructure.web.controller;

import com.projuris.gamescatalog.application.dto.GameRequestDTO;
import com.projuris.gamescatalog.application.dto.GameResponseDTO;
import com.projuris.gamescatalog.application.dto.PageResponseDTO;
import com.projuris.gamescatalog.application.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para gerenciamento de games
 * Adapter HTTP que expõe os use cases da camada de aplicação para a camada de
 * infraestrutura
 */
@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Games", description = "API para gerenciamento de catálogo de games")
public class GameController {

    private final CreateGameUseCase createGameUseCase;
    private final GetGameUseCase getGameUseCase;
    private final ListGamesUseCase listGamesUseCase;
    private final UpdateGameUseCase updateGameUseCase;
    private final DeleteGameUseCase deleteGameUseCase;
    private final ListGamesByGenreUseCase listGamesByGenreUseCase;

    @Operation(summary = "Criar um novo game", description = "Cria um novo game no catálogo com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game criado com sucesso", content = @Content(schema = @Schema(implementation = GameResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<GameResponseDTO> create(@Valid @RequestBody GameRequestDTO request) {
        log.info("POST /api/games - Criando novo game: title={}, developer={}",
                request.getTitle(), request.getDeveloper());
        try {
            GameResponseDTO response = createGameUseCase.execute(request);
            log.info("POST /api/games - Game criado com sucesso: id={}, title={}",
                    response.getId(), response.getTitle());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("POST /api/games - Erro ao criar game: title={}", request.getTitle(), e);
            throw e;
        }
    }

    @Operation(summary = "Buscar game por ID", description = "Retorna um game específico baseado no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game encontrado", content = @Content(schema = @Schema(implementation = GameResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Game não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDTO> getById(
            @Parameter(description = "ID do game a ser buscado", required = true) @PathVariable Long id) {
        log.info("GET /api/games/{} - Buscando game por ID", id);
        try {
            GameResponseDTO response = getGameUseCase.execute(id);
            log.debug("GET /api/games/{} - Game encontrado: title={}", id, response.getTitle());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("GET /api/games/{} - Erro ao buscar game", id, e);
            throw e;
        }
    }

    @Operation(summary = "Listar games", description = "Lista games com paginação. Pode ser filtrado por gênero através do parâmetro 'genre'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de games retornada com sucesso", content = @Content(schema = @Schema(implementation = PageResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<PageResponseDTO<GameResponseDTO>> listAll(
            @Parameter(description = "Filtro por gênero do game") @RequestParam(required = false) String genre,
            @Parameter(description = "Número da página (inicia em 0)", example = "0") @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page deve ser maior ou igual a 0") int page,
            @Parameter(description = "Tamanho da página (máximo 100)", example = "20") @RequestParam(defaultValue = "20") @Min(value = 1, message = "Size deve ser maior ou igual a 1") @Max(value = 100, message = "Size deve ser menor ou igual a 100") int size) {
        try {
            if (genre != null && !genre.isEmpty()) {
                PageResponseDTO<GameResponseDTO> response = listGamesByGenreUseCase.execute(genre, page, size);
                log.info("GET /api/games?genre={}&page={}&size={} - Encontrados {} games na página",
                        genre, page, size, response.getContent().size());
                return ResponseEntity.ok(response);
            } else {
                PageResponseDTO<GameResponseDTO> response = listGamesUseCase.execute(page, size);
                log.info("GET /api/games?page={}&size={} - Encontrados {} games na página",
                        page, size, response.getContent().size());
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            log.error("GET /api/games - Erro ao listar games", e);
            throw e;
        }
    }

    @Operation(summary = "Atualizar um game", description = "Atualiza os dados de um game existente baseado no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game atualizado com sucesso", content = @Content(schema = @Schema(implementation = GameResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Game não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<GameResponseDTO> update(
            @Parameter(description = "ID do game a ser atualizado", required = true) @PathVariable Long id,
            @Valid @RequestBody GameRequestDTO request) {
        log.info("PUT /api/games/{} - Atualizando game: title={}", id, request.getTitle());
        try {
            GameResponseDTO response = updateGameUseCase.execute(id, request);
            log.info("PUT /api/games/{} - Game atualizado com sucesso: title={}",
                    id, response.getTitle());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("PUT /api/games/{} - Erro ao atualizar game", id, e);
            throw e;
        }
    }

    @Operation(summary = "Deletar um game", description = "Remove um game do catálogo baseado no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Game deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Game não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do game a ser deletado", required = true) @PathVariable Long id) {
        log.info("DELETE /api/games/{} - Deletando game", id);
        try {
            deleteGameUseCase.execute(id);
            log.info("DELETE /api/games/{} - Game deletado com sucesso", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("DELETE /api/games/{} - Erro ao deletar game", id, e);
            throw e;
        }
    }
}
