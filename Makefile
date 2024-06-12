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
	./docker-deployment DockerfileBootBuildImage

local-builder:
	#make native-compile
	./docker-deployment Dockerfile


