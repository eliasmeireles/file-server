FROM alpine:3.20.1

LABEL mainers=eliasmflilico@gmail.com

# Install libc6-compat for glibc compatibility
RUN apk add --no-cache libc6-compat

COPY ./app/build/native/nativeCompile/app /usr/bin/app

ENV TZ=America/Sao_Paulo

CMD ["/usr/bin/app"]
