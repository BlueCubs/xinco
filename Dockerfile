FROM eclipse-temurin:25-jre-noble

WORKDIR /app

# Requires production build first (JDK 25; do NOT use Homebrew Java 26):
#   export JAVA_HOME=/Users/javierortiz/Library/Java/JavaVirtualMachines/openjdk-25.0.2/Contents/Home
#   export PATH=$JAVA_HOME/bin:$PATH
#   mvn -B install -DskipTests -Dmaven.javadoc.skip=true -Pproduction -pl xinco-ui -am
COPY xinco-ui/target/xinco-ui-*.jar app.jar

# Extract Flyway migration SQL from nested xinco-core JAR so Flyway can use filesystem: location
# (classpath: scanning inside nested Spring Boot JARs is unreliable)
RUN apt-get update -qq && apt-get install -y --no-install-recommends unzip && rm -rf /var/lib/apt/lists/* \
 && unzip -j app.jar 'BOOT-INF/lib/xinco-core-*.jar' -d /tmp/extract \
 && mkdir -p /app/db/migration \
 && unzip -j /tmp/extract/xinco-core-*.jar 'db/migration/*.sql' -d /app/db/migration \
 && rm -rf /tmp/extract

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
