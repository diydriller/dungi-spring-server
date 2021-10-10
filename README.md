## 구현사항  
* 작업일지: https://dhrhd080.tistory.com/37
* multi-module을 사용해서 의존성을 최소화
* spring-cloud를  사용해서 micro service artitecture를 구현
* redis sentinel 구축해서 redis 서버 장애 발생시 복구/redis로 서버간 데이터 공유

* config server 로부터 설정정보 주입
* auth(회원가입/sms문자전송/카카오회원가입/로그인)
* room(CRUD)
* todo(CRUD)
* memo(CRUD)
* noticeVote(CRUD)
* chart(스케줄링)
* file(이미지 정적파일 제공)
* common(entity,repository)
* elk logging

* git secret을 사용해 설정파일 암호화
* docker compose

## 포트
* eureka-server : 8761
* config-server : 9000
* gateway-server : 9001
* auth-server : 9002
* file-server : 9003
* room-server : 9004
* memo-server : 9005
* todo-server : 9006
* notice-vote-server : 9007
* chart-server : 9008

## 버전
* java 11
* gradle 6.9
* spring boot 2.4.9

* retrofit 2.9.0
* jwt 3.18.1
* twilio 8.18.0


## 엔티티 관계도
<img width="598" alt="erd" src="https://user-images.githubusercontent.com/41245313/134474902-b58d43a9-7601-4492-a298-45a576ed1ab3.png">
