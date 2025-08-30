# -------- build stage --------
FROM openjdk:17-jdk-slim AS build

# Instalar utilidades para mvnw
RUN apt-get update && apt-get install -y --no-install-recommends unzip curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiar Maven Wrapper y pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Descargar dependencias en cache
RUN ./mvnw -q -DskipTests dependency:go-offline

# Copiar el código fuente y compilar
COPY src ./src
RUN ./mvnw -q -DskipTests package

# -------- runtime stage --------
FROM openjdk:17-jdk-slim
WORKDIR /app
ENV TZ=America/Santiago

COPY --from=build /app/target/*SNAPSHOT*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]