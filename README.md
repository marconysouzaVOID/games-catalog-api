# Games Catalog API

API REST para gerenciamento de catálogo de games desenvolvida como teste técnico para Projuris.

## Descrição

Sistema CRUD completo para gerenciamento de um catálogo de games, implementado seguindo os princípios da Arquitetura Hexagonal (Ports and Adapters) e utilizando Programação Orientada a Eventos (POE) com Spring Events por um pedido do Davy (Tech Lead) durante a entrevista técnica realizada.

## Arquitetura

### Arquitetura Hexagonal

Esta arquitetura separa a lógica de negócio (core) das preocupações técnicas (infraestrutura), garantindo:

- **Independência**: O domínio não depende de frameworks ou tecnologias específicas
- **Testabilidade**: Facilita a criação de testes unitários e de integração
- **Manutenibilidade**: Mudanças na infraestrutura não afetam o domínio
- **Flexibilidade**: Permite trocar implementações (ex: banco de dados) sem alterar o core

#### Estrutura de Camadas

```
src/main/java/com/projuris/gamescatalog/
├── domain/                          # Camada de Domínio (Core)
│   ├── model/                       # Entidades de domínio
│   │   └── Game.java                # Entidade principal com lógica de negócio
│   ├── events/                      # Eventos de domínio
│   │   ├── GameCreatedEvent.java
│   │   ├── GameUpdatedEvent.java
│   │   └── GameDeletedEvent.java
│   └── repository/                  # Ports (interfaces)
│       └── GameRepository.java      # Contrato do repositório
│
├── application/                     # Camada de Aplicação
│   ├── dto/                         # Data Transfer Objects
│   │   ├── GameRequestDTO.java
│   │   ├── GameResponseDTO.java
│   │   └── PageResponseDTO.java
│   ├── usecase/                     # Use Cases (casos de uso)
│   │   ├── CreateGameUseCase.java
│   │   ├── GetGameUseCase.java
│   │   ├── ListGamesUseCase.java
│   │   ├── UpdateGameUseCase.java
│   │   ├── DeleteGameUseCase.java
│   │   └── ListGamesByGenreUseCase.java
│   └── mapper/                      # Mappers (MapStruct)
│       └── GameMapper.java
│
└── infrastructure/                  # Camada de Infraestrutura (Adapters)
    ├── persistence/                 # Adaptadores de persistência
    │   ├── entity/                  # Entidades JPA
    │   │   └── GameEntity.java
    │   ├── repository/              # Repositórios JPA
    │   │   └── JpaGameRepository.java
    │   ├── adapter/                 # Adaptadores
    │   │   └── GameRepositoryAdapter.java
    │   ├── mapper/                  # Mappers JPA
    │   │   └── GameEntityMapper.java
    │   └── loader/                  # Data Loader
    │       └── GameDataLoader.java
    ├── web/                         # Adaptadores HTTP
    │   ├── controller/
    │   │   ├── GameController.java
    │   ├── exception/
    │   │   └── GlobalExceptionHandler.java
    │   └── filter/
    │       └── TraceIdFilter.java
    ├── events/                      # Sistema de eventos
    │   ├── DomainEventPublisher.java
    │   └── handler/                 # Event Handlers
    │       ├── GameCreatedEventHandler.java
    │       ├── GameUpdatedEventHandler.java
    │       └── GameDeletedEventHandler.java
    └── config/                      # Configurações
        ├── ApplicationProperties.java
        ├── ActuatorConfig.java
        ├── AsyncConfig.java
        ├── CacheConfig.java
        └── OpenApiConfig.java
```

### Fluxo de Dados

1. **Controller (Adapter HTTP)** recebe requisição REST
2. **Use Case (Application)** orquestra a lógica de negócio
3. **Domain Model** contém a regra de negócio e gera eventos
4. **Repository Adapter (Infrastructure)** persiste os dados
5. **Event Publisher** publica eventos de domínio
6. **Event Handlers** processam eventos assincronamente

## Programação Orientada a Eventos (POE)

A aplicação implementa POE utilizando Spring Events para desacoplar ações e permitir processamento assíncrono. Quando um game é criado, atualizado ou deletado, eventos são disparados e processados por handlers específicos.

### Eventos Implementados

- **GameCreatedEvent**: Disparado quando um novo game é criado
- **GameUpdatedEvent**: Disparado quando um game é atualizado
- **GameDeletedEvent**: Disparado quando um game é deletado

