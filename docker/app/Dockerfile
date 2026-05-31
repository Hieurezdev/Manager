FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . /app

RUN apt-get update && apt-get install -y --no-install-recommends curl libxext6 libxrender1 libxtst6 libxi6 libfreetype6 fontconfig libgtk-3-0 && \
    rm -rf /var/lib/apt/lists/* && \
    if [ ! -f mysql-connector-j-8.0.33.jar ]; then \
      curl -L -o mysql-connector-j-8.0.33.jar https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar; \
    fi && \
    find src/com/example/manager -name "*.java" ! -name "Test*.java" -print0 | xargs -0 javac -encoding UTF-8 -d out

ENV DB_HOST=mysql
ENV DB_PORT=3306
ENV DB_NAME=manager_db
ENV DB_USER=root
ENV DB_PASSWORD=Hch301105#
ENV DISPLAY=${DISPLAY}

CMD ["java", "-cp", "out:mysql-connector-j-8.0.33.jar", "com.example.manager.view.DangNhapFrm"]