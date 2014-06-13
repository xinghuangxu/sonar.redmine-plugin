@ECHO OFF
call mvn clean install -DskipTests

call COPY C:\Users\leonx\Desktop\sonar\sonar-bcref-master\target\sonar-redmine-plugin-0.2-SNAPSHOT.jar C:\Users\leonx\Desktop\sonar\sonarqube-4.2\sonarqube\sonar-application\target\sonarqube-4.3.2-SNAPSHOT\extensions\plugins