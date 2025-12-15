package com.projuris.gamescatalog.application.mapper;

import com.projuris.gamescatalog.application.dto.GameResponseDTO;
import com.projuris.gamescatalog.domain.model.Game;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para conversão entre entidades de domínio e DTOs
 * Usa MapStruct para geração automática de código de mapeamento para a camada
 * de aplicação
 */
@Mapper(componentModel = "spring")
public interface GameMapper {

    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    GameResponseDTO toResponseDTO(Game game);
}
