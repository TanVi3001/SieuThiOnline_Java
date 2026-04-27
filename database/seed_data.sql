-- ==========================================
-- SCRIPT ĐỒNG BỘ DỮ LIỆU LÕI (BASE DATA)
-- Dành cho Team chạy trước khi Import 1 triệu dòng
-- ==========================================

-- 1. TẠO CỬA HÀNG (STORES)
INSERT INTO STORES (store_id, store_name, address, is_deleted) 
VALUES ('ST001', N'Siêu thị Trung tâm UIT', N'Làng Đại học Thủ Đức', 0);

-- 2. TẠO NHÀ CUNG CẤP CHUẨN (SUPPLIERS)
INSERT INTO SUPPLIERS (supplier_id, supplier_name, is_deleted) VALUES ('SUP001', N'Công ty CP Hàng Tiêu Dùng Masan', 0);
INSERT INTO SUPPLIERS (supplier_id, supplier_name, is_deleted) VALUES ('SUP002', N'Tập đoàn Suntory PepsiCo', 0);
INSERT INTO SUPPLIERS (supplier_id, supplier_name, is_deleted) VALUES ('SUP003', N'Unilever Việt Nam', 0);
INSERT INTO SUPPLIERS (supplier_id, supplier_name, is_deleted) VALUES ('SUP004', N'Vinafood 1', 0);

-- 3. TẠO DANH MỤC THỰC TẾ (CATEGORIES) - Đúng scope của Vĩ
INSERT INTO CATEGORIES (category_id, category_name, description, is_deleted) 
VALUES ('CAT001', N'Thực phẩm khô', N'Gạo, mì, đường, gia vị', 0);

INSERT INTO CATEGORIES (category_id, category_name, description, is_deleted) 
VALUES ('CAT002', N'Đồ uống & Giải khát', N'Nước suối, nước ngọt, trà', 0);

INSERT INTO CATEGORIES (category_id, category_name, description, is_deleted) 
VALUES ('CAT003', N'Hàng tiêu dùng cá nhân', N'Dầu gội, sữa tắm, kem đánh răng', 0);

-- CHỐT SỔ (BẮT BUỘC)
COMMIT;
