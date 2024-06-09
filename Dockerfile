FROM docker.io/library/app:1.0.0 AS build

FROM ubuntu:24.04

LABEL mainers=eliasmflilico@gmail.com

COPY --from=build /workspace/com.softwareplace.fileserver.FileServerApplicationKt /usr/bin/app

# Required by container health check
RUN apt-get update \
    && apt-get install -y curl \
    && apt-get clean

ARG PORT
ARG JWT_PUBLIC_KEY
ARG JWT_PRIVATE_KEY
ARG PROFILE

ENV TZ America/Sao_Paulo

ENV SPRING_PROFILES_ACTIVE $PROFILE

EXPOSE $PORT

CMD /usr/bin/app -DSPRING_PROFILES_ACTIVE="${PROFILE}" -DSERVER_PORT="${PORT}" -DENV_JWT_PUBLIC_KEY="${JWT_PUBLIC_KEY}" -DENV_JWT_PRIVATE_KEY="${JWT_PRIVATE_KEY}" -DENV_STORAGE_PATH="${ENV_STORAGE_PATH}"

#CMD tail -f /dev/null
