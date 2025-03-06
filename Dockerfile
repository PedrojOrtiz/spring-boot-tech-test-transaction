# Usar una imagen base de OpenJDK
FROM openjdk:21-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el JAR del proyecto al contenedor
COPY target/spring-boot-tech-test-transaction-1.0.jar /app/spring-boot-tech-test-transaction-1.0.jar

# Exponer el puerto en el que corre el servicio
EXPOSE 8081

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "spring-boot-tech-test-transaction-1.0.jar"]
