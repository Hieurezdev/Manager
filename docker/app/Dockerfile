FROM maven:3.9-eclipse-temurin-17

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN apt-get update && apt-get install -y --no-install-recommends \
    curl libxext6 libxrender1 libxtst6 libxi6 libfreetype6 fontconfig libgtk-3-0 && \
    rm -rf /var/lib/apt/lists/*

RUN mvn clean compile -DskipTests

ENV DB_HOST=mysql
ENV DB_PORT=3306
ENV DB_NAME=manager_db
ENV DB_USER=root
ENV DB_PASSWORD=Hch301105#
ENV DISPLAY=${DISPLAY}

CMD ["mvn", "exec:java", "-Dexec.mainClass=com.example.manager.view.DangNhapFrm"]