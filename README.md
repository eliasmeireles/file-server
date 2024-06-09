# File Server

## Requirements

> - GraalVM 21.1.0

- Upload and download files from a server.

## Deploying with native image

- Deploying with `bootBuildImage` task.

````shell
    make boot-build-image
````

- Deploying with `native-compile` task.

````shell
    make boot-build-image
````

## Check if running

```shell
curl --location 'http://localhost:8080/api/file-server/v1/authorization' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "test@email.com",
    "password": "123456"
}'
```


