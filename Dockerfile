FROM ubuntu:24.04

LABEL mainers=eliasmflilico@gmail.com

COPY ./app/build/native/nativeCompile/app /usr/bin/

# Required by container health check
RUN apt-get update \
    && apt-get install -y curl \
    && apt-get clean

ARG PORT
ARG JWT_PUBLIC_KEY
ARG JWT_PRIVATE_KEY
ARG PROFILE

ENV TZ America/Sao_Paulo

ENV SERVER_PORT $PORT

ENV SPRING_PROFILES_ACTIVE $PROFILE

ENV ENV_JWT_PUBLIC_KEY $JWT_PUBLIC_KEY
ENV ENV_JWT_PRIVATE_KEY $JWT_PRIVATE_KEY

EXPOSE $PORT

CMD /usr/bin/app
