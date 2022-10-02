FROM openjdk:17-alpine
COPY home/java-function-invoker-1.1.1.jar /home/java-function-invoker.jar
COPY home/wy-space-1.0-SNAPSHOT-runner.jar /wy-space-1.0-SNAPSHOT-runner.jar
ENV JAVA_OPTS=""
CMD java $JAVA_OPTS -jar /home/java-function-invoker.jar --classpath wy-space-1.0-SNAPSHOT-runner.jar --target io.quarkus.gcp.functions.QuarkusHttpFunction
