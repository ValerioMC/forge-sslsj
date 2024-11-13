# Docker Setup

To run this project locally, you will need to set up the following Docker images.

## Postgres Docker Image

You can use the official Postgres Docker image to set up a local PostgreSQL database. The following configuration
provides a basic setup with the database named `sample_project`, and with a user and password that you can specify.

### Docker Command

```sh
docker run --name sample_project_db -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=mypassword -e POSTGRES_DB=sample_project -p 5432:5432 -d postgres
```

Replace `myuser` and `mypassword` with your desired username and password for the Postgres database.

### Docker Compose

Alternatively, you can use Docker Compose for more complex setups. Here’s a `docker-compose.yml` file setup for the
Postgres image:

```yaml
version: '3.8'
services:
  db:
    image: postgres
    container_name: sample_project_db
    environment:
      POSTGRES_DB: sample_project
      POSTGRES_USER: myuser
      POSTGRES_PASSWORD: mypassword
    ports:
      - "5432:5432"
```

Replace `myuser` and `mypassword` with your desired username and password for the Postgres database.

### Tweaking Parameters

To optimize the setup based on your specific project requirements, you may adjust the following parameters:

- **Database Configuration**: Modify `POSTGRES_DB`, `POSTGRES_USER`, and `POSTGRES_PASSWORD` to fit your project’s
  needs.
- **Ports**: Ensure that the exposed port (`5432` in this example) does not conflict with other services on your host
  machine. You can change the host-side port by modifying the `-p` option or the `ports` section in
  `docker-compose.yml`.
- **Volumes**: Persist database data by attaching a volume. In `docker-compose.yml`, add a `volumes` section:
    ```yaml
    volumes:
      - db_data:/var/lib/postgresql/data
    ```
  This ensures that your data is not lost when the container restarts.

- **Advanced Configuration**: For advanced Postgres configurations (e.g., `max_connections`, `shared_buffers`), you
  might want to create a custom `postgresql.conf` file and mount it into the container:
    ```yaml
    services:
      db:
        image: postgres
        volumes:
          - ./custom_postgresql.conf:/etc/postgresql/postgresql.conf
        command: postgres -c 'config_file=/etc/postgresql/postgresql.conf'
    ```

# Docker Setup

To run this project locally, you will need to set up the following Docker images.

## SonarQube Docker Image

To configure a local SonarQube server for code quality and code coverage analysis, you can use the official SonarQube
Docker image. The following commands will set up SonarQube and a PostgreSQL database for it.

### Docker Commands

```sh
# Start PostgreSQL for SonarQube
docker run --name sonarqube_db -e POSTGRES_USER=sonar -e POSTGRES_PASSWORD=sonar -e POSTGRES_DB=sonarqube -p 5432:5432 -d postgres
# Start SonarQube
docker run --name sonarqube --link sonarqube_db:db -e SONARQUBE_JDBC_URL=jdbc:postgresql://db:5432/sonarqube -e SONARQUBE_JDBC_USERNAME=sonar -e SONARQUBE_JDBC_PASSWORD=sonar -p 9000:9000 -d sonarqube
```


```yaml
version: '3.8'
services:
  sonarqube:
    image: sonarqube
    container_name: sonarqube
    environment:
      SONARQUBE_JDBC_URL: jdbc:postgresql://db:5432/sonarqube
      SONARQUBE_JDBC_USERNAME: sonar
      SONARQUBE_JDBC_PASSWORD: sonar
    ports:
      - "9000:9000"
    depends_on:
      - db
  db:
    image: postgres
    container_name: sonarqube_db
    environment:
      POSTGRES_DB: sonarqube
      POSTGRES_USER: sonar
      POSTGRES_PASSWORD: sonar
    ports:
      - "5432:5432"
```

## Postgres Docker Image

You can use the official Postgres Docker image to set up a local PostgreSQL database. The following configuration
provides a basic setup with the database named `sample_project`, and with a user and password that you can specify.

### Docker Command

```sh
docker run --name sample_project_db -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=mypassword -e POSTGRES_DB=sample_project -p 5432:5432 -d postgres
```

Replace `myuser` and `mypassword` with your desired username and password for the Postgres database.

### Docker Compose

Alternatively, you can use Docker Compose for more complex setups. Here’s a `docker-compose.yml` file setup for the
Postgres image:

```yaml
version: '3.8'
services:
  db:
    image: postgres
    container_name: sample_project_db
    environment:
      POSTGRES_DB: sample_project
      POSTGRES_USER: myuser
      POSTGRES_PASSWORD: mypassword
    ports:
      - "5432:5432"
```

Replace `myuser` and `mypassword` with your desired username and password for the Postgres database.

### Tweaking Parameters

To optimize the setup based on your specific project requirements, you may adjust the following parameters:

- **Database Configuration**: Modify `POSTGRES_DB`, `POSTGRES_USER`, and `POSTGRES_PASSWORD` to fit your project’s
  needs.
- **Ports**: Ensure that the exposed port (`5432` in this example) does not conflict with other services on your host
  machine. You can change the host-side port by modifying the `-p` option or the `ports` section in
  `docker-compose.yml`.
- **Volumes**: Persist database data by attaching a volume. In `docker-compose.yml`, add a `volumes` section:
    ```yaml
    volumes:
      - db_data:/var/lib/postgresql/data
    ```
  This ensures that your data is not lost when the container restarts.
- **Advanced Configuration**: For advanced Postgres configurations (e.g., `max_connections`, `shared_buffers`), you
  might want to create a custom `postgresql.conf` file and mount it into the container:
    ```yaml
    services:
      db:
        image: postgres
        volumes:
          - ./custom_postgresql.conf:/etc/postgresql/postgresql.conf
        command: postgres -c 'config_file=/etc/postgresql/postgresql.conf'
    ```

# Running Tests with JaCoCo and SonarQube

To run tests with JaCoCo coverage and upload the results to SonarQube, follow these steps:

## Add JaCoCo Plugin

Add the JaCoCo plugin configuration to your `pom.xml` (for Maven):

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.8</version>
            <executions>
                <execution>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
                <execution>
                    <id>report</id>
                    <phase>verify</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## Add SonarQube Plugin

Add the SonarQube plugin configuration to your `pom.xml` (for Maven):

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.sonarsource.scanner.maven</groupId>
            <artifactId>sonar-maven-plugin</artifactId>
            <version>3.9.1.2184</version>
        </plugin>
    </plugins>
</build>
```

## Configure SonarQube Properties

Add a `sonar-project.properties` file to your project's root directory with the following content:

```properties
sonar.projectKey=sample_project
sonar.host.url=http://localhost:9000
sonar.login=<your_sonar_token>
sonar.sources=src/main/java
sonar.tests=src/test/java
sonar.java.binaries=target/classes
sonar.jacoco.reportPaths=target/jacoco.exec
```

Replace `<your_sonar_token>` with your actual SonarQube token.

## Run Tests and Upload Coverage

To run tests with coverage and upload the results to SonarQube, use the following Maven commands:

```sh
mvn clean verify
mvn sonar:sonar
```