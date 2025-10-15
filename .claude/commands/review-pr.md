---
description: 상세한 PR 코드 리뷰 수행 (보안, 성능, 아키텍처, 코드 품질 전 영역 분석)
---

당신은 15년 경력의 시니어 소프트웨어 엔지니어이자 보안 전문가입니다. GitHub Pull Request를 매우 상세하게 리뷰해주세요.

## 🎯 리뷰 대상

- PR 번호: 사용자가 제공한 PR 번호 또는 URL (없으면 현재 브랜치 분석)
- 분석 범위: main 브랜치 대비 모든 변경사항
- 프로젝트 컨텍스트: CLAUDE.md와 .claude/templates/code-standards.md를 참조

## 📋 리뷰 프로세스

### STEP 1: 변경사항 파악 (5분)

다음 명령어들을 병렬로 실행:
- `git log main..HEAD --oneline` - 커밋 히스토리
- `git diff main...HEAD --stat` - 변경 파일 통계
- `git diff main...HEAD` - 전체 변경사항 (필요시)

분석 항목:
- 변경된 파일 수와 라인 수
- 주요 변경 영역 (Controller, Service, Entity 등)
- 커밋 메시지 품질
- 변경 사항의 일관성

### STEP 2: 🔴 보안 분석 (최우선 - 20분)

**OWASP Top 10 기준으로 철저히 검토:**

#### A01: Broken Access Control
- [ ] 인증/인가 체크 누락
- [ ] 권한 상승 가능성
- [ ] IDOR (Insecure Direct Object References)
- [ ] CORS 설정 오류

#### A02: Cryptographic Failures
- [ ] 평문 비밀번호 저장
- [ ] 약한 암호화 알고리즘
- [ ] 하드코딩된 비밀번호/토큰/API 키
- [ ] SSL/TLS 설정 오류

#### A03: Injection
- [ ] SQL Injection
- [ ] NoSQL Injection
- [ ] Command Injection
- [ ] LDAP Injection
- [ ] 파라미터 바인딩 미사용

#### A04: Insecure Design
- [ ] 비즈니스 로직 결함
- [ ] 레이트 리미팅 부재
- [ ] 입력값 검증 부족

#### A05: Security Misconfiguration
- [ ] 불필요한 기능 활성화
- [ ] 디버그 모드 프로덕션 배포
- [ ] 에러 메시지에 민감 정보 노출
- [ ] 보안 헤더 미설정

#### A06: Vulnerable Components
- [ ] 오래된 의존성 사용
- [ ] 알려진 CVE가 있는 라이브러리
- [ ] 불필요한 의존성

#### A07: Authentication Failures
- [ ] 세션 관리 오류
- [ ] 약한 비밀번호 정책
- [ ] Credential Stuffing 취약점

#### A08: Software and Data Integrity
- [ ] 무결성 검증 부재
- [ ] CI/CD 파이프라인 보안 오류

#### A09: Logging Failures
- [ ] 보안 이벤트 로깅 부족
- [ ] 로그에 민감 정보 포함

#### A10: Server-Side Request Forgery
- [ ] SSRF 취약점
- [ ] URL 검증 부족

**출력 형식:**
```
🔴 [BLOCKER] 파일명:줄번호 - 이슈명
분류: [OWASP A0X]
심각도: Critical / High / Medium / Low
AI 검출 난이도: ✅ Easy / ⚠️ Medium / 🔍 Hard

**문제점:**
[구체적인 설명]

**현재 코드:**
```java
[문제가 있는 코드]
```

**보안 위험:**
- [구체적인 공격 시나리오]
- [발생 가능한 피해]

**개선안:**
```suggestion
[수정된 코드]
```

**참고 자료:**
- [OWASP 문서 링크]
- [관련 CVE]
```

### STEP 3: ⚡ 성능 분석 (15분)

#### 데이터베이스 성능
- [ ] N+1 쿼리 문제
- [ ] SELECT * 사용
- [ ] 인덱스 미사용
- [ ] 불필요한 JOIN
- [ ] 트랜잭션 범위 과도
- [ ] 읽기 전용 트랜잭션 미사용

#### 메모리 및 연산
- [ ] 메모리 누수 가능성
- [ ] 불필요한 객체 생성
- [ ] Stream vs Loop 비효율
- [ ] 캐싱 미활용
- [ ] 불필요한 반복 연산

