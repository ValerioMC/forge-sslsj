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