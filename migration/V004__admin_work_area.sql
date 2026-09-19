-- Phase 6: 관리자 업무 담당 영역(접수/디자인/출력/기타) + 최초 비밀번호 미설정 상태 지원
-- 적용 전 백업 권장

CREATE TABLE IF NOT EXISTS admin_work_area (
    admin_id INT NOT NULL,
    work_area VARCHAR(20) NOT NULL,
    PRIMARY KEY (admin_id, work_area),
    CONSTRAINT fk_admin_work_area_admin
        FOREIGN KEY (admin_id) REFERENCES admin_user(admin_id)
        ON DELETE CASCADE
);

-- 계정을 비밀번호 없이 생성할 수 있도록 NULL 허용 (기존 컬럼 정의가 다르면 VARCHAR 길이 맞춰 조정)
ALTER TABLE admin_user
    MODIFY COLUMN password_hash VARCHAR(255) NULL;
