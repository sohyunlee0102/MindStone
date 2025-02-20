FROM openjdk:17-jdk-slim

# JAR 파일 경로를 변수로 지정 (정확한 파일명 사용)
ARG JAR_FILE=build/libs/myapp.jar  

# JAR 파일을 컨테이너로 복사
COPY ${JAR_FILE} app.jar

# 컨테이너 실행 시 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
