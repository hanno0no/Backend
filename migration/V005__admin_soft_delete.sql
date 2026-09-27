-- Phase 7: 관리자 계정 소프트 삭제(비활성화/재활성화) 지원 + username 유니크 제약
-- 적용 전 백업 권장. V004는 이미 운영 DB에 적용된 파일이므로 절대 수정하지 말 것.

-- 관리자 삭제는 실제 row를 지우지 않고 deleted_at만 채우는 방식으로 바뀐다.
-- (완료/실패 주문의 admin_id FK가 RESTRICT라서, row를 진짜로 지우면 그 관리자가
--  처리한 완료/실패 이력이 있는 한 삭제 자체가 DB 제약 위반으로 실패하기 때문)
ALTER TABLE admin_user
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP NULL DEFAULT NULL;

-- 동시에 같은 아이디로 계정을 생성하는 요청이 겹쳐도(TOCTOU) 중복 아이디가 생기지 않도록
-- DB 레벨 유니크 제약을 추가한다. 실행 전 아래 쿼리로 기존 중복 여부를 확인해볼 수 있다:
-- SELECT username, COUNT(*) FROM admin_user GROUP BY username HAVING COUNT(*) > 1;
CREATE UNIQUE INDEX IF NOT EXISTS uq_admin_user_username ON admin_user (username);
