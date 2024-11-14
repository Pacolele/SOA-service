FROM maven:eclipse-temurin

WORKDIR /build

# Copy poms
COPY ./pom.xml .
COPY ./account-rest/pom.xml ./account-rest/pom.xml
COPY ./account-datalayer/pom.xml ./account-datalayer/pom.xml
COPY ./account-logic/pom.xml ./account-logic/pom.xml
COPY ./account-utils/pom.xml ./account-utils/pom.xml
COPY ./account-xfinal-test/pom.xml ./account-xfinal-test/pom.xml

# Copy src folders
COPY ./account-rest/src/ ./account-rest/src/
COPY ./account-datalayer/src/ ./account-datalayer/src/
COPY ./account-logic/src/ ./account-logic/src/
COPY ./account-utils/src/ ./account-utils/src/
COPY ./account-xfinal-test/src/ ./account-xfinal-test/src/

RUN mvn clean install -DskipTests
CMD ["mvn", "-f", "./account-rest/pom.xml", "spring-boot:run"]
