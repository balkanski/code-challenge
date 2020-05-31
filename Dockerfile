
FROM openjdk:11-slim

ADD build/libs/*code-challenge*.jar /code/
WORKDIR /code
EXPOSE 8080

ARG CACHEBUSTER
RUN echo $profile
CMD ["sh", "-c", "java -Xmx300m -Xms300m -jar  *code-challenge*.jar"]
