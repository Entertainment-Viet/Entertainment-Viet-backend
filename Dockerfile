FROM azul/zulu-openjdk-alpine:17-jre as builder
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM azul/zulu-openjdk-alpine:17-jre
COPY server.crt.pem $JAVA_HOME/jre/lib/security
RUN cd $JAVA_HOME/jre/lib/security && keytool -keystore cacerts -storepass changeit -noprompt -trustcacerts -importcert -alias keycloakcert -file server.crt.pem
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]