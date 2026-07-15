-- Phase 2: 대시보드 건수 설정 + 완료 명단 숨김
-- 적용 전 백업 권장
-- 이미 컬럼이 있으면 해당 문은 에러가 날 수 있음 — 한 줄씩 실행해도 됨

ALTER TABLE event_info
    ADD COLUMN IF NOT EXISTS completed_limit INT NOT NULL DEFAULT 9;

ALTER TABLE event_info
    ADD COLUMN IF NOT EXISTS waiting_limit INT NOT NULL DEFAULT 12;

ALTER TABLE orders
    ADD COLUMN IF NOT EXISTS hidden_from_dashboard BOOLEAN NOT NULL DEFAULT FALSE;
