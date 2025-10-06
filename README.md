# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## link to my sequence diagram 
https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGcnQMqQI4FdIDsBjGAERAEMBzAJzIFtoAjAT2mMgA9hIr0m8ATbhTJkq-AFDjqAe2wAHaAGImkcOGkB3OFFFLqZJuLIFg0ngFUk3cZe7QAtAD5tIfMABc0KllxJg0MmgCHRpCSBsrKgAee3tYcFc8DwBBJABrVAAzMyDpPEyQKloyUDzxBm8yNK9IACtIE1RgyFEI7id4xI8AJTqG4CaQsjDxTrdYx1sqd16kOTz+VA0weBr6kxA86FpkJEpw-AkpjoS3dwI8gqKm6VpigVHTpIdnDCoAN253KwFBlp4LndhhI3p8qE50Nx3iAiN9DqhAs1dID7iCoTDIBDiCUyAwyFYAgQiCgvj9FkEQrkgQ9+Di8QTacBcfjwtimfSYMZiZEIYzmVZvkw-JBiqACGQ1CxBFAuNBIGCmMB4CA8BRxHyOby6SzPAREAQMtBTNArDAQJk5QqlSqKNANPjCcBsBKpapIFwJBUWtVuFQckqSkbENANSy7Q68NJ-EjvBJ1dqrE42fzOUTdl8lTBQwT7ahI-5jE6JeAWDHIPw5VQ-VRJKCMTF7MmOYTuV9vE6qHhUL6zOJQdwG3XYe3sJ3u1Xe2MkoOoW33aOu5Xq21onEnh4yQijdIgzBsJEg4HM4xpKIKzHhkRoJkyCAoBJDpIm2GuenV9nIO4ACrBj-hv6iOWJrYGmKCZNgkp9uiRANs+BKviS0wjmOwGgUgUEfAOsRDp+yGLkgIHco8XQzphSHzihBFoSuDZTh4-AgHM4AGKgxrHvudgBv4x4MKeYj-hS-xAVR3LgSW4iPpIMjyEoFSaHg0C9BQjFcDw0B6DQhgSQ8Ul+jJij6CwAAy0jKQpigMMxBqSJJUh6QoiixtAJkULI-gGZpNk6XZsgOXIo5yFAzkqdAADidDIBpBheXGPn6dQkD4HAlSyuFOxRVptnSQ5GjKrKtTSCq0DCOlHnRdp-BAA
## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
