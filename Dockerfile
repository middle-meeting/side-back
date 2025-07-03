# 1단계: 빌드용 (멀티스테이지)
FROM eclipse-temurin:21-jdk AS builder

# 작업 디렉터리 설정
WORKDIR /app

# gradlew 및 설정 파일 복사 (wrapper 포함)
COPY gradlew .
COPY gradle ./gradle

# 소스 코드 복사
COPY src ./src
COPY build.gradle settings.gradle .

# 권한 부여 (gradlew 실행 권한)
RUN chmod +x gradlew

# 의존성 다운로드 및 빌드 (테스트 포함)
RUN ./gradlew clean bootJar -x test --no-daemon

# 2단계: 런타임용 경량 이미지
FROM eclipse-temurin:21-jre
WORKDIR /app

# 빌드 결과물만 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# Cloud Run 기본 포트 노출
EXPOSE 8080

# 실행 커맨드
ENTRYPOINT ["java", "-jar", "app.jar"]
