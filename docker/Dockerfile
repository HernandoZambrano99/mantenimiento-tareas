# syntax=docker/dockerfile:1.7
###############################################################################
# Mantenimiento — Backend (Spring Boot 3.5 / Java 21)
# Multi-stage, layered jar, non-root, hardened.
#
# Build from project root (NOT from docker/):
#   docker build -f docker/Dockerfile -t mantenimiento:0.0.1 .
###############################################################################

FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build

COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B -q dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -q clean package -DskipTests

RUN cp target/*.jar app.jar && \
    java -Djarmode=tools -jar app.jar extract --layers --launcher --destination extracted


FROM eclipse-temurin:21-jre-noble AS runtime

ARG APP_VERSION=0.0.1-SNAPSHOT
ARG VCS_REF=unknown
ARG BUILD_DATE=unknown
LABEL org.opencontainers.image.title="mantenimiento" \
      org.opencontainers.image.description="API backend de mantenimiento con arquitectura hexagonal" \
      org.opencontainers.image.version="${APP_VERSION}" \
      org.opencontainers.image.revision="${VCS_REF}" \
      org.opencontainers.image.created="${BUILD_DATE}" \
      org.opencontainers.image.base.name="docker.io/library/eclipse-temurin:21-jre-noble"

RUN groupadd --system --gid 10001 spring && \
    useradd  --system --uid 10001 --gid spring --no-create-home --shell /usr/sbin/nologin spring && \
    apt-get update && \
    apt-get install -y --no-install-recommends curl ca-certificates && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

WORKDIR /application

COPY --from=builder --chown=spring:spring /build/extracted/dependencies/         ./
COPY --from=builder --chown=spring:spring /build/extracted/spring-boot-loader/   ./
COPY --from=builder --chown=spring:spring /build/extracted/snapshot-dependencies/ ./
COPY --from=builder --chown=spring:spring /build/extracted/application/          ./

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 \
-XX:+ExitOnOutOfMemoryError \
-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp \
-Djava.security.egd=file:/dev/./urandom \
-Dfile.encoding=UTF-8"

ENV SPRING_PROFILES_ACTIVE=docker

# Default local container port. On Render, PORT is injected at runtime and the
# app honors it via server.port=${PORT:8081}. EXPOSE is documentation
# only — Render does not use it.
EXPOSE 8081

USER spring:spring

# Shell form: ${PORT:-8081} is expanded by /bin/sh, so the same image
# works locally (PORT unset) and on Render (PORT injected).
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD curl --fail --silent http://localhost:${PORT:-8081}/actuator/health/readiness || exit 1

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
