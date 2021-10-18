## 구현사항
### COMMON
* multi-module을 사용해서 의존성을 최소화
* spring-cloud를  사용해서 micro service artitecture를 구현
* redis sentinel 구축해서 redis 서버 장애 발생시 복구하고 redis로 서버간 데이터 공유
* configuration server 로부터 설정정보 주입
* 설정정보 파일은 git secret을 사용해 암호화
* docker compose 사용
* elk logging 사용
* junit , mockito를 사용해서 unit test , ngrinder를 사용해서 streess test
### AUTH
* 회원가입할때 비밀번호 저장시 bcypt 사용해서 단방향 암호화
* sms 문자인증으로 twilio 서비스 사용
* kakao 회원가입/로그인시 retrofit 사용
* 로그인시 JWT 토큰 방식 사용하고 redis로 세션 공유
* transaction 사용시 propagtion 설정으로 rollback 
### ROOM
* 방 생성/삭제/입장/퇴장
* 방 퇴장시 인원이 0명이면 자동으로 방 삭제
* 페이징 사용해서 방조회
### TODO
* 오늘 할일/반복 할일 생성
* 페이징 사용해서 할일 조회
### MEMO
* 메모 생성
* 페이징 사용해서 메모 조회
### NOTICE-VOTE
* 투표/공지 생성
* 페이징 사용해서 투표/공지 조회
### CHART
* 스케줄링을 사용해 집안일 많이 한 베스트 메이트 선정


## 엔티티 
* 반복할일/하루할일로 나뉘는 할일은 상속관계 single table 사용
* 한 탭에서 같이 조회되는 조회되는 notice와 vote는 상속관계 single table 사용
* N+1 문제 해결을 위해 fetch join과 batch size 사용

<img width="598" alt="erd" src="https://user-images.githubusercontent.com/41245313/137633409-a5c5785b-0d10-47a8-b818-f39c817ce974.png">

## 버전
* java 11
* gradle 6.9
* spring boot 2.4.9
* retrofit 2.9.0
* jwt 3.18.1
* twilio 8.18.0


