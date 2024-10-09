# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Phase 2 Sequence Diagram
![phase2_sequenceDiagram](https://github.com/user-attachments/assets/3e538112-5216-42b8-88e6-db3848c4e577) ([https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3VgdgAhmGYEAYYEgAGYwABCwFtkLRGMdoiThXzlz93mSQoPqPRsQAolBvFBMJeoAVi2V+qN1MA5LjFML5vnuB7psesR9AcP4FNkPYVAALAOjRDn0-6qIB9wga+0DlPutoPhiMFrhu24wH6cgwNgCDAJYHYxDAy66hwYTyHOp6JgSYZumSFIGrS5ajiaRLhhaHLcryBqCsKMCiuKxjnq6LKlJCYA+Lc8n2tooZiW654JigpSRBpWkybpmAwEUaooOen7lPUqQ3sxCCsZY9TAHOMCgdAVlnEClCGcUJY9FMmHYcBfS+WmLFemxlFec2MAHNZcHwd2GDlChADMQ7hSykVVjF5RxQe7FJfOKVkYYFE1H6HYxZoOjKbxqkwJQYEReSelMgZ+RGY5zkQLePhoCgAAe2AoOAKC2k1aVynZBQOTATkuWNk3TbN814e+1mfsF1CXGFGGFUBxV7eUsIcGoOrEAQiQwBAO6ddAmKTKlNklOeCFZQArAO+VnQBF0wN00VXTAN13VAD0JEkL0dXtH3VegHBmJwpheL4-gBNA7DkjAAAyEDREkARpBkWSZcwsrHWm1R1E0rQGOoCNDgpKCzC8bwcLBIVfvkq2nVzuxfLzSzi9Vn5soNMDklSNIItCyIoFBWJcYYLptaSCsCTSXOiX10qRpJPK2hZ8hCiKwnisuzAAJI7jcWjW1uvhY9ZSn5CpeumZpSRW8A-l+6oR1OvK6ZmUHOnyP5Rkarr7qet6QYBkGIbWWHElWjaMCxsGumLbZnYOdOs7oNmub+bL57faFAx22OE5fBXjbJdZ46tqRP6dn9YB9kDvRNyOowLpOfTt3OE+kSg661TuBp8sEQbQEgABec3Na1+ntYqyqh8nEfgmtw2javfrr1vHDLwt31nit5zAkNG2X6k19zXfe218-QXfoLK4BVQY4Uhm+coY1pyf2WNVEuP0Mo5EHjAFCABGYG3VQElXku-aBn0aqbh3PVVIjUf5KQKGHcob1YAYN6macOADbKvxGtgraM1Mi7TfAnJa9k-5pnWswzaU02FzR8j-A6vCI4nWAVhMGYD8LQ1uqoe62BHqI1eijPBcD-75AHqUQGaDejSKKrhcBCjYbwyekjKhqMDjo0xhjTw3g-CBC8CgdAJMya+GYJTdImRMADzloAyo0gnzEyfPUJ8zQWhs1UBzd+lc0DtiFqtae6APwSIGktcot1uCZEEqktA2IjLkOTtk+eFJ9Q0gKcbOhZtyjSHKZkZimddIOwIc0usHckiTWhKyHk7JaHhhPlHAOWkD4qkTvXVa4zf6BRBPTEoVwBYM37rTIeg5ei2IXu05elIcFwxvjvEpe89YzOzsfTJjCz5vygQcr+HRRGcLgY-YWvCmEX1uZve5JCnkBVTJItMp0MFRSwZAtedyYFfXSjotZyCnAGKBedTBUMwVXwhXg+e5FCENVgE1MhvtSnIy6kiwZ-V5b8Ivqwnajy-LPJUDwuZ7yWFCOpffQ6DDFmIpASCqGMMlFwxUQjZ66i3w2K4QzX6az9HoKRcBCGWC+XKNUcKol70MVYzsY43GgRIS2mJtCGAABxUcrIfHU38bTQJDNygVENeEqJ9hRzdAKUkp+jKegFNbgCDJ8tFbGv-BnTpc4incIJSc90+swD+rUIGmcXSalDMKJaEo24wiF0gJxNpFEClPgmr0zcSRjBHLDSbf2FJA4KxNfq2IpKWTDOMqMpIjr-zVuYLCOAiR5A8lZM2lAAB9R2tpFTDFmN0HQEA-TaTHmoWY5xKLEMraMAAcqOFwWsy5vOjhW3tqhW3pLmZ2H8QC+g7uAhUfovbHbSGAig3sOUkLTG6FMKmeTm7iyeDoOiIAGwGgrJ8R9SQT2jhXaMf4MBGjLOOPXXRMB+wbNOqem1F7RxXpvXeh9T6+gvsqdO99UxP2gB-W+-9mHe0geMoucDWysV2g7Hsz5hzvYtWOaWiNZyZDHw5afCl2CGPfNxWIh+9K3WpiZaij+ELv6-PZZcxZpYjGyNBfsr5kLxXwJhYg7K8KZXcsuqY8TuC0bbLqjiml758UUNVdQklYd61ifGiy9hZm1MvNWjxwR20nNsokRyqRIMZHItMYqgVyqrEaNgQ3L8CCezSsMf54x8reWKKVUKsLor1UY01TjZxARsCaVooYOAuomnRpSL4mmiCrVyaZg0B1Tr4ldKHGR0ckGgqvMZQx+IiRygAB4Cn5HQs18eXr93-Nk+CTYTZo2xoSbMIbKAQ22RY3Qj0xDv2FwSTN+Ntb1B1JTVuNNQYM1pKzTuHNebYh9MLSYWzXH5SNsolAdEWs7vGRgKnTI035uLcjnZEtK2YCxGgCgL7wGRI7fDvkS05BICQnnYYaNxbLPqW3Sh6QEO7MPcvejyZyY3lY7R6Ngs9cj0IbR2h+91U+7QbWXB4G2OKdISMzRwiMAjyPiwMUvHjLrzML0BwYiz4f6HVkydeLim9rgSIurDnvdBbRayshVC6FgV6fkazwXYA57GaXg8+j4KVP+Qs4S9jt3xtR3c8pm+UnaVCeWu10T1yBFW-4858RcyAXHtVyY+RBn0URehTB1BOmAs8v0y71TmLF4wCIT8vyxvw2UMlzQs35Lz7Ms8yI++XOHcvyd5SxzWfBMydJwpwL8jgsWLUVZsVWiosaZi9puL3vEtBeSyF1LIq1Vow1VjLVOWGLKmYQAKQgP00rAQCPfotZVhZJZqiUhZi0XtzqgwJKHLReilAO0IGgHNtHrXklvM66o3r-Xgab4YlAHf0AVgAHUWCOwiS0XcxMFBwAANJfAZ+UW9lPvUe7m7GSTYg6jhbZzj77Lrg7FL-bhirYzQNgbZdLgHoAJr9TsilD7aHZ+jHaJKnYdJxpzi5r5r9JFqp5ZIPYWDPYwGWZ+pgHzbPSwDY5oF1pQ4ciRqLpiAJ6sZqTlpaS9oY6va8ExycEvZAGraEwoBVB0RX5IFzgoFoA8wyHb4QC75QCQEoBXo-ZniwF8SA6w6gGjCwjfYY5sHlBkAGHw5GqjhI6Eoo78GE7kFXIPYzK44XibrsZ1xz6AqH6rKaawbDw9DUbR6s7s4ng55-Ivy863j86a5NQi6k7i7l6wAa4y4kSH4K6DxK5OBoQjze5yJpipEaza40ZUTMCX6MSshlQJQcRpJOG-YmR8FJBUFiA0GEp0FGEMHQCcFaGmHQ6KyiHcErb2FNo2H1HggjGiEwGDQgCpCSHSFb5QByHoAKFKGLE37qE9HSDYh0r25ubp7VEeSVRu6RHaJHryZJFh7yKHGJQzwB7y6ZFaZ5TN6ypq6xRuTxQVR3HBE7J64R5G7Ma6H7xKgqjnLhpCH568YG7W4PLZ7cIiZ56W58a3ywnF4+ai6Apl5XFph+4qaaKRaSr+HB4vG6Y+44kR4Yo64x6mZ4qAmWZUIwAp4XJp5vxUpeaCY577GsmF4cK24l6AJcqh5vHXTt5V4qrWL4mB5SqBFYnlCt4V6imCqWJd5QBip2JZZOJ4xeD0RdheiwDADYC0SEBdYUzYYz7mBVbz4hJhIRJRJFrU7uGMqlgAGphVanwgDcB4CMh6AGCwjaH0pAl6wel6nen6AoB+l9HsHJDECGCMj7gGCCHiFbpjIgnrpTIeGpnu6ukk5BJBFJJ+E9gBHwZLgrjtKx4CacJDFwFWaMk2bMlZKQkebCK8n7R24MqO6W5slF7Sboml6XHClmL8pilpbd5QoPEN4Awyn9ngzzgKqKmhYqlqm94YxAA )


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
