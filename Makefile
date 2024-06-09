ARCH := $(shell uname -m)
COMPILED_DIR := ./compiled/$(ARCH)
APP_BINARY := ./app/build/native/nativeCompile/app

rebuild:
	./gradlew clean build

native-compile:
	./gradlew clean app:nativeCompile

jar-content:
	jar tf app/build/libs/app-1.0.0.jar

run:
	./gradlew clean bootRun

test:
	./gradlew clean test

clean:
	./gradlew clean

boot-build-image:
	./gradlew bootBuildImage
	DOCKERFILE=DockerfileBootBuildImage docker-compose build file-server
	make deploy

local-builder:
	make native-compile
	DOCKERFILE=Dockerfile docker-compose build file-server
	make deploy

deploy:
	docker-compose up -d --no-deps file-server
	docker-compose logs -f file-server

