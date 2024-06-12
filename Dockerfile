FROM ubuntu:24.04

LABEL mainers=eliasmflilico@gmail.com

# Required by container health check
RUN apt-get update \
    && apt-get install -y curl \
    && apt-get clean

ARG PORT
ARG JWT_PUBLIC_KEY
ARG JWT_PRIVATE_KEY
ARG PROFILE
ARG ENV_AUTHORIZATION_PATH
ARG ENV_STORAGE_PATH

RUN mkdir -p $ENV_STORAGE_PATH/config/

COPY ./app/build/native/nativeCompile/app /usr/bin/app
COPY ./app/build/native/nativeCompile/app $ENV_STORAGE_PATH/app
COPY ./test/runner.json $ENV_STORAGE_PATH/config/runner.json

COPY $JWT_PUBLIC_KEY /etc/security/file-server/sec/public.key
COPY $JWT_PRIVATE_KEY /etc/security/file-server/sec/private.key
COPY $ENV_AUTHORIZATION_PATH /etc/security/file-server/authorization/users.json

ENV PORT $PORT
ENV ENV_STORAGE_PATH $ENV_STORAGE_PATH
ENV ENV_JWT_PUBLIC_KEY=/etc/security/file-server/sec/public.key
ENV ENV_JWT_PRIVATE_KEY=/etc/security/file-server/sec/private.key
ENV ENV_AUTHORIZATION_PATH=/etc/security/file-server/authorization/users.json

ENV TZ America/Sao_Paulo

ENV SPRING_PROFILES_ACTIVE $PROFILE

EXPOSE $PORT

CMD /usr/bin/app

#CMD tail -f /dev/null
