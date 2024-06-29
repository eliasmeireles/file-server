FROM ubuntu:24.04

LABEL mainers=eliasmflilico@gmail.com

COPY ./app/build/native/nativeCompile/app /usr/bin/app

ENV TZ=America/Sao_Paulo

CMD /usr/bin/app
