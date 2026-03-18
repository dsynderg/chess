# BYU CS 240 Chess

A Java client/server chess application built for BYU CS 240. It demonstrates HTTP APIs, WebSocket gameplay, persistence, and shared game logic across modules.

## Overview
- Command line client to register/login, list/create/join games, and play moves
- Javalin-based server that exposes REST endpoints and a WebSocket for live play
- Shared module contains chess rules, game state, and WebSocket message/command models
- SQL-backed data access with in-memory implementations for testing

## Repo Structure
- `client/` CLI app and client-side services
- `server/` Javalin server, services, and data access layers
- `shared/` chess engine and shared DTOs/messages
- `starter-code/` course-provided phase scaffolding
- `server/src/main/resources/web/` minimal static web assets

## Build and Run (Maven)
From the repo root:

```sh
mvn compile
mvn test
```

Run the apps:

```sh
mvn -pl server exec:java
mvn -pl client exec:java
```

Package uber jars:

```sh
mvn package
# or
mvn package -DskipTests
```

Run a packaged client jar:

```sh
java -jar client/target/client-jar-with-dependencies.jar
```

## Key Concepts
- **HTTP endpoints** for user and game management
- **WebSocket** channel for live game actions and notifications
- **Persistence** via SQL data access classes with a memory fallback
- **Shared chess logic** with tests under `shared/src/test/java/`

## API Summary
Base paths are configured in `server/src/main/java/server/Server.java`.

HTTP endpoints:
- `POST /user` register a new user
- `POST /session` login
- `DELETE /session` logout
- `GET /game` list games (requires `authorization` header)
- `POST /game` create a game (requires `authorization` header)
- `PUT /game` join a game as white/black (requires `authorization` header)
- `DELETE /db` clear all server data (test/reset helper)

WebSocket:
- `WS /ws` live game channel (connect, load game, make move, resign, leave)

## Tests
- Shared chess rules and move validation tests live under `shared/src/test/java/`
- Server and client tests live under `server/src/test/java/` and `client/src/test/java/`

## Notes
- The architecture diagram is stored in `10k-architecture.png`.
- Course scaffolding lives in `starter-code/` and is meant to be copied in by phase.