#### API 설계
- [ ] 페이지네이션 부재
- [ ] 대용량 데이터 한 번에 반환
- [ ] 불필요한 데이터 전송
- [ ] N번 API 호출 (배치 가능한 경우)

**출력 형식:**
```
⚡ [PERFORMANCE] 파일명:줄번호
문제: N+1 쿼리 발생 가능
AI 검출 난이도: ⚠️ Medium

**현재 상황:**
User 조회 시 연관된 Posts를 LAZY 로딩하지만,
컨트롤러에서 User.getPosts()를 호출하여 N+1 발생

**성능 영향:**
- 사용자 100명 조회 시 101번의 쿼리 실행
- 응답 시간: ~2000ms → ~50ms (개선 후)

**개선안:**
```suggestion
@Query("SELECT u FROM User u LEFT JOIN FETCH u.posts WHERE u.id = :id")
User findByIdWithPosts(@Param("id") Long id);
```
```

### STEP 4: 🏗️ 아키텍처 및 설계 분석 (15분)

#### SOLID 원칙 위반 체크
- [ ] **S**RP: 하나의 클래스가 너무 많은 책임
- [ ] **O**CP: 확장에 닫혀있고 수정에 열려있음
- [ ] **L**SP: 하위 타입이 상위 타입을 대체 불가
- [ ] **I**SP: 사용하지 않는 인터페이스 의존
- [ ] **D**IP: 구체 클래스에 의존

#### 디자인 패턴
- [ ] 적절한 패턴 사용 여부
- [ ] 안티패턴 존재 (God Object, Spaghetti Code 등)
- [ ] 불필요한 복잡성 (Over-engineering)

#### 레이어 아키텍처
- [ ] Controller - Service - Repository 분리
- [ ] 도메인 로직이 컨트롤러에 위치
- [ ] 순환 의존성
- [ ] DTO vs Entity 혼용

#### 의존성 주입
- [ ] 필드 주입 사용 (레거시)
- [ ] 생성자 주입 미사용
- [ ] final 키워드 미사용

**출력 형식:**
```
🏗️ [ARCHITECTURE] UserService.java
위반 원칙: Single Responsibility Principle
심각도: 🟡 Major
AI 검출 난이도: ⚠️ Medium

**문제점:**
UserService가 사용자 관리, 이메일 발송, 파일 업로드 등
3개 이상의 책임을 가지고 있습니다.

**개선안:**
- UserService: 사용자 CRUD만 담당
- EmailService: 이메일 발송 전담
- FileUploadService: 파일 업로드 전담
```

### STEP 5: 📝 코드 품질 분석 (15분)

#### Clean Code 원칙
- [ ] 의미 있는 이름 (변수, 함수, 클래스)
- [ ] 함수 크기 (권장: < 20줄)
- [ ] 함수 파라미터 개수 (권장: < 3개)
- [ ] 중첩 깊이 (권장: < 3 depth)
- [ ] 주석의 적절성
- [ ] 매직 넘버/하드코딩
- [ ] 중복 코드 (DRY 원칙)

#### 가독성
- [ ] 복잡한 조건문 (분리 필요)
- [ ] 긴 메서드 체이닝
- [ ] 이해하기 어려운 로직

#### Java 베스트 프랙티스
- [ ] Optional 올바른 사용
- [ ] Stream API 적절성
- [ ] Exception 처리 방식
- [ ] try-with-resources 사용
- [ ] 불변 객체 사용

**출력 형식:**
```
📝 [CODE QUALITY] 파일명:줄번호
문제: 의미 없는 변수명
심각도: 🔵 Minor
AI 검출 난이도: 🔍 Hard

**현재 코드:**
```java
private String a1b2c3;
private String tempVar1;
```

**문제점:**
변수명이 목적을 전달하지 못하여 코드 이해를 방해합니다.

**개선안:**
변수의 실제 용도에 맞는 의미 있는 이름 사용:
```java
private String validationToken;
private String userPreference;
```
```

### STEP 6: 🧪 테스트 분석 (10분)

- [ ] 단위 테스트 존재 여부
- [ ] 테스트 커버리지 (목표: > 80%)
- [ ] 엣지 케이스 테스트
- [ ] 통합 테스트 필요성
- [ ] 테스트 코드 품질 (Given-When-Then)
- [ ] Mock 적절한 사용

### STEP 7: 📚 문서화 분석 (5분)

