FROM java:8

WORKDIR /

ADD target/googleplaycrawler-0.0.1-SNAPSHOT.jar googleplaycrawler-0.0.1-SNAPSHOT.jar

EXPOSE 9000

CMD java - jar googleplaycrawler-0.0.1-SNAPSHOT.jar