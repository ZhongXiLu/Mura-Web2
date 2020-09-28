FROM adoptopenjdk/openjdk11:jdk-11.0.8_10-slim

RUN apt update && apt install --no-install-recommends -y \
    git \
    postgresql \
    maven && \
    rm -rf /var/lib/apt/lists/*

USER postgres
RUN /etc/init.d/postgresql start && \
    psql --command "ALTER USER postgres WITH PASSWORD 'postgres';" && \
    createdb -O postgres mura-web && \
    echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/10/main/pg_hba.conf && \
    echo "listen_addresses='*'" >> /etc/postgresql/10/main/postgresql.conf

USER root
WORKDIR /usr/src/mura-web
COPY build/libs/*.jar entrypoint.sh /usr/src/mura-web/
COPY data /usr/src/mura-web/data/

RUN chmod +x /usr/src/mura-web/entrypoint.sh
CMD ["/usr/src/mura-web/entrypoint.sh"]
