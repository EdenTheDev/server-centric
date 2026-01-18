# Use Tomcat 9 with JDK 11
FROM tomcat:9.0.65-jdk11-openjdk-slim

# Copy the war file and give it the specific project name
COPY target/CycleNestOrchestrator.war /usr/local/tomcat/webapps/CycleNestOrchestrator.war

EXPOSE 8080
CMD ["catalina.sh", "run"]