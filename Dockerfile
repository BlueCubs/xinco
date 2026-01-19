FROM tomcat:9.0-jdk17

# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR file to the webapps directory
COPY Xinco/target/Xinco-*.war /usr/local/tomcat/webapps/xinco.war

# Expose the default Tomcat port
EXPOSE 8080

# Set headless mode for AWT
ENV CATALINA_OPTS="-Djava.awt.headless=true"

# Start Tomcat
CMD ["catalina.sh", "run"]
