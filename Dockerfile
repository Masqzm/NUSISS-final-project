# Stage 1 - Build Angular
#=========================
FROM node:23 AS ng-build

WORKDIR /src

RUN npm i -g @angular/cli

#COPY client/public public
COPY client/src src
COPY client/*.json .

RUN npm ci && ng build


# Stage 2 - Build SpringBoot
#============================
FROM openjdk:23-jdk AS j-build

WORKDIR /src

COPY server/.mvn .mvn
COPY server/src src
COPY server/mvnw .
COPY server/pom.xml .

# Copy all angular app files over 
COPY --from=ng-build /src/dist/client/browser/* src/main/resources/static

RUN chmod a+x mvnw && ./mvnw package -Dmaven.test.skip=true


# Stage 3 - Build Full App
#==========================
FROM openjdk:23-jdk

WORKDIR /app

COPY --from=j-build /src/target/finalproject-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080

# DBs
ENV SPRING_DATASOURCE_URL=
ENV SPRING_DATASOURCE_USERNAME=
ENV SPRING_DATASOURCE_PASSWORD=
ENV SPRING_DATA_MONGODB_URI=

ENV GOOGLE_API_KEY=

ENV SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID=
ENV SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET=

ENV STRIPE_SECRET_KEY=

ENV CLIENT_BASE_URL=

EXPOSE ${PORT}

SHELL ["/bin/sh", "-c"]
ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar