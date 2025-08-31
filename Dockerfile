# 1. 베이스 이미지 선택 (Java 17 사용)
FROM openjdk:21-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle Wrapper 파일들 복사
COPY gradlew .
COPY gradle gradle

# 4. build.gradle 과 settings.gradle 파일 복사
COPY build.gradle settings.gradle ./

# 5. gradlew 파일에 실행 권한 부여
RUN chmod +x ./gradlew

# 6. 의존성 다운로드 (소스코드 변경 없이 의존성만 같으면 이 단계는 캐시됨)
RUN ./gradlew dependencies

# 7. 소스 코드 전체 복사
COPY src ./src

# 8. 애플리케이션 빌드 (테스트는 제외!)
RUN ./gradlew build -x test --no-daemon

# 9. 실행할 JAR 파일의 경로를 환경 변수로 지정
ENV JAR_FILE_PATH=build/libs/hnn-0.0.1-SNAPSHOT.jar

# 10. 애플리케이션 실행
CMD ["java", "-jar", "${JAR_FILE_PATH}"]