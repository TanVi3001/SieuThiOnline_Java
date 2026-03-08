package business.main;

// Import thư viện kết nối DB
import common.db.JDBCUtil;
import java.sql.Connection;

// Import đầy đủ 4 Model & DAO của mảng Đối tác, Kho bãi & Giao hàng
import business.sql.SupplierDAO;
import business.sql.StoreDAO;
import business.sql.InventoryDAO;
import business.sql.DeliveryManagementDAO;

import model.Supplier;
import model.Store;
import model.Inventory;
import model.DeliveryManagement;

public class SieuThiOnline {

    public static void main(String[] args) {
        
        // ==========================================
        // BƯỚC 0: TEST KẾT NỐI ORACLE
        // ==========================================
        System.out.println("=== KIỂM TRA KẾT NỐI ORACLE ===");
        try {
            Connection con = JDBCUtil.getConnection();
            if (con != null) {
                System.out.println("✅ Chúc mừng Vĩ! Kết nối Oracle thành công rực rỡ!");
                JDBCUtil.closeConnection(con);
            } else {
                System.out.println("❌ Kết nối thất bại, hãy kiểm tra lại file JDBCUtil!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ==========================================
        // BƯỚC 1 - 4: TEST THÊM DỮ LIỆU VÀO 4 BẢNG
        // ==========================================
        System.out.println("\n=== BẮT ĐẦU TEST 4 BẢNG CỦA VĨ ===");

        // 1. Test bảng SUPPLIERS (Nhà cung cấp)
        System.out.println("\n1. Đang test thêm Nhà cung cấp...");
        Supplier ncc = new Supplier("SUP_TEST_01", "Công ty Nước Giải Khát", "contact@ngk.com", "KCN Sóng Thần, Dĩ An", "0999888777", 0);
        int kqSup = SupplierDAO.getInstance().insert(ncc);
        if (kqSup > 0) System.out.println("✅ Thành công: Đã thêm SUPPLIERS!");
        else System.out.println("❌ Thất bại hoặc dữ liệu đã tồn tại.");

        // 2. Test bảng STORES (Cửa hàng)
        System.out.println("\n2. Đang test thêm Cửa hàng...");
        Store cuaHang = new Store("STORE_TEST_01", "store.langdaihoc@sieuthi.com", "Làng Đại Học Quốc Gia", "0123456789", 0);
        int kqStore = StoreDAO.getInstance().insert(cuaHang);
        if (kqStore > 0) System.out.println("✅ Thành công: Đã thêm STORES!");
        else System.out.println("❌ Thất bại hoặc dữ liệu đã tồn tại.");

        // Lấy ngày giờ hiện tại cho bảng 3 và 4
        java.sql.Date ngayHienTai = new java.sql.Date(System.currentTimeMillis());

        // 3. Test bảng INVENTORY (Tồn kho)
        // Dùng PROD_MILK_01 (từ script SQL của nhóm) và STORE_TEST_01 (vừa tạo ở trên)
        System.out.println("\n3. Đang test thêm Tồn kho...");
        Inventory tonKho = new Inventory("PROD_MILK_01", "STORE_TEST_01", 50, "Thùng", ngayHienTai, 0);
        int kqInv = InventoryDAO.getInstance().insert(tonKho);
        if (kqInv > 0) System.out.println("✅ Thành công: Đã thêm INVENTORY!");
        else System.out.println("❌ Thất bại hoặc dữ liệu đã tồn tại.");

        // 4. Test bảng DELIVERY_MANAGEMENT (Quản lý giao hàng)
        // Dùng ORD_001 và EMP_01 (từ script SQL của nhóm)
        System.out.println("\n4. Đang test thêm Quản lý giao hàng...");
        DeliveryManagement giaoHang = new DeliveryManagement("DEL_TEST_01", "ORD_001", "EMP_01", ngayHienTai, "Đang giao", 0);
        int kqDel = DeliveryManagementDAO.getInstance().insert(giaoHang);
        if (kqDel > 0) System.out.println("✅ Thành công: Đã thêm DELIVERY_MANAGEMENT!");
        else System.out.println("❌ Thất bại hoặc dữ liệu đã tồn tại.");

        System.out.println("\n=== HOÀN TẤT TEST ===");
    }
}