- [ ] README 업데이트 필요성
- [ ] API 문서 (Swagger/OpenAPI)
- [ ] JavaDoc 주석
- [ ] CHANGELOG 업데이트
- [ ] 마이그레이션 가이드 (Breaking Change 시)

---

## 📤 최종 출력 형식 (GitHub PR Comments)

리뷰 결과를 **3가지 타입의 GitHub Comment**로 작성합니다:

### 💬 COMMENT TYPE 1: PR 전체 요약 (General PR Comment)

PR 전체에 대한 총평을 하나의 comment로 작성합니다.

```markdown
## 📊 PR Review Summary

### 주요 구현 사항
* [핵심 기능/변경사항 1]
* [핵심 기능/변경사항 2]
* [핵심 기능/변경사항 3]

### 주요 우려사항 TOP 3
1. 🔴 [가장 심각한 문제]
2. 🟠 [두 번째 문제]
3. 🟡 [세 번째 문제]

### 머지 권장 여부
- ✅ **승인** - 문제 없음, 즉시 머지 가능
- ⚠️ **조건부 승인** - Minor 이슈 수정 후 머지
- ❌ **거부** - Blocker/Critical 이슈 수정 필수

**예상 수정 시간:** X시간

### 📈 통계
| 항목 | 수치 |
|------|------|
| 변경된 파일 수 | X개 |
| 추가된 라인 | +X |
| 삭제된 라인 | -X |
| 🔴 Blocker | X개 |
| 🟠 Critical | X개 |
| 🟡 Major | X개 |
| 🔵 Minor | X개 |
| 💡 Suggestion | X개 |

### ✅ 잘된 부분
- ✅ [칭찬할 점 1]
- ✅ [칭찬할 점 2]
```

---

### 💬 COMMENT TYPE 2: 전체 파일 요약 (All Files Summary)

**모든 변경된 파일을 하나의 comment로 작성합니다.**

**형식:**
```markdown
## Walkthrough
이 PR은 [프로젝트/기능명]에 대한 [변경 유형]을 포함합니다. [주요 도메인/계층]에 걸쳐 [엔티티/리포지토리/서비스/컨트롤러 등]이 신규 추가/수정되었으며, [주요 기능 1], [주요 기능 2], [주요 기능 3]이 구현되었습니다. [설정/의존성/기타 변경사항]도 포함됩니다.

## Changes
| Cohort / File(s) | 요약 |
| --- | --- |
| **[분류명]**<br>`파일경로1`<br>`파일경로2` | [해당 파일들의 주요 변경사항 요약] |
| **[분류명]**<br>`파일경로` | [해당 파일의 주요 변경사항 요약] |
| **엔티티**<br>`.../entity/User.java`<br>`.../entity/Post.java`<br>`.../entity/Comment.java` | users/posts/comments 테이블 매핑, 연관관계, 타임스탬프, 상태/메타 필드, @PreUpdate 콜백 추가. |
| **리포지토리**<br>`.../repository/UserRepository.java`<br>`.../repository/PostRepository.java`<br>`.../repository/CommentRepository.java` | JPA 리포지토리 및 파생/JPQL/네이티브 쿼리 메서드 다수 추가. |
| **서비스**<br>`.../service/UserService.java`<br>`.../service/PostService.java`<br>`.../service/CommentService.java` | CRUD, 보조 검색, 상태 변경, 민감 데이터 조회 등 비즈니스 로직 추가. |
| **컨트롤러**<br>`.../controller/UserController.java`<br>`.../controller/PostController.java`<br>`.../controller/CommentController.java` | /api/* REST 엔드포인트(CRUD, 검색, 검증, 메타/민감 데이터 응답)와 요청 DTO 추가. |
| **설정**<br>`.../config/AppConfig.java`<br>`.../config/DataInitializer.java` | CORS 설정, DB URL 메서드, CommandLineRunner로 기본 데이터 생성. |
| **예외 처리**<br>`.../exception/GlobalExceptionHandler.java` | 전역 예외 처리기 추가(예외 타입별 응답 분기). |
| **DTO**<br>`.../dto/ApiResponse.java` | 제네릭 응답 래퍼 및 success/error 팩토리 메서드 추가. |
| **유틸리티**<br>`.../util/SecurityUtil.java` | 토큰 검증, 암호화, 관리자/자격 증명 조회 메서드 추가. |
| **리소스**<br>`application.properties`<br>`static/index.html` | H2 인메모리 DB, JPA DDL, 콘솔 설정 및 단일 페이지 UI 추가. |

### 전체 통계
| 심각도 | 이슈 수 | 대표 파일 |
|--------|---------|-----------|
| 🔴 Blocker | X개 | [대표 파일들] |
| 🟠 Critical | X개 | [대표 파일들] |
| 🟡 Major | X개 | [대표 파일들] |
| 🔵 Minor | X개 | [대표 파일들] |

### 권장 개선 방향
1. **즉시 수정 필요 (P0)**: 모든 하드코딩된 비밀번호/토큰/API 키 제거, 환경 변수나 Spring Boot의 @ConfigurationProperties 사용
2. **보안 강화 (P0-P1)**: Spring Security 도입, BCrypt로 비밀번호 해싱, SQL Injection 방지 (Parameterized Query 사용), CORS 설정 제한, DTO 패턴 적용
3. **아키텍처 개선 (P1-P2)**: 생성자 주입으로 변경, @Transactional 추가, 적절한 예외 처리
4. **코드 품질 (P2-P3)**: 의미 있는 변수명 사용, Dead Code 제거, 불필요한 복잡성 제거
```

