# Берем официальный Tomcat 11
FROM tomcat:11.0-jdk17-temurin

# Копируем WAR-файл в папку webapps Tomcat
COPY target/Dispatcher_notification-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/Dispatcher_notification.war

# Открываем порт Tomcat
EXPOSE 8000

# Запуск Tomcat
CMD ["catalina.sh", "run"]