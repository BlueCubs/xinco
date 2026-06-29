FROM eclipse-temurin:25-jre-noble

WORKDIR /app

# Requires the project to be built first: mvn -B install -DskipTests
COPY xinco-ui/target/xinco-ui-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
