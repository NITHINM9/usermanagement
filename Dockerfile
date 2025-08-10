# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace


COPY pom.xml .
RUN mvn -B -DskipTests dependency:go-offline


COPY src ./src
RUN mvn -B -DskipTests package

# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app


COPY --from=build /workspace/target/usermanagement-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s CMD curl -f http://localhost:8080/actuator/health || exit 1

ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