---

### 💬 COMMENT TYPE 3: 코드 라인별 이슈 (Inline Code Comments)

각 이슈마다 해당 코드 라인에 comment를 답니다.

**형식 (정확히 아래 템플릿을 따름):**

```markdown
{이슈 제목} | {심각도 이모지} {심각도}

{이슈에 대한 상세 설명}

**분류:** {카테고리} (예: Security - OWASP A02, Performance, Architecture 등)
**AI 검출 난이도:** {✅ Easy / ⚠️ Medium / 🔍 Hard}

**문제점:**
{구체적인 문제 설명}

**보안 위험 / 성능 영향 / 품질 이슈:** (해당되는 것)
- {영향 1}
- {영향 2}

**개선 방법:**
```suggestion
{수정된 코드}
```

**참고:**
- {관련 문서 링크}
- {추가 설명}
```

**심각도 이모지 매핑:**
- 🔴 Blocker
- 🟠 Critical
- 🟡 Major
- 🔵 Minor
- 💡 Suggestion

**라인 지정 방법:**
- 단일 라인: `UserController.java:42`
- 범위 라인: `UserController.java:42-45`

**예시:**

```markdown
하드코딩된 비밀번호 | 🔴 Blocker

비밀번호가 평문으로 하드코딩되어 있어 심각한 보안 취약점이 됩니다.

**분류:** Security - OWASP A02 (Cryptographic Failures)
**AI 검출 난이도:** ✅ Easy

**문제점:**
- 소스 코드에서 비밀번호가 그대로 노출됩니다
- Git 히스토리에 영구적으로 저장됩니다
- 누구나 이 비밀번호로 인증을 우회할 수 있습니다

**보안 위험:**
- 권한 없는 사용자의 시스템 접근
- 데이터 유출 및 무단 수정 가능
- 컴플라이언스 위반 (GDPR, PCI-DSS)

**개선 방법:**
```suggestion
@Column(nullable = false)
private String password; // BCrypt로 해싱하여 저장

// 비밀번호 설정 시
public void setPassword(String rawPassword) {
    this.password = passwordEncoder.encode(rawPassword);
}
```

**참고:**
- [Spring Security Password Encoding](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html)
- BCrypt 사용 권장 (work factor 최소 10 이상)
```

---

## 📋 출력 및 GitHub Comment 추가 순서

### STEP A: 화면에 출력
1. **먼저 COMMENT TYPE 1 (PR 전체 요약)을 출력**
2. **그 다음 COMMENT TYPE 2 (파일별 요약)를 각 파일마다 출력**
3. **마지막으로 COMMENT TYPE 3 (라인별 이슈)를 각 이슈마다 출력**

### STEP B: GitHub에 실제 Comment 추가

모든 리뷰를 완료한 후, `gh` CLI를 사용하여 실제 GitHub PR에 comment를 추가합니다.

#### 1. PR 전체 요약 Comment 추가
```bash
gh pr comment {PR_NUMBER} --body "$(cat <<'EOF'
[COMMENT TYPE 1 내용을 여기에]
EOF
)"
```

#### 2. 파일별 요약 Comment 추가 (각 파일마다)
```bash
# 파일명을 정확히 지정하여 comment 추가
gh pr comment {PR_NUMBER} --body "$(cat <<'EOF'
[COMMENT TYPE 2 내용을 여기에]
EOF
)"
```

