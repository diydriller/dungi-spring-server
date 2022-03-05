### 최고의 메이트
* spring batch와 quartz를 사용해서 매주 월요일 새벽마다 모든 방마다 1주 동안 집안일을
  가장 많이 한 사람을 선정한다.
* reader와 processor를 거쳐 처리된 item을 chunk size만큼 쌓은 뒤 writer를 통해
  데이터이스로 쿼리를 날린다.