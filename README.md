# EcoAlignEvent

- 스프링부트 properties, implementation 등 설정
- user entity, dto 작성
- security, jwt 작성
- JWT 수정
- authentication 부여 코드 작성
- email sender 설정 및 작성
- user 설정
- User update 메소드 작성
- Sign-up 작성
- email authentication 토큰화
- login 수정, 메시지 없는 에러 발생 !

- 아이디 중복 확인, 이메일 인증번호 전송, 이메일 인증, 회원정보 저장(회원가입), 회원 탈퇴, 로그인, 아이디 찾기 임시완료
- 아이디 중복 확인 수정 - 최신화
- 이메일 인증번호 전송, 이메일 인증 수정 - 최신화
- 조건문 수정 및 회원정보 저장(회원가입) 수정
- 회원정보 저장(회원가입) 수정 - 최신화
- 회원탈퇴 수정 - 최신화
- 로그인 수정 - 최신화
- 아이디 찾기 수정 - 최신화
- 사용자 이메일 인증코드 전송 임시완료
- 사용자 이메일 인증 임시완료
- 비밀번호 재설정 보류

- group entity, dto 작성
- group repository, group service, group service impl, group controller 작성
- 그룹 추가, 그룹 삭제, 그룹 조회 임시완료
- schedule entity, schedule dto, schedule repository, schedule service, schedule service impl, schedule controller 작성
- 일정 추가, 일정 수정, 일정 삭제, 일정 조회 임시완료
- 그룹 삭제 시 일정 함께 삭제되도록 수정

- 이메일 인증, 이메일 인증번호 전송, 아이디 중복 확인, 회원 정보 저장, 회원 탈퇴, 로그인, 아이디 찾기, 사용자 이메일 인증코드 전송, 사용자 이메일 인증, 비밀번호 재설정 최종완료
- 로그아웃 메소드 필요
- 일정 조회, 일정 삭제, 일정 수정, 일정 추가, 그룹 추가, 그룹 삭제, 그룹 조회 ResponseEntity 반환 필요
- Memo 부분 시작 전

- 회원 탈퇴 시 비밀번호가 틀린 경우, 아이디가 없는 경우 등 추가해서 response 처리
- 일정 추가, 일정 수정, 일정 삭제, 일정 조회 시 토큰 만료로 인한 에러 처리 - response 처리
- 그룹 추가, 그룹 삭제, 그룹 조회 시 토큰 만료로 인한 에러 처리 - response 처리
- memo entity, memo dto, memo repository, memo service, memo service impl, memo controller 작성
- 일정 수정 메소드 수정, 메모 추가, 메모 수정, 메모 삭제, 메모 조회 임시완료

- 모든 메소드 확인 및 연결 성공, 메모, 그룹, 일정에 아이디 식별 추가 시작