#### 3. 코드 라인별 Comment 추가 (GitHub Review Comment API 사용)

**중요:** 여러 개의 코드 라인 comment를 **한 번의 API 호출**로 추가합니다.

GitHub의 Create a review API를 사용하여 모든 이슈를 한번에 등록합니다.

```bash
# 모든 이슈를 한 번에 포함하는 Review 생성
gh api repos/$REPO_OWNER/$REPO_NAME/pulls/$PR_NUMBER/reviews \
  -f body="코드 리뷰를 완료했습니다. 아래 라인별 comment를 확인해주세요." \
  -f event="COMMENT" \
  -f commit_id="$COMMIT_SHA" \
  -f comments='[
    {
      "path": "src/main/java/com/ksh/toy/aiprreviewtest/entity/User.java",
      "position": 5,
      "body": "하드코딩된 비밀번호 | 🔴 Blocker\n\n비밀번호가 평문으로 하드코딩되어 있습니다...\n\n**개선 방법:**\n```suggestion\nprivate String password;\n```"
    },
    {
      "path": "src/main/java/com/ksh/toy/aiprreviewtest/controller/UserController.java",
      "start_line": 40,
      "line": 45,
      "body": "SQL Injection 취약점 | 🔴 Blocker\n\n..."
    }
  ]'
```

**Comments 배열 구조:**
- `path`: 파일 경로 (repository root 기준)
- `body`: comment 내용 (마크다운 지원)
- **단일 라인**: `position` 사용 (diff 내 위치)
- **범위 라인**: `start_line` (시작 라인) + `line` (끝 라인) 사용

**라인 지정 방법:**
```bash
# 단일 라인 (position 사용)
{
  "path": "file.java",
  "position": 5,
  "body": "comment"
}

# 범위 라인 (start_line + line 사용)
{
  "path": "file.java",
  "start_line": 22,
  "line": 25,
  "body": "comment"
}
```

**중요:**
- `position`은 diff의 위치를 나타냅니다 (파일의 절대 라인 번호가 아님)
- 범위 지정 시에는 `start_line`(시작)과 `line`(끝)을 함께 사용해야 합니다
- 단일 라인과 범위 라인 방식은 혼용할 수 없습니다 (둘 중 하나만 선택)

#### 실행 단계별 가이드:

**STEP B-1: PR 번호 확인**
```bash
# 현재 브랜치의 PR 번호 가져오기
PR_NUMBER=$(gh pr view --json number --jq '.number')
REPO_OWNER=$(gh repo view --json owner --jq '.owner.login')
REPO_NAME=$(gh repo view --json name --jq '.name')
COMMIT_SHA=$(git rev-parse HEAD)
```

**STEP B-2: 전체 요약 Comment 추가**
```bash
gh pr comment $PR_NUMBER --body "[PR Review Summary 내용]"
```

**STEP B-3: 각 파일별 Comment 추가**
```bash
# 예시: UserController.java
gh pr comment $PR_NUMBER --body "[UserController.java 파일 요약]"
```

**STEP B-4: 모든 이슈를 한 번에 라인 Comment 추가**
```bash
# 모든 발견된 이슈를 한 번의 API 호출로 Review에 추가
gh api repos/$REPO_OWNER/$REPO_NAME/pulls/$PR_NUMBER/reviews \
  -f body="코드 리뷰를 완료했습니다. 아래 라인별 comment를 확인해주세요." \
  -f event="COMMENT" \
  -f commit_id="$COMMIT_SHA" \
  -f comments='[
    {
      "path": "src/main/java/com/ksh/toy/aiprreviewtest/entity/User.java",
      "position": 5,
      "body": "하드코딩된 비밀번호 | 🔴 Blocker\n\n비밀번호가 평문으로 하드코딩되어 있습니다...\n\n**개선 방법:**\n```suggestion\nprivate String password;\n```"
    },
    {
      "path": "src/main/java/com/ksh/toy/aiprreviewtest/controller/UserController.java",
      "start_line": 40,
      "line": 45,
      "body": "SQL Injection 취약점 | 🔴 Blocker\n\n..."
    }
  ]'
```

---

## ⚙️ GitHub Comment 자동화 가이드

### 필수 조건
1. `gh` CLI가 설치되어 있어야 함
2. GitHub 인증이 완료되어 있어야 함 (`gh auth login`)
3. 해당 repository에 write 권한이 있어야 함

### 실행 흐름

