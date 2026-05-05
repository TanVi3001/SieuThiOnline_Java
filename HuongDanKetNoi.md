Hướng Dẫn Cấu Hình Kết Nối Oracle Database & Real-time (Mạng LAN)
Dự án: Smart Supermarket - Store Portal
Công cụ: Oracle Database, DataGrip, Java Swing, WebSocket

Tài liệu này hướng dẫn các thành viên cách thiết lập cơ sở dữ liệu trên DataGrip và cấu hình mã nguồn Java để có thể chạy ứng dụng đồng bộ Real-time trên nhiều máy tính khác nhau thông qua mạng LAN (Wi-Fi chung).

PHẦN 1: CÀI ĐẶT CƠ SỞ DỮ LIỆU BẰNG DATAGRIP
1. Tạo kết nối (Connection) mới trong DataGrip
Mở DataGrip, bấm vào dấu + ở góc trái trên cùng (Database Explorer) -> Data Source -> Oracle.

Điền các thông tin kết nối máy chủ (Máy host DB):

Host: localhost (Nếu bạn là máy chủ) hoặc Địa_chỉ_IP_LAN_của_máy_chủ (Ví dụ: 10.0.23x.4x).

Port: 1521 (Cổng mặc định của Oracle).

SID / Service Name: Tùy phiên bản Oracle bạn cài (Thường là xe, orcl hoặc FREE) --> chọn orcl.

User/Password: Nhập tài khoản quản trị Oracle của bạn (VD: system / Admin123).

Bấm Test Connection. Nếu hiện dấu tick xanh là thành công. Bấm OK để lưu.

2. Chạy Script Khởi Tạo Dữ Liệu
Sau khi kết nối thành công, bạn cần chạy các file SQL để tạo bảng và nạp dữ liệu mồi:

Mở file database/KhoiTaoCacBang.sql, bôi đen toàn bộ lệnh và bấm Run (Nút tam giác xanh) để tạo cấu trúc bảng.  

Mở file database/seed_data.sql và chạy để nạp dữ liệu mẫu (Data Seeding) cho các danh mục, sản phẩm, nhân viên ban đầu.  

PHẦN 2: CẤU HÌNH MÃ NGUỒN JAVA
Để các máy trong nhóm có thể kết nối với nhau, cần chỉnh sửa cấu hình IP trong các file cốt lõi của dự án.

Quy tắc: Chọn ra 1 máy mạnh nhất làm Máy Chủ (Host) chạy Database và Realtime Server. Các máy còn lại là Máy Trạm (Client).

1. Cấu hình Database Connection (Tất cả các máy)
Mở file src/main/java/common/db/DatabaseConnection.java.  

Tìm chuỗi kết nối URL và thay đổi địa chỉ IP trỏ về máy chủ đang chạy Oracle:

Java
// Nếu chạy độc lập trên 1 máy (Dev ở nhà):
private static final String url = "jdbc:oracle:thin:@localhost:1521:orcl";

// LÚC DEMO NHÓM (Chạy qua mạng LAN):
// Thay localhost bằng IP của máy tính đang làm MÁY CHỦ
private static final String url = "jdbc:oracle:thin:@10.0.23x.4x:1521:orcl";

// Lưu ý: Cập nhật lại USER và PASSWORD cho đúng với máy chủ
private static final String USER = "your_oracle_user";
private static final String PASS = "Admin123";
2. Cấu hình WebSocket Server (Chỉ Máy Chủ cần quan tâm)
Mở file src/main/java/common/realtime/RealtimeServer.java.  

Để Server có thể lắng nghe các máy khác trong mạng LAN bắt sóng, địa chỉ Host bắt buộc phải là 0.0.0.0.

Lưu ý: File này thường không cần chỉnh sửa nếu đã được code cứng địa chỉ 0.0.0.0 và cổng 9999.

Khi Máy Chủ chạy file Main (SieuThiOnline.java), hệ thống sẽ tự động gọi hàm khởi động Server này lên.  

Java
// Code mẫu cấu hình trong file RealtimeServer.java
String host = "0.0.0.0"; // Lắng nghe trên mọi IP của mạng LAN
int port = 9999;         // Cổng kết nối WebSocket
3. Cấu hình WebSocket Client (Dành cho Máy Trạm)
Mở file src/main/java/common/realtime/RealtimeClient.java.  

Nhiệm vụ của file này là gõ cửa máy chủ để hóng biến. Bạn cần đổi URL kết nối trỏ về IP của Máy Chủ:

Java
// Nếu chạy độc lập trên 1 máy (Chỉ test cá nhân):
private static final String WS_URL = "ws://localhost:9999";

// LÚC DEMO NHÓM (Chạy qua mạng LAN):
// Thay localhost bằng IP của máy tính đang làm MÁY CHỦ
private static final String WS_URL = "ws://10.0.23x.4x:9999"; 
Cấu hình này giúp các máy trạm (của nhân viên) lắng nghe tín hiệu Real-time (như ORDERS_CHANGED, INVENTORY_CHANGED) từ máy chủ để tự động làm mới giao diện như bảng điều khiển, thống kê.

PHẦN 3: XỬ LÝ LỖI KẾT NỐI (TROUBLESHOOTING)
Nếu Máy Trạm đã đổi đúng IP nhưng vẫn báo lỗi kết nối DataGrip hoặc lỗi WebSocket, Máy Chủ cần kiểm tra 2 chốt chặn sau:

Tường lửa Windows (Windows Defender Firewall): Máy chủ phải tắt tạm thời tường lửa (Turn off Windows Defender Firewall ở cả mạng Public và Private) để máy trạm không bị chặn Port 1521 và 9999.

Oracle Listener:

Máy chủ mở file listener.ora (trong thư mục cài Oracle .../network/admin/).

Sửa HOST = localhost thành HOST = 0.0.0.0 hoặc HOST = Tên_Máy_Tính_Của_Bạn.

Mở services.msc -> Tìm OracleXETNSListener (hoặc tên tương tự) -> Chuột phải chọn Restart.
