# Procyon

Procyon is a lightweight Java library designed to streamline backend development by providing utilities for logging, instance management, HTTP requests, MySQL, Redis connections, etc.

## Features
- **Logging**: SLF4J logging.
- **Instance Management**: Simple instance registry for easy dependency retrieval.
- **HTTP Requests**: Lightweight HTTP client abstraction using Apache.
- **Database Connectivity**: MySQL connection handling using HikariCP.
- **Redis Integration**: Redis connection handling with Lettuce.
- **Event Management**: Event.
- **More**

## Installation
```xml
<dependency>
  <groupId>me.zloits.procyon</groupId>
  <artifactId>procyon-lib</artifactId>
  <version>{VERSION}</version>
</dependency>
```

## Basic Usage

### Logging (SLF4J)
```java
Logger logger = new ProcyonLogger<>(YourClass.class).getLogger();
logger.info("Procyon is working!");
```

### Instance Management
Instance Management were used for store instance class to **InstanceRegistry** for then to retrieve again. This is use when the Instance class does not provide **getInstance()** method.
```java
// It could be any instance that don't have static getter to obtain again later.
ExampleInstance instance = new ExampleInstance();

InstanceGetter.add(instance);

ExampleInstance retrieved = InstanceGetter.get(ExampleInstance.class);
```

### HTTP Requests
```java
ProcyonHttpAPI httpAPI = new ProcyonHttpAPI(HttpClients.createDefault());
httpAPI.setBASE_URL("https://example.com");

httpAPI.POST("/endpoint", "data");
```

### MySQL Connection
Initialize your connection.
```java
SQLConnection sqlConnection = SQLConnection.createConnection(
    "localhost", 3306, "database", "user", "password", false
);

procyonConnection.getConnections().add(sqlConnection);
```

Execute to database
```java
new QueryExecutor(sqlConnection, "insert into procyon (id) values (?)", () -> {
    System.out.println("Test E: WORKS");
}, List.of(uuid.toString())).start();
```

Retrieve data from database.
```java
QueryGetter<UUID> queryGetter = new QueryGetter<>(sqlConnection, "select id from procyon where id = ?", resultSet -> {
    while (resultSet.next()) {
        System.out.println("Test F: WORKS (1)");
    }

    return uuid;
}, List.of(uuid.toString())).start();

queryGetter().start();

UUID uuid = queryGetter.get();
```

### Redis Connection
Initialize your connection
```java
ProcyonRedisConnection redisConnection = ProcyonRedisConnection.createConnection("localhost", 6379);

procyonConnection.getConnections().add(redisConnection);
```

Example Redis Packet
```java
@AllArgsConstructer
@Getter
public class ExamplePacket extends RedisPacket {

    private final String name;

    @Override
    public @NonNull String getChannel() {
        return "Example";
    }
}
```

Publish packet and handle packet response.
```java
ExamplePacket examplePacket = new ExamplePacket("Izhar");

redisConnection.publish(examplePacket, json -> {
    ExamplePacket examplePacket = GsonUtil.fromJson(json, ExamplePacket.class);

    // Handle response...

    System.out.println("Response successfully handled.");
}, timeout)
```

## Contributing
Contributions are welcome! Feel free to fork the repository and submit a pull request.

## License
This project is licensed under the MIT License.

For detailed documentation, check out the [Wiki](https://github.com/Zloits/Procyon/wiki).