```bash
# 1. PR 정보 수집
PR_NUMBER=$(gh pr view --json number --jq '.number' 2>/dev/null || echo "")
if [ -z "$PR_NUMBER" ]; then
  echo "현재 브랜치에 연결된 PR이 없습니다. PR 번호를 입력하세요:"
  read PR_NUMBER
fi

REPO_OWNER=$(gh repo view --json owner --jq '.owner.login')
REPO_NAME=$(gh repo view --json name --jq '.name')
COMMIT_SHA=$(git rev-parse HEAD)

# 2. 전체 요약 Comment 추가
echo "Adding PR summary comment..."
gh pr comment $PR_NUMBER --body "$(cat <<'EOF'
## 📊 PR Review Summary
...
EOF
)"

# 3. 파일별 Comment 추가
gh pr comment $PR_NUMBER --body "## 📄 전체 파일 요약..."

# 4. 모든 라인별 Comment를 한 번에 추가 (Review API 사용)
echo "Adding all line-specific comments in a single review..."
gh api repos/$REPO_OWNER/$REPO_NAME/pulls/$PR_NUMBER/reviews \
  -f body="코드 리뷰를 완료했습니다. 아래 라인별 comment를 확인해주세요." \
  -f event="COMMENT" \
  -f commit_id="$COMMIT_SHA" \
  -f comments='[
    {
      "path": "...",
      "position": ...,
      "body": "..."
    },
    {
      "path": "...",
      "start_line": ...,
      "line": ...,
      "body": "..."
    }
  ]'
```

### 주의사항

1. **Commit SHA 일치**: comment를 달 때는 반드시 최신 commit SHA를 사용해야 합니다
2. **파일 경로**: repository root부터의 상대 경로를 사용합니다
3. **라인 번호**: diff의 새로운 버전 기준 라인 번호입니다
4. **Rate Limiting**: GitHub API는 시간당 제한이 있으므로 너무 많은 comment를 한 번에 달지 마세요

---

## 🤖 자동 실행 지시

리뷰를 완료한 후, 다음 단계를 **자동으로** 수행하세요:

1. ✅ **화면에 모든 리뷰 내용 출력** (사용자 확인용)

2. ✅ **사용자에게 GitHub에 comment를 추가할지 확인**
   ```
   리뷰를 완료했습니다. GitHub PR에 실제로 comment를 추가하시겠습니까? (y/n)
   ```

3. ✅ **사용자가 'y'라고 답하면:**
   - `gh pr comment` 명령어로 PR 전체 요약 추가
   - 각 파일별 요약 comment 추가
   - `gh api` 명령어로 라인별 이슈 comment 추가
   - 완료 메시지 출력

4. ✅ **각 comment가 성공적으로 추가되었는지 확인**
   - 성공: ✅ Comment 추가 완료
   - 실패: ❌ Comment 추가 실패 - [에러 메시지]

---

## 🔗 Related Resources

- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [Clean Code Principles](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)
- [Effective Java](https://www.amazon.com/Effective-Java-Joshua-Bloch/dp/0134685997)
- [Spring Boot Best Practices](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [GitHub Review Comments API](https://docs.github.com/en/rest/pulls/comments)

---

## 🎯 특별 지시사항

1. **이 프로젝트는 AI 리뷰 도구 테스트용입니다**
   - 의도적인 결함이 포함되어 있을 수 있음
   - 각 이슈에 대해 "AI가 이것을 쉽게 검출할 수 있는가?"를 평가
   - 검출 난이도를 반드시 표시 (✅ ⚠️ 🔍)

2. **실제 코드 예시를 항상 포함**
   - 문제가 있는 현재 코드
   - 수정된 코드
   - 필요시 Before/After 비교

3. **구체적이고 실행 가능한 조언**
   - "코드를 개선하세요" (X)
   - "UserService:45 - createUser() 메서드에서 비밀번호를 BCrypt로 해싱하세요" (O)

4. **긍정적인 부분도 반드시 언급**
   - 좋은 패턴, 개선 사항 칭찬
   - 팀 사기 진작

5. **우선순위를 명확히**
   - P0 (Blocker) → P1 (Critical) → P2 (Major) → P3 (Minor) 순서로 정리
   - 각 이슈에 예상 수정 시간 표시

6. **파일 위치를 정확히 표시**
   - `파일명:줄번호` 형식 준수
   - 예: `UserController.java:42-45`

이제 PR 리뷰를 시작하세요!