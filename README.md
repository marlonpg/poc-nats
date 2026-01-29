# NATS - High Performance Messaging System

## What is NATS?

NATS is a simple, secure, and high-performance messaging system for cloud native applications, IoT messaging, and microservices architectures.

### History
- **Created**: 2010 by Derek Collison
- **Original Purpose**: Built for Cloud Foundry as a lightweight messaging system
- **Open Source**: Yes, Apache 2.0 License since 2012
- **Current Maintainer**: Synadia Communications (founded by Derek Collison)
- **Language**: Written in Go

## Why NATS?

### Key Features
- **Lightweight**: ~15MB memory footprint, single binary deployment
- **High Performance**: 11M+ messages/sec throughput
- **Always On**: Built-in clustering, auto-healing, zero-downtime upgrades
- **Secure**: TLS encryption, NKEY authentication, multi-tenancy
- **Simple**: Fire-and-forget, request-reply, queue groups
- **Streaming**: Persistent messaging with NATS JetStream
- **Multi-Protocol**: Core NATS, JetStream, WebSocket, MQTT support

## NATS architecture

### Best Use Cases
1. **Microservices Communication**: Service-to-service messaging
2. **IoT Data Ingestion**: High-volume sensor data collection
3. **Real-time Applications**: Chat, gaming, live updates
4. **Event-Driven Architecture**: Event sourcing and CQRS patterns
5. **Edge Computing**: Lightweight messaging for resource-constrained environments

### Production Users
- **Ericsson**: Telecom infrastructure
- **Siemens**: Industrial IoT platforms
- **MasterCard**: Payment processing systems
- **Apcera**: Container orchestration (acquired by Ericsson)
- **Baidu**: Search and AI services
- **Capital One**: Financial services

## Trade-offs vs Competitors

| Feature | NATS | Apache Kafka | RabbitMQ | Redis Pub/Sub |
|---------|------|--------------|----------|---------------|
| **Throughput** | 11M+ msg/sec | 1M+ msg/sec | 50K msg/sec | 1M+ msg/sec |
| **Latency** | <1ms | 5-10ms | 1-5ms | <1ms |
| **Memory Usage** | 15MB | 1GB+ | 100MB+ | 50MB+ |
| **Persistence** | JetStream | Yes | Yes | Optional |
| **Clustering** | Built-in | Complex | Built-in | Sentinel/Cluster |
| **Learning Curve** | Low | High | Medium | Low |
| **Operational Complexity** | Low | High | Medium | Low |

### Performance Benchmarks
- **NATS Core**: 11.5M messages/sec, 8GB RAM, 4 CPU cores¹
- **Kafka**: 1.1M messages/sec, 32GB RAM, 8 CPU cores²
- **RabbitMQ**: 50K messages/sec, 4GB RAM, 4 CPU cores³

## Code Examples

### Setup NATS Server

```bash
# Download and run NATS server
curl -L https://github.com/nats-io/nats-server/releases/download/v2.10.7/nats-server-v2.10.7-windows-amd64.zip -o nats-server.zip
unzip nats-server.zip
./nats-server
```

### Java Integration

Add dependency to `pom.xml`:
```xml
<dependency>
    <groupId>io.nats</groupId>
    <artifactId>jnats</artifactId>
    <version>2.17.2</version>
</dependency>
```

### Basic Publisher
```java
import io.nats.client.*;

public class Publisher {
    public static void main(String[] args) throws Exception {
        Connection nc = Nats.connect("nats://localhost:4222");
        
        // Simple publish
        nc.publish("updates", "Hello NATS!".getBytes());
        
        // Publish with reply subject
        nc.publish("requests", "reply.inbox", "Request data".getBytes());
        
        nc.close();
    }
}
```

### Basic Subscriber
```java
import io.nats.client.*;

public class Subscriber {
    public static void main(String[] args) throws Exception {
        Connection nc = Nats.connect("nats://localhost:4222");
        
        // Simple subscription
        Subscription sub = nc.subscribe("updates");
        Message msg = sub.nextMessage(Duration.ofSeconds(1));
        System.out.println("Received: " + new String(msg.getData()));
        
        nc.close();
    }
}
```

### Request-Reply Pattern
```java
// Responder
Connection nc = Nats.connect();
Subscription sub = nc.subscribe("help");
Message msg = sub.nextMessage(Duration.ofSeconds(1));
nc.publish(msg.getReplyTo(), "I can help!".getBytes());

// Requester  
Message reply = nc.request("help", "need help".getBytes(), Duration.ofSeconds(1));
System.out.println("Reply: " + new String(reply.getData()));
```

### JetStream (Persistent Messaging)
```java
import io.nats.client.api.*;

Connection nc = Nats.connect();
JetStreamManagement jsm = nc.jetStreamManagement();

// Create stream
StreamConfiguration streamConfig = StreamConfiguration.builder()
    .name("ORDERS")
    .subjects("orders.*")
    .build();
jsm.addStream(streamConfig);

// Publish to stream
JetStream js = nc.jetStream();
js.publish("orders.new", "Order #1234".getBytes());

// Subscribe to stream
JetStreamSubscription jsSub = js.subscribe("orders.*");
Message msg = jsSub.nextMessage(Duration.ofSeconds(1));
msg.ack(); // Acknowledge message
```

## References

1. [NATS Performance Benchmarks](https://nats.io/blog/nats-server-210-performance/)
2. [Kafka Performance Testing](https://engineering.linkedin.com/kafka/benchmarking-apache-kafka-2-million-writes-second-three-cheap-machines)
3. [RabbitMQ Performance](https://www.rabbitmq.com/blog/2012/04/25/rabbitmq-performance-measurements-part-2/)
4. [NATS Official Documentation](https://docs.nats.io/)
5. [NATS vs Kafka Comparison](https://nats.io/blog/nats-vs-apache-kafka/)
6. [Cloud Native Computing Foundation Landscape](https://landscape.cncf.io/)

---
*For more examples and advanced usage, visit the [NATS Documentation](https://docs.nats.io/)*