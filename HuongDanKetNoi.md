# Hướng Dẫn Cấu Hình Kết Nối Oracle Database & Real-time (Mạng LAN)
**Dự án:** Smart Supermarket - Store Portal
**Công cụ:** Oracle Database, DataGrip, Java Swing, WebSocket

Tài liệu này hướng dẫn các thành viên cách thiết lập cơ sở dữ liệu trên DataGrip và cấu hình mã nguồn Java để có thể chạy ứng dụng đồng bộ Real-time trên nhiều máy tính khác nhau thông qua mạng LAN (Wi-Fi chung).

---

## PHẦN 1: CÀI ĐẶT CƠ SỞ DỮ LIỆU BẰNG DATAGRIP

### 1. Tạo kết nối (Connection) mới trong DataGrip
1. Mở DataGrip, bấm vào dấu **+** ở góc trái trên cùng (Database Explorer) -> **Data Source** -> **Oracle**.
2. Điền các thông tin kết nối máy chủ (Máy host DB):
   * **Host:** `localhost` (Nếu bạn là máy chủ) hoặc `Địa_chỉ_IP_LAN_của_máy_chủ` (Ví dụ: `10.0.23x.4x`).
   * **Port:** `1521` (Cổng mặc định của Oracle).
   * **SID / Service Name:** Tùy phiên bản Oracle bạn cài (Thường là `xe`, `orcl` hoặc `FREE`) --> chọn orcl.
   * **User/Password:** Nhập tài khoản quản trị Oracle của bạn (VD: `system` / `Admin123`).
3. Bấm **Test Connection**. Nếu hiện dấu tick xanh là thành công. Bấm **OK** để lưu.

### 2. Chạy Script Khởi Tạo Dữ Liệu
Sau khi kết nối thành công, bạn cần chạy các file SQL để tạo bảng và nạp dữ liệu mồi:
1. Mở file `database/KhoiTaoCacBang.sql`, bôi đen toàn bộ lệnh và bấm **Run** (Nút tam giác xanh) để tạo cấu trúc bảng.
2. Mở file `database/seed_data.sql` và chạy để nạp dữ liệu mẫu (Data Seeding) cho các danh mục, sản phẩm, nhân viên ban đầu.

---

## PHẦN 2: CẤU HÌNH MÃ NGUỒN JAVA

Để các máy trong nhóm có thể kết nối với nhau, cần chỉnh sửa cấu hình IP trong 2 file cốt lõi của dự án.

### 1. Cấu hình Database Connection
Mở file `src/main/java/common/db/DatabaseConnection.java`.

Tìm chuỗi kết nối `URL` và thay đổi địa chỉ IP trỏ về máy chủ đang chạy Oracle:

```java
// Nếu chạy độc lập trên 1 máy:
private static final String url = "jdbc:oracle:thin:@localhost:1521:orcl";

// LÚC DEMO NHÓM (Chạy qua mạng LAN):
// Thay localhost bằng IP của máy tính đang chạy Oracle Database
private static final String url = "jdbc:oracle:thin:@10.0.23x.4x:1521:orcl";

// Lưu ý: Cập nhật lại USER và PASSWORD cho đúng với máy chủ
private static final String USER = "your_oracle_user";
private static final String PASS = "your_oracle_password";