### Benefícios da POE

- **Desacoplamento**: Handlers não conhecem quem disparou o evento
- **Extensibilidade**: Fácil adicionar novos handlers sem modificar código existente
- **Processamento Assíncrono**: Eventos são processados em threads separadas
- **Auditoria**: Facilita registro de ações para auditoria

## Tecnologias Utilizadas

### Core

- **Java 17**: Linguagem de programação
- **Spring Boot 3.2.0**: Framework principal

### Persistência

- **Spring Data JPA**: Abstração para acesso a dados
- **H2 Database**: Banco de dados em memória para desenvolvimento.

### Cache

- **Spring Cache**: Framework de cache
- **Caffeine**: Implementação de cache em memória

### Documentação

- **SpringDoc OpenAPI 2.2.0**: Documentação interativa da API com Swagger UI

### Validação

- **Bean Validation (Jakarta)**: Validação de dados de entrada

### Mapeamento

- **MapStruct 1.5.5**: Geração automática de código para mapeamento DTO ↔ Domain

### Monitoramento e Observabilidade

- **Spring Boot Actuator**: Endpoints de monitoramento e métricas
- **Micrometer Prometheus**: Exportação de métricas no formato Prometheus

### Utilitários

- **Lombok**: Redução de boilerplate (getters, setters, construtores)

### Testes

- **JUnit 5**: Framework de testes
- **Mockito**: Mocking para testes unitários
- **Spring Boot Test**: Testes de integração
- **MockMvc**: Testes de controllers

### Justificativas das Bibliotecas

#### MapStruct

- **Por quê**: Geração de código em tempo de compilação, melhor performance que reflection
- **Benefício**: Type-safe, sem overhead de runtime, fácil manutenção

#### Lombok

- **Por quê**: Reduz código boilerplate significativamente
- **Benefício**: Código mais limpo e legível, menos erros manuais

#### H2 Database

- **Por quê**: Banco em memória, ideal para desenvolvimento e testes
- **Benefício**: Setup rápido, sem necessidade de instalação externa e com alta compatibilidade com postgres caso seja necessário a mudança.

#### Caffeine Cache

- **Por quê**: Cache em memória de alta performance, ideal para consultas frequentes
- **Benefício**: Reduz carga no banco de dados, melhora tempo de resposta

#### SpringDoc OpenAPI

- **Por quê**: Geração automática de documentação interativa da API
- **Benefício**: Facilita testes e integração, documentação sempre atualizada

#### Spring Boot Actuator

- **Por quê**: Fornece endpoints prontos para monitoramento e observabilidade
- **Benefício**: Health checks, métricas do sistema, integração com Prometheus, facilita troubleshooting

#### Micrometer Prometheus

- **Por quê**: Expõe métricas da aplicação no formato Prometheus para integração com sistemas de monitoramento
- **Benefício**: Permite coleta de métricas por ferramentas como Prometheus, Grafana e outros sistemas de observabilidade, facilitando monitoramento em produção

## Como Executar

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+

### Executando a Aplicação

```bash
# Compilar o projeto
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

### Acessando o H2 Console

Durante a execução, o console H2 está disponível em:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:gamesdb`
- Username: `sa`
- Password: (vazio)

## Data Loader

A aplicação possui um componente `GameDataLoader` que popula automaticamente o banco de dados com 100 games na inicialização. Este componente foi criado especificamente para facilitar a demonstração e testes durante o desenvolvimento deste teste técnico.

**Importante**: O DataLoader verifica se já existem games no banco antes de popular. Se houver dados existentes, o carregamento é pulado. Este componente não é necessário para o funcionamento da aplicação e pode ser desabilitado removendo a anotação `@Component` da classe `GameDataLoader`.

## Documentação da API (Swagger)

A aplicação possui documentação interativa da API utilizando Swagger/OpenAPI. Após iniciar a aplicação, acesse:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

A documentação Swagger permite:

- Visualizar todos os endpoints disponíveis
- Testar os endpoints diretamente pela interface
- Ver exemplos de requisições e respostas
- Entender os modelos de dados utilizados

## Observabilidade e Monitoramento

### Spring Boot Actuator

A aplicação utiliza Spring Boot Actuator para fornecer endpoints de monitoramento e métricas. Os seguintes endpoints estão disponíveis:

