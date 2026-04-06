-- ==========================================================
-- 1. XÓA DỮ LIỆU CŨ (Để tránh lỗi trùng khóa khi chạy lại)
-- =-- Xóa theo thứ tự ngược lại của khóa ngoại
-- ==========================================================
DELETE FROM ACCOUNT_ASSIGN_ROLE_GROUP WHERE account_id IN ('ACC001', 'ACC002');
DELETE FROM ACCOUNTS WHERE username IN ('admin', 'staff');
DELETE FROM USERS WHERE email IN ('admin@sieuthionline.local', 'staff@sieuthionline.local');
DELETE FROM ROLE_GROUPS WHERE role_group_id IN ('RG001', 'RG002');

-- ---------------------------------------------------------
-- 2) THÊM NHÓM QUYỀN (ROLE_GROUPS)
-- ---------------------------------------------------------
INSERT INTO ROLE_GROUPS (role_group_id, group_name, is_deleted)
VALUES ('RG001', 'ADMIN', 0);

INSERT INTO ROLE_GROUPS (role_group_id, group_name, is_deleted)
VALUES ('RG002', 'STAFF', 0);

-- ---------------------------------------------------------
-- 3) THÊM THÔNG TIN NGƯỜI DÙNG (USERS)
-- ---------------------------------------------------------
INSERT INTO USERS (user_id, full_name, email, phone_number, is_deleted)
VALUES ('U001', 'System Admin', 'admin@sieuthionline.local', '0900000001', 0);

INSERT INTO USERS (user_id, full_name, email, phone_number, is_deleted)
VALUES ('U002', 'Store Staff', 'staff@sieuthionline.local', '0900000002', 0);

-- ---------------------------------------------------------
-- 4) THÊM TÀI KHOẢN (ACCOUNTS)
-- Mật khẩu để '123' cho ông dễ test, vì code chưa có Bcrypt
-- ---------------------------------------------------------
INSERT INTO ACCOUNTS (account_id, user_id, username, password, status, is_deleted)
VALUES ('ACC001', 'U001', 'admin', '123', 'ACTIVE', 0);

INSERT INTO ACCOUNTS (account_id, user_id, username, password, status, is_deleted)
VALUES ('ACC002', 'U002', 'staff', '123', 'ACTIVE', 0);

-- ---------------------------------------------------------
-- 5) GÁN QUYỀN VÀO NHÓM (ACCOUNT_ASSIGN_ROLE_GROUP)
-- Theo Schema của ông, đây là bảng trung gian để phân quyền
-- ---------------------------------------------------------
INSERT INTO ACCOUNT_ASSIGN_ROLE_GROUP (account_id, role_group_id, is_deleted)
VALUES ('ACC001', 'RG001', 0);

INSERT INTO ACCOUNT_ASSIGN_ROLE_GROUP (account_id, role_group_id, is_deleted)
VALUES ('ACC002', 'RG002', 0);

-- Kết thúc bằng COMMIT để lưu dữ liệu thật vào DB
COMMIT;
