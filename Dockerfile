# Dockerfile para aplicación Spring Boot con JWT y PostgreSQL
# Utilizamos imagen base de OpenJDK 21
FROM openjdk:21-jdk-slim

# Metadatos del contenedor
LABEL maintainer="Eduardo Rivera & Ronald Sanchez"
LABEL description="Sistema de Soporte Técnico con Spring Boot, JWT y PostgreSQL"

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Dar permisos de ejecución al wrapper de Maven
RUN chmod +x ./mvnw

# Descargar dependencias (optimización de capas Docker)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación
RUN ./mvnw clean package -DskipTests

# Exponer puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/parcial-final-n-capas-0.0.1-SNAPSHOT.jar"] 