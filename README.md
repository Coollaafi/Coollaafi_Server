# 개인화된 AI를 통해, 날씨에 맞는 맞춤형 옷을 추천해주는 소셜 네트워크 서비스, WOT 👔
날씨가 생각보다 더워서, 혹은 추워서 입은 옷을 후회한 적이 있지 않나요?<br />
매일 OOTD를 기록하고 싶은데, SNS에 올리기엔 부담스럽지 않나요?<br />
<br />
이에, 저희는 개인화된 AI를 통해, 오늘의 날씨에 맞는 여러분의 옷을 추천하고,<br />
친한 친구 20명과 함께하는 SNS를 제공하는 서비스, <b>왓(wot)</b>을 기획하였습니다.<br />
###### 배포사이트: https://coollaafi-frontend.vercel.app/
###### 데모 영상: https://www.youtube.com/watch?v=RDpbWE1proI&t=5s
<hr/>
<div align="center"><img src="https://github.com/user-attachments/assets/a32cacb5-bb23-4893-bc99-3189098ba3b3" width="100%"></div>
<hr/>

### ⚙️기술 스택
<b>[Frontend]</b><br/>
<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"><img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=white"><img src="https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=TypeScript&logoColor=white"><img src="https://img.shields.io/badge/CSS-1572B6?style=for-the-badge&logo=CSS&logoColor=white">
<br/><br/><b>[Backend]</b><br>
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white"><img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white"><img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white">
<br><br><b>[AI]</b><br/>
<img src="https://img.shields.io/badge/TensorFlow-FF6F00?style=for-the-badge&logo=TensorFlow&logoColor=white"><img src="https://img.shields.io/badge/PyTorch-EE4C2C?style=for-the-badge&logo=PyTorch&logoColor=white"><img src="https://img.shields.io/badge/Flask-000000?style=for-the-badge&logo=Flask&logoColor=white">


---

### How to install
1. **Java Development Kit (JDK 21)** 설치
    - JDK 21 이상이 설치되어 있어야 합니다.

2. **Gradle** 설치

3. **MySQL 데이터베이스**
    - MySQL 8.0 이상 설치
    - 사용할 DB를 생성합니다.

4. **AWS S3 설정**
    - 이미지 저장을 위해 S3 버킷이 필요합니다.
    - AWS Access Key와 Secret Key를 `application.yml` 파일에 설정해야 합니다.
   
5. **OpenWeather key 발급**

6. **Kakao 로그인 구현을 위한 Kakao Developer 애플리케이션 생성**

<br>

### How to build
1. 저장소 클론
   ```bash
   git clone <repository-url>
   cd <project-directory>

2. 프로젝트 빌드
   ```bash
   ./gradlew build

<br>

### How to run
1. application-local.yml 파일 생성
   - 현재 application.yml 파일과 application-prod.yml 파일이 github에 올라가있다.
   - 로컬로 실행하기 위해서는 application-local.yml 파일을 생성하고 작성해야한다.
   - application-prod.yml 파일과 동일한 형식으로 작성하며, 각 환경변수에 맞는 값을 입력한다. 
   - 작성해야하는 환경변수는 DB 정보, 카카오 developer App 정보, OpenWeather key, Frontend URL 및 AI FestAPI URL 이다.

2. MySQL 데이터베이스 시작

3. 애플리케이션 실행
   ```bash
   ./gradlew bootRun
   
4. 애플리케이션 접속
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API 엔드포인트: http://localhost:8080

---

### SourceCode 설명
**[E-R 다이어그램]**
![WOT](https://github.com/user-attachments/assets/0581ead3-52a9-4af1-b64e-6ae7e6086b52)
<br><br>
**[프로젝트 흐름]**
  1. 클라이언트와의 통신 
     - 클라이언트는 REST API를 통해 필요한 요청을 전송
  2. AI FestAPI와의 통신
     - 클라이언트 요청 처리 과정에서 AI API 호출이 필요한 경우, 서버에서 AI의 FestAPI를 호출해 결과 반환
  3. 데이터 흐름
     - 클라이언트 요청 → Spring Boot Controller → 내부 비즈니스 로직 실행 → AI FestAPI 호출 (필요 시) → 결과 반환
<br><br>

**[계층별 역할]**
  1. **Controller Layer**
     - 클라이언트 요청(Request)을 수신하고, 응답(Response)을 반환하는 계층
     - Service Layer와 DTO를 통해 상호작용
  2. **Service Layer**
     - 비즈니스 로직을 처리하는 계층.
     - 컨트롤러에서 받은 데이터를 기반으로 필요한 작업을 수행하고 결과를 반환
  3. **Converter**
     - DTO와 엔티티(Entity) 간 변환을 담당하는 계층
     - DB 레이어와 비즈니스 로직 계층 간 데이터를 매핑
  4. **Repository Layer**
     - 데이터베이스와 상호작용하는 계층
     - JPA를 기반으로 CRUD 작업 수행
  5. **DTO (Data Transfer Object)**
     - 클라이언트와 서버 간의 데이터를 주고받는 객체
     - 비즈니스 로직에서 사용되는 엔티티와 분리하여 클라이언트와의 통신 포맷을 제공

---

### 사용한 주요 기술 스택
   - **Spring Boot Starter Web**: REST API 구성 
   - **Spring Data JPA**: 데이터베이스 접근 
   - **MySQL Connector**: MySQL 연결 
   - **Springdoc OpenAPI**: Swagger UI 및 API 문서화 
   - **AWS SDK**: AWS S3 연동 
   - **JSON Web Tokens (JWT)**: 인증 처리 
   - **Spring Security**: 엔드포인트 보안 
   - **Jackson**: JSON 처리


### 사용한 오픈 소스 라이브러리
   - **Spring Framework**: https://spring.io/projects/spring-framework
   - **AWS SDK for Java**: https://aws.amazon.com/sdk-for-java/
   - **Lombok**: https://projectlombok.org/
   - **JSON Library**: https://github.com/stleary/JSON-java

---

### 👩‍💻팀원 소개
  <table >
    <tr>
      <td align="center"><b>Frontend</b></td>
      <td align="center"><b>Backend</b></td>
      <td align="center"><b>AI</b></td>
    </tr>
    <tr>
      <td align="center"><img src="https://avatars.githubusercontent.com/u/88073842?s=400&u=bc39f4c6820808f5c034dc5e210f7ea279bff43c&v=4" width="130"></td>
      <td align="center"><img src="https://avatars.githubusercontent.com/u/52813483?v=4" width="130"></td>
      <td align="center" ><img src="https://avatars.githubusercontent.com/u/137473567?v=4" width="130" borderRadius="100%"></td>
    </tr>
    <tr>
      <td align="center"><a href="https://github.com/sujinRo" target="_blank" width="160">sujinRo</a></td>
      <td align="center"><a href="https://github.com/ujiiin" target="_blank">ujiiin</a></td>
      <td align="center"><a href="https://github.com/Choi-Hanui" target="_blank">Choi-Hanui</a></td>
    </tr>
  </table>