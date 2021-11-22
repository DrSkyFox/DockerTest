FROM maven:3.8.3-openjdk-17 as DEPS
COPY test-dock/pom.xml test-dock/pom.xml
COPY test-dock-db/pom.xml test-dock-db/pom.xml

COPY pom.xml
RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.2.0:go-offline

FROM maven:3.8.3-openjdk-17 as BUILD \
WORKDIR /opt/app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /opt/app /opt/app
COPY test-dock/src /opt/app/test-dock/src
COPY test-dock-db/src /opt/app/test-dock-db/src

RUN mvn -B -e -o clean install -DskipTests=true


FROM openjdk:17-ea-4-oraclelinux8
WORKDIR /opt/app
COPY --from=build /opt/app/<path-to-target>/my.jar
EXPOSE 8000
CMD ["java", "-jar", "/opt/app/my.jar"]
