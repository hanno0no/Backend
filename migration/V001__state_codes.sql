-- Phase 0-1: v0.1 상태 코드 → v0.5 상태 코드
-- 기존 orders.state_num FK는 유지하고 state 테이블 값만 갱신합니다.
-- 적용 전 DB 백업을 권장합니다.

-- 이미 마이그레이션된 환경에서 재실행해도 안전하게, v0.1 코드만 갱신
UPDATE state SET state = 'submitted'       WHERE state = 'submission';
UPDATE state SET state = 'accepted'        WHERE state = 'register';
UPDATE state SET state = 'design_complete' WHERE state = 'design';
UPDATE state SET state = 'print_complete'  WHERE state = 'print';
UPDATE state SET state = 'failed'          WHERE state = 'rejection';

-- picked_up 추가 (없을 때만)
INSERT INTO state (state_num, state)
SELECT 6, 'picked_up'
WHERE NOT EXISTS (
    SELECT 1 FROM state WHERE state_num = 6 OR state = 'picked_up'
);

-- 검증용 (선택)
-- SELECT state_num, state FROM state ORDER BY state_num;
