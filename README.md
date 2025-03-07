# Procyon

Procyon is a lightweight Java library designed to streamline backend development by providing utilities for logging, instance management, HTTP requests, MySQL, Redis connections, etc.

## Features
- **Logging**: SLF4J logging.
- **Instance Management**: Simple instance registry for easy dependency retrieval.
- **HTTP Requests**: Lightweight HTTP client abstraction using Apache.
- **Database Connectivity**: MySQL connection handling using HikariCP.
- **Redis Integration**: Redis connection handling with Lettuce.
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
```java
SQLConnection sqlConnection = SQLConnection.createConnection(
    "localhost", 3306, "database", "user", "password", false
);

procyonConnection.getConnections().add(sqlConnection);
```

### Redis Connection
```java
ProcyonRedisConnection redisConnection = ProcyonRedisConnection.createConnection("localhost", 6379);

procyonConnection.getConnections().add(redisConnection);
```

## Contributing
Contributions are welcome! Feel free to fork the repository and submit a pull request.

## License
This project is licensed under the MIT License.

For detailed documentation, check out the [Wiki](https://github.com/Zloits/Procyon/wiki).