- **Health Check**: `http://localhost:8080/actuator/health`

  - Status geral da aplicação
  - Detalhes de componentes (banco de dados, etc.)

- **Métricas**: `http://localhost:8080/actuator/metrics`

  - Lista todas as métricas disponíveis
  - Métricas específicas: `http://localhost:8080/actuator/metrics/{metricName}`

- **Info**: `http://localhost:8080/actuator/info`

  - Informações sobre a aplicação (nome, versão, descrição)

- **Prometheus**: `http://localhost:8080/actuator/prometheus`
  - Métricas no formato Prometheus para integração com sistemas de monitoramento
  - Requer a dependência `micrometer-registry-prometheus` no projeto
  - Retorna métricas em formato texto plano compatível com Prometheus

### Métricas Disponíveis

A aplicação expõe métricas básicas do sistema:

- **CPU**: Uso de processador
- **Memória**: Uso de memória (heap, non-heap)
- **Threads**: Número de threads ativas
- **HTTP Requests**: Contadores e tempos de resposta
- **JVM**: Métricas da JVM (garbage collection, classes carregadas, etc.)

### TraceId nos Logs

Cada requisição HTTP recebe um `traceId` único que é:

- Gerado automaticamente se não fornecido no header `X-Trace-Id`
- Adicionado ao MDC (Mapped Diagnostic Context) para aparecer em todos os logs da requisição
- Incluído no header de resposta `X-Trace-Id` para rastreamento
- Formatado nos logs no padrão: `[traceId]`

Exemplo de log com traceId:

```
2024-01-15 10:30:00 [http-nio-8080-exec-1] [a1b2c3d4] INFO  GameController - POST /api/games - Criando novo game
```

Isso permite rastrear todas as operações relacionadas a uma requisição específica através do traceId.

## Cache

A aplicação utiliza Spring Cache com Caffeine para melhorar a performance de consultas frequentes. O cache é configurado com:

- **Máximo de entradas**: 1000 por cache
- **Expiração por escrita**: 10 minutos
- **Expiração por acesso**: 5 minutos
- **Estatísticas**: Habilitadas

Os seguintes caches são utilizados:

- `games`: Cache da lista completa de games
- `gameById`: Cache de games por ID
- `gamesByGenre`: Cache de games por gênero

O cache é automaticamente invalidado quando games são criados, atualizados ou deletados.

## Testes

### Executando Testes

```bash
# Executar todos os testes
mvn test
```

### Estrutura de Testes

- **Testes Unitários**: Testam use cases isoladamente com mocks

  - `CreateGameUseCaseTest`
  - `GetGameUseCaseTest`
  - `UpdateGameUseCaseTest`
  - `DeleteGameUseCaseTest`
  - `ListGamesUseCaseTest`
  - `ListGamesByGenreUseCaseTest`

- **Testes de Integração**: Testam o fluxo completo (Controller → Use Case → Repository)
  - `GameControllerIntegrationTest`

## Padrões de Design Aplicados

### 1. Repository Pattern

- **Onde**: `GameRepository` (port) e `GameRepositoryAdapter` (adapter)
- **Por quê**: Abstrai a lógica de acesso a dados, facilitando testes e troca de implementação

### 2. Use Case Pattern

- **Onde**: Camada `application/usecase`
- **Por quê**: Cada caso de uso é uma classe isolada, facilitando manutenção e testes

### 3. DTO Pattern

- **Onde**: `GameRequestDTO` e `GameResponseDTO`
- **Por quê**: Separa objetos de transferência de dados das entidades de domínio

### 4. Adapter Pattern

- **Onde**: `GameRepositoryAdapter`, `GameController`
- **Por quê**: Adapta interfaces externas (JPA, HTTP) para o domínio

### 5. Observer Pattern (via Spring Events)

- **Onde**: Event Handlers
- **Por quê**: Desacopla ações e permite processamento assíncrono

## Melhorias Futuras

- [ ] Adicionar filtros avançados (por desenvolvedora, ano, preço)
- [ ] Implementar autenticação e autorização
- [ ] Implementar persistência dos processos da API para auditoria
- [ ] Implementar busca full-text
- [ ] Implementar testes de carga
- [ ] Adicionar métricas e monitoramento mais aprofundado (Grafana e Splunk)
- [ ] Migrar para banco de dados PostgreSQL
- [ ] Implementar CI/CD
