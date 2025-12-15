package com.projuris.gamescatalog.infrastructure.persistence.loader;

import com.projuris.gamescatalog.domain.model.Game;
import com.projuris.gamescatalog.domain.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * DataLoader para popular o banco de dados com games iniciais
 * Executa automaticamente na inicialização da aplicação
 * observação: classe foi feita apenas para facilitar a carga de dados iniciais,
 * não é necessária para o funcionamento da aplicação.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GameDataLoader implements CommandLineRunner {

    private final GameRepository gameRepository;
    private final Random random = new Random();

    private static final String[] TITLES = {
            "The Witcher 3: Wild Hunt", "Cyberpunk 2077", "Red Dead Redemption 2",
            "Grand Theft Auto V", "The Last of Us Part II", "God of War",
            "Horizon Zero Dawn", "Assassin's Creed Valhalla", "Call of Duty: Modern Warfare",
            "FIFA 24", "Minecraft", "Fortnite", "Among Us", "Valorant", "League of Legends",
            "Counter-Strike 2", "Apex Legends", "Overwatch 2", "Rocket League", "PUBG",
            "Elden Ring", "Dark Souls III", "Bloodborne", "Sekiro: Shadows Die Twice",
            "Ghost of Tsushima", "Spider-Man", "Spider-Man: Miles Morales", "Uncharted 4",
            "Death Stranding", "Final Fantasy VII Remake", "Persona 5 Royal", "Nier: Automata",
            "Hades", "Celeste", "Hollow Knight", "Ori and the Blind Forest", "Cuphead",
            "Stardew Valley", "Terraria", "Factorio", "RimWorld", "Crusader Kings III",
            "Civilization VI", "Total War: Warhammer III", "XCOM 2", "Divinity: Original Sin 2",
            "Baldur's Gate 3", "Disco Elysium", "The Outer Worlds", "Mass Effect Legendary Edition",
            "Dragon Age: Inquisition", "The Elder Scrolls V: Skyrim", "Fallout 4", "Fallout: New Vegas",
            "Doom Eternal", "Wolfenstein II", "Resident Evil 4", "Resident Evil Village",
            "Silent Hill 2", "Dead Space", "Outlast", "Amnesia: The Dark Descent",
            "Half-Life: Alyx", "Portal 2", "The Stanley Parable", "What Remains of Edith Finch",
            "Gone Home", "Firewatch", "Life is Strange", "The Walking Dead", "Telltale's The Wolf Among Us",
            "Detroit: Become Human", "Heavy Rain", "Beyond: Two Souls", "Until Dawn",
            "Little Nightmares", "Inside", "Limbo", "Braid", "The Witness", "Return of the Obra Dinn",
            "Papers, Please", "This War of Mine", "Frostpunk", "Cities: Skylines", "SimCity",
            "The Sims 4", "Planet Zoo", "Planet Coaster", "Kerbal Space Program", "Space Engineers",
            "No Man's Sky", "Elite Dangerous", "Star Citizen", "EVE Online", "World of Warcraft",
            "Final Fantasy XIV", "Guild Wars 2", "The Elder Scrolls Online", "Black Desert Online",
            "Destiny 2", "Warframe", "Path of Exile", "Diablo III", "Diablo IV", "Grim Dawn"
    };

    private static final String[] DEVELOPERS = {
            "CD Projekt RED", "Rockstar Games", "Naughty Dog", "Santa Monica Studio",
            "Guerrilla Games", "Ubisoft", "Activision", "EA Sports", "Mojang Studios",
            "Epic Games", "InnerSloth", "Riot Games", "Valve", "Respawn Entertainment",
            "Blizzard Entertainment", "FromSoftware", "Sucker Punch Productions",
            "Insomniac Games", "Kojima Productions", "Square Enix", "Atlus", "PlatinumGames",
            "Supergiant Games", "Team Cherry", "Moon Studios", "Studio MDHR", "ConcernedApe",
            "Re-Logic", "Wube Software", "Ludeon Studios", "Paradox Interactive",
            "Firaxis Games", "Creative Assembly", "Larian Studios", "Obsidian Entertainment",
            "BioWare", "Bethesda Game Studios", "id Software", "MachineGames", "Capcom",
            "Konami", "Electronic Arts", "Valve Corporation", "Annapurna Interactive",
            "Telltale Games", "Quantic Dream", "Supermassive Games", "Tarsier Studios",
            "Playdead", "Number None", "Thekla, Inc.", "Lucas Pope", "11 bit studios",
            "Colossal Order", "Maxis", "Squad", "Keen Software House", "Hello Games",
            "Frontier Developments", "Cloud Imperium Games", "CCP Games", "Blizzard Entertainment",
            "Square Enix", "ArenaNet", "Zenimax Online Studios", "Pearl Abyss", "Bungie",
            "Digital Extremes", "Grinding Gear Games", "Blizzard Entertainment", "Crate Entertainment"
    };

    private static final String[] PUBLISHERS = {
            "CD Projekt", "Rockstar Games", "Sony Interactive Entertainment", "Ubisoft",
            "Activision", "Electronic Arts", "Microsoft", "Epic Games", "Riot Games",
            "Valve", "Blizzard Entertainment", "FromSoftware", "Square Enix", "Capcom",
            "Konami", "Bethesda Softworks", "Paradox Interactive", "2K Games", "Warner Bros. Games",
            "Sega", "Bandai Namco Entertainment", "Focus Home Interactive", "Devolver Digital",
            "Annapurna Interactive", "505 Games", "Team17", "Curve Digital", "Raw Fury"
    };

    private static final String[] GENRES = {
            "RPG", "Action", "Adventure", "Shooter", "Sports", "Simulation", "Strategy",
            "Puzzle", "Horror", "Platformer", "Fighting", "Racing", "MMO", "MOBA",
            "Battle Royale", "Survival", "Indie", "Open World", "Stealth", "Roguelike"
    };

    @Override
    public void run(String... args) {
        log.info("GameDataLoader - Iniciando carregamento de dados iniciais...");

        long count = gameRepository.findAll().size();
        if (count > 0) {
            log.info("GameDataLoader - Banco de dados já possui {} games. Pulando carregamento inicial.", count);
            return;
        }

        List<Game> games = generateGames(100);

        log.info("GameDataLoader - Salvando {} games no banco de dados...", games.size());
        int saved = 0;
        for (Game game : games) {
            try {
                gameRepository.save(game);
                saved++;
                if (saved % 10 == 0) {
                    log.debug("GameDataLoader - Salvos {} games...", saved);
                }
            } catch (Exception e) {
                log.error("GameDataLoader - Erro ao salvar game: {}", game.getTitle(), e);
            }
        }

        log.info("GameDataLoader - Carregamento concluído! {} games salvos com sucesso.", saved);
    }

    private List<Game> generateGames(int quantity) {
        List<Game> games = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            String baseTitle = TITLES[random.nextInt(TITLES.length)];
            String developer = DEVELOPERS[random.nextInt(DEVELOPERS.length)];
            String publisher = PUBLISHERS[random.nextInt(PUBLISHERS.length)];
            String genre = GENRES[random.nextInt(GENRES.length)];
            final int index = i;
            final String base = baseTitle;
            String title = games.stream().anyMatch(g -> g.getTitle().equals(base))
                    ? base + " " + (index + 1)
                    : base;

            String description = String.format(
                    "Um jogo incrível de %s desenvolvido por %s. Uma experiência única e envolvente.",
                    genre, developer);

            int releaseYear = 2010 + random.nextInt(15);
            double price = 29.90 + (random.nextDouble() * 170.10);

            Game game = Game.create(
                    title,
                    description,
                    developer,
                    publisher,
                    genre,
                    releaseYear,
                    Math.round(price * 100.0) / 100.0);

            games.add(game);
        }

        return games;
    }
}
