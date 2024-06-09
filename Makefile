ARCH := $(shell uname -m)
COMPILED_DIR := ./compiled/$(ARCH)
APP_BINARY := ./app/build/native/nativeCompile/app

rebuild:
	./gradlew clean build

store:
	mkdir -p $(COMPILED_DIR)
	cp $(APP_BINARY) $(COMPILED_DIR)/app

native-run:
	#$(APP_BINARY) -Dspring.main.allow-bean-definition-overriding=true -DENV_JWT_PUBLIC_KEY="/home/eliasmeireles/workspace/personal/backend/storage-api/app/src/main/resources/public.key" -DENV_JWT_PRIVATE_KEY="/home/eliasmeireles/workspace/personal/backend/storage-api/app/src/main/resources/private.key"
	$(APP_BINARY) -DENV_STORAGE_PATH=/home/eliasmeireles/Downloads  -DENV_JWT_PUBLIC_KEY="./app/src/main/resources/sec/public.key" -DENV_JWT_PRIVATE_KEY="./app/src/main/resources/sec/private.key"

native-compile:
	./gradlew clean app:nativeCompile
	make store

jar-content:
	jar tf app/build/libs/app-1.0.0.jar

native:
	native-image -classpath=com.softwareplace.fileserver.FileServerApplication --no-server -cp app/build/libs/app-1.0.0.jar

run:
	./gradlew clean bootRun

test:
	./gradlew clean test

clean:
	./gradlew clean

deploy:
	./gradlew bootBuildImage
	docker-compose build file-server
	docker-compose up -d --no-deps file-server
	docker-compose logs -f file-server

