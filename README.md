<div align="center">
  <h1>
    ♻️ [2025 kakao X 9oorm 시즌톤] 75팀 분리특공대 ♻️
  </h1>
  <strong>IT 연합 동아리 구름톤 유니브</strong>
  <p>2025_SEASONTHON_TEAM_75_BE</p>

  <br/>

  <p>
    <strong>"이거 어떻게 버려야 하지?" 헷갈리는 분리배출, 이제 그만!</strong>
  </p>
  <p>
    분리특공대는 복잡한 쓰레기 배출 정보를 한눈에 알려주고, <br/>
    우리 동네 배출일에 맞춰 알림을 보내주는 똑똑한 분리배출 가이드 서비스입니다.
  </p>

  <br/>
</div>

![image](https://github.com/user-attachments/assets/1cca76f2-050f-4b4e-8779-a48f25b02718)
![KakaoTalk_20250917_224547832_01](https://github.com/user-attachments/assets/2d01502d-c0b6-4aef-a298-550e13c78eff)
![KakaoTalk_20250917_224547832_02](https://github.com/user-attachments/assets/a8ac9deb-aa84-4dc8-ba63-c85b9cfbe360)
![KakaoTalk_20250917_224547832](https://github.com/user-attachments/assets/cec9cbe2-beac-447f-b95b-e045be894c95)




## 📌 기본 설명

- **목표**
    - 사용자 위치(기본 자치구/동) 기반으로 품목별 올바른 분리배출 방법을 안내
    - 자주 검색되는 항목을 **3시간 단위 랭킹**으로 보여주어 트렌드를 제공
    - 잘못 버리기 쉬운 **주의 품목**을 별도 안내(예: 음식물 타입이지만 실제로는 일반쓰레기)


---

## ⚙️ Backend 기술 스택

- Gradle project
- Spring Boot 3.x
- Java 21
- MySQL 8.x
- Spring Data JPA
- Spring Security + OAuth2 Client
- jdbc template


---

## ☁️ 인프라 기술 스택

- **AWS EC2**  + **Docker**
- **AWS RDS MySQL**
- **AWS S3** 
- **Docker Hub**
- **GitHub Actions** (CI/CD)

---

## 🚀 주요 기능

- **인증/회원**
    - 카카오 OAuth2 로그인
    - JWT 발급/재발급, 로그아웃
- **지역 관리**
    - 시/도, 시/군/구, 읍/면/동 검색 및 기본 자치구 설정
- **분리배출 가이드**
    - 이미지 분석을 통해 쓰레기 분리 배출 방법 제공 
    - 품목 키워드 검색 → 지역별 배출 규칙 반환
    - 주의 품목(redirect 타입) 처리
- **랭킹/트렌드**
    - 검색 로그 집계 → 3시간 단위 랭킹 갱신
    - schedule
- **공지/이력**
    - `Revision`을 통한 제도 변경/공지 제공
- **뱃지**
  - 각 뱃지 정책을 통해 뱃지 획득 조건을 계산하여 부여
  - 이벤트 드리븐 패턴
---

## 🧱 패키지 구조도
```bash
feature/trash/                                  # 🗑️ 쓰레기 정보 관리 도메인
├── 📁 api/                                     # Presentation Layer (REST Controllers)
│   ├── 📁 admin/                               # 관리자 전용 API
│   │   ├── 📄 TrashTypeAdminController.java     # 쓰레기 타입 관리 API
│   │   └── 📄 TrashTypeAdminControllerApi.java  # Swagger 문서
│   ├── 📄 TrashController.java                 # 사용자용 쓰레기 API
│   └── 📄 TrashControllerApi.java              # Swagger 문서
├── 📁 application/                             # Application Layer (Use Cases & Services)
│   ├── 📁 admin/
│   │   └── 📄 TrashTypeAdminService.java        # 관리자 서비스
│   ├── 📄 TrashService.java                     # 쓰레기 조회/분석 서비스
│   └── 📄 TrashCreateUseCase.java              # 쓰레기 생성 유스케이스
├── 📁 domain/                                  # Domain Layer (핵심 비즈니스 로직)
│   ├── 📁 entity/
│   │   ├── 📄 Trash.java                        # 쓰레기 메인 엔티티
│   │   ├── 📄 TrashType.java                    # 쓰레기 분류 타입
│   │   ├── 📄 TrashItem.java                    # 개별 쓰레기 아이템
│   │   └── 📄 TrashDescription.java             # 쓰레기 설명 정보
│   ├── 📁 type/
│   │   ├── 📄 Type.java                         # 쓰레기 주요 분류 enum
│   │   └── 📄 ItemType.java                     # 아이템 세부 분류 enum
│   └── 📁 service/
│       ├── 📄 TrashTypeFinder.java              # 쓰레기 타입 조회 서비스
│       ├── 📄 TrashDescriptionFinder.java       # 설명 조회 서비스
│       └── 📄 TrashItemFinder.java              # 아이템 조회 서비스
├── 📁 infrastructure/                          # Infrastructure Layer (데이터 접근)
│   ├── 📄 TrashRepository.java                  # 쓰레기 메인 리포지토리
│   ├── 📄 TrashTypeRepository.java              # 타입 리포지토리
│   ├── 📄 TrashItemRepository.java              # 아이템 리포지토리
│   └── 📄 TrashDescriptionRepository.java       # 설명 리포지토리
└── 📁 dto/                                     # Data Transfer Objects
    ├── 📁 request/
    │   └── 📄 CreateTrashRequest.java           # 쓰레기 생성 요청
    └── 📁 response/
        ├── 📄 TrashResultResponse.java          # 쓰레기 분석 결과
        ├── 📄 TrashTypeResponse.java            # 타입 정보 응답
        ├── 📄 TrashItemResponse.java            # 아이템 정보 응답
        ├── 📄 TrashDescriptionResponse.java     # 설명 정보 응답
        ├── 📄 TrashAnalysisResponseDto.java     # AI 분석 결과
        ├── 📄 PartCardResponse.java             # 부분 카드 정보
        └── 📄 DistrictSummaryResponse.java      # 지역별 요약 정보

infrastructure/
├── 📁 adapter/                                  # 어댑터 구현체
│   ├── 📁 in/                                   # Inbound 어댑터 (현재 비어있음)
│   └── 📁 out/                                  # Outbound 어댑터
│       ├── 📁 s3/
│       │   └── 📄 S3FileStorageAdapter.java     # AWS S3 파일 저장소 어댑터
│       └── 📁 gpt/
│           ├── 📁 dto/
│           │   └── 📄 SimilarResult.java        # GPT 유사도 결과 DTO
│           └── 📄 OpenAIChatAdapter.java        # OpenAI GPT 채팅 어댑터
└── 📁 port/                                     # 포트 인터페이스
    ├── 📁 s3/
    │   └── 📄 FileStoragePort.java              # 파일 저장소 포트
    └── 📁 gpt/
        └── 📄 ChatAIClientPort.java             # AI 채팅 클라이언트 포트

global/
├── 📁 auth/                                     # 인증/인가 전역 설정
│   ├── 📁 jwt/                                  # JWT 토큰 관리
│   │   ├── 📁 entity/                           # JWT 토큰 엔티티, DTO, 타입
│   │   ├── 📁 filter/                           # JWT 인증 필터
│   │   └── 📁 service/                          # JWT 토큰 생성/검증, 쿠키 관리
│   ├── 📁 handler/                              # OAuth2 성공/실패 핸들러
│   ├── 📁 security/                             # 사용자 인증 정보 및 서비스
│   └── 📁 service/                              # OAuth2 사용자 서비스
├── 📁 config/                                   # 스프링 설정 클래스들
│   │                                           # (Security, Swagger, Web, JPA, AI, Scheduling 등)
├── 📁 entity/                                   # 공통 엔티티 (BaseTimeEntity)
├── 📁 exception/                                # 전역 예외 처리
├── 📁 response/                                 # 공통 응답 형식
│   └── 📁 type/                                 # 성공/에러 코드 enum
└── 📁 util/                                     # 유틸리티 클래스들
```
### 계층의 의존 관계 흐름
- **application** : {도메인}Controller, {도메인}Service
  - 독립적으로 실행 가능한 애플리케이션 모듈
  - 하위에서 설계한 domain, infrastructure 모듈들을 조립하여 실행
  - REST API 컨트롤러와 애플리케이션 서비스가 이 계층에 위치

- **domain** : Entity, {도메인}Finder, Type
  - 시스템의 핵심 비즈니스 로직을 담당하는 모듈
  - JPA Entity, Domain Service(Finder), Enum Type이 이 계층에 위치
  - 외부 의존성이 전혀 없는 순수한 비즈니스 로직만 포함
  - 데이터베이스나 외부 시스템에 대한 직접적인 의존성 제거

- **infrastructure** : {도메인}Repository, {외부호출}Adapter
  - 외부 시스템과의 연동을 담당하는 모듈 (DB, S3, OpenAI 등)
  - JPA Repository, AWS S3 어댑터, OpenAI 채팅 어댑터가 이 계층에 위치
  - 포트 & 어댑터 패턴을 통해 언제든 교체 가능하도록 설계

- **global** : 공통 설정 및 횡단 관심사
  - 전체 애플리케이션에서 공통으로 사용되는 모듈
  - 인증/보안, 예외 처리, 응답 형식, 설정 클래스들이 위치

##  ERD
![image](https://github.com/user-attachments/assets/71ed8eb7-b8f4-41c1-8573-4d7a3f1c0db6)

