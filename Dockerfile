# Use a full JDK image for building
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy all source code and Makefile
COPY . .

# Build using make
RUN make backend

# === Runtime stage ===
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built artifact from the builder stage
# Adjust path below if make outputs elsewhere
COPY --from=builder /app/target/backend.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
