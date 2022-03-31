### jenkins
```shell
    gradlew clean api-server-module:bootBuildImage
    gradlew clean batch-server-module:bootBuildImage
    gradlew clean config-server-module:bootBuildImage
    gradlew clean gateway-server-module:bootBuildImage
    gradlew clean service-registry-server-module:bootBuildImage
```
```shell
    mkdir docker/docker_data
    docker-compose --env-file {env file} up
```