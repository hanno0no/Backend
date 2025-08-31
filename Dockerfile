# 1. 베이스 이미지 선택 (Java 17 사용)
FROM openjdk:21-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 필요한 파일들만 먼저 복사하여 빌드 캐시 활용
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x ./gradlew
RUN ./gradlew dependencies

# 4. 소스 코드 전체 복사
COPY src ./src

# 5. 애플리케이션 빌드 (테스트 제외)
RUN ./gradlew build -x test --no-daemon

# 6. [디버깅] 빌드 후 libs 폴더 내용 확인하기
RUN echo "--- Contents of build/libs ---" && ls -l build/libs

# 7. 애플리케이션 실행 (어떤 이름의 JAR 파일이든 실행)
CMD java -jar build/libs/*.jar