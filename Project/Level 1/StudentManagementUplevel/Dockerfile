# ---- Stage 1: build .war bang Maven ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# copy rieng pom.xml truoc de tan dung cache layer cua Docker:
# neu chi sua code (khong sua pom.xml) thi lan build sau khong phai tai lai dependency
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- Stage 2: chay tren Tomcat 10.1 (Jakarta EE 10, khop pom.xml) ----
FROM tomcat:10.1-jdk17
# xoa app mac dinh cua Tomcat, deploy thang app cua minh vao context root "/"
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
