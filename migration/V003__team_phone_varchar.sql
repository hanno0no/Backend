-- Phase 5: 팀 관리 - 연락처를 "010-0000-0000" 형식 문자열로 저장하기 위한 컬럼 타입 변경
-- 적용 전 백업 권장
-- 기존 phone 값(전부 0으로 채워져 있던 int)은 문자열 '0'으로 변환된다. 필요하면 아래 UPDATE로 정리한다.

ALTER TABLE team
    MODIFY COLUMN phone VARCHAR(20) NULL;

-- 기존에 채워져 있던 더미 값 '0'을 비워서(NULL) 실제 연락처만 남기고 싶다면 아래 실행
-- UPDATE team SET phone = NULL WHERE phone = '0';
