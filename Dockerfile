FROM sbtscala/scala-sbt:eclipse-temurin-jammy-17.0.10_7_1.10.0_2.13.14

WORKDIR /app

COPY . .

EXPOSE 8000

CMD ["sbt", "run"]