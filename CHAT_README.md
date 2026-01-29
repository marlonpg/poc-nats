# NATS Chat Application

Simple chat application using NATS messaging and Java 25.

## Setup

1. **Start NATS Server**:
```bash
docker-compose up -d
```

2. **Compile the application**:
```bash
mvn compile
```

3. **Run multiple chat clients**:
```bash
# Terminal 1
mvn exec:java -Dexec.mainClass="Chat" -Dexec.args="Alice"

# Terminal 2  
mvn exec:java -Dexec.mainClass="Chat" -Dexec.args="Bob"

# Terminal 3
mvn exec:java -Dexec.mainClass="Chat" -Dexec.args="Charlie"
```

## Usage

- Type messages and press Enter to send
- Messages appear in all connected clients
- Type `quit` to exit
- NATS monitoring available at: http://localhost:8222

## Architecture

- **Subject**: `chat.messages` - all chat messages
- **Pattern**: Publish-Subscribe (fan-out to all clients)
- **Protocol**: NATS Core (fire-and-forget for real-time chat)

## Stop

```bash
docker-compose down
```