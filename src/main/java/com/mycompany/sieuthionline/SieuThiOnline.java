package com.mycompany.sieuthionline;

// Import các class Model và DAO của 4 bảng Vĩ phụ trách
import com.mycompany.sieuthionline.dao.DeliveryManagementDAO;

import com.mycompany.sieuthionline.dao.StoreDAO;

import com.mycompany.sieuthionline.model.DeliveryManagement;

import com.mycompany.sieuthionline.model.Store;

public class SieuThiOnline {

    public static void main(String[] args) {
        System.out.println("=== BẮT ĐẦU TEST 4 BẢNG CỦA VĨ ===");


        // 2. Test bảng STORES (Cửa hàng)
        System.out.println("\n2. Đang test thêm Cửa hàng...");
        Store cuaHang = new Store("STORE_TEST_01", "store.langdaihoc@sieuthi.com", "Làng Đại Học Quốc Gia", "0123456789", 0);
        int kqStore = StoreDAO.getInstance().insert(cuaHang);
        if (kqStore > 0) System.out.println("✅ Thành công: Đã thêm STORES!");
        else System.out.println("❌ Thất bại: Lỗi thêm STORES.");

        // 3. Test bảng INVENTORY (Tồn kho)
        // Lưu ý: Dùng PROD_MILK_01 (đã có sẵn trong DB) và STORE_TEST_01 (vừa tạo ở trên)
        System.out.println("\n3. Đang test thêm Tồn kho...");
        java.sql.Date ngayHienTai = new java.sql.Date(System.currentTimeMillis());

        // 4. Test bảng DELIVERY_MANAGEMENT (Quản lý giao hàng)
        // Lưu ý: Dùng ORD_001 và EMP_01 (đã có sẵn trong DB từ file SQL trước)
        System.out.println("\n4. Đang test thêm Quản lý giao hàng...");
        DeliveryManagement giaoHang = new DeliveryManagement("DEL_TEST_01", "ORD_001", "EMP_01", ngayHienTai, "Đang giao", 0);
        int kqDel = DeliveryManagementDAO.getInstance().insert(giaoHang);
        if (kqDel > 0) System.out.println("✅ Thành công: Đã thêm DELIVERY_MANAGEMENT!");
        else System.out.println("❌ Thất bại: Lỗi thêm DELIVERY_MANAGEMENT.");

        System.out.println("\n=== HOÀN TẤT TEST ===");
    }
}