# 🛒 Phần mềm Quản lý Siêu thị Online (Java Desktop)

Dự án Đồ án môn học: Xây dựng ứng dụng quản lý hệ thống Siêu thị Online. 
Sử dụng kiến trúc **MVC (Model - View - Controller)** kết hợp với **DAO (Data Access Object)**.

## 🛠 Công nghệ sử dụng
* **Ngôn ngữ:** Java (JDK 17+)
* **Giao diện:** Java Swing
* **Cơ sở dữ liệu:** Oracle Database 21c (hoặc phiên bản tương đương)
* **Kết nối CSDL:** JDBC (ojdbc8.jar)
* **IDE khuyên dùng:** Apache NetBeans / IntelliJ IDEA

## 📁 Cấu trúc thư mục (MVC Pattern)
* `model`: Chứa các lớp đối tượng thực thể (User, Product, Order...).
* `dao`: Chứa các giao thức thao tác dữ liệu với Oracle (Insert, Update, Delete, Select).
* `view`: Chứa các lớp giao diện kéo thả (JFrame, JPanel).
* `controller`: Chứa các bộ xử lý logic và bắt sự kiện từ giao diện.
* `database`: Chứa lớp `JDBCUtil.java` đảm nhận việc kết nối CSDL.

## 🚀 Hướng dẫn cài đặt và chạy dự án (Dành cho thành viên)

**Bước 1: Khởi tạo Database**
1. Mở Oracle SQL Developer hoặc DataGrip.
2. Chạy toàn bộ file script `KhoiTaoCacBang.sql` để tạo cấu trúc các bảng.

**Bước 2: Cài đặt dự án**
1. Clone dự án về máy: `git clone https://github.com/TanVi3001/SieuThiOnline_Java.git`
2. Mở dự án bằng NetBeans / IntelliJ.
3. Đảm bảo file `pom.xml` đã tải thành công thư viện `ojdbc8` (Nếu dùng Maven).

**Bước 3: Cấu hình kết nối CSDL**
1. Mở file `src/main/java/com/mycompany/sieuthionline/database/JDBCUtil.java`.
2. Sửa thông tin `username` và `password` khớp với tài khoản Oracle trên máy cá nhân của bạn:
   ```java
   String username = "TÊN_USER_ORACLE_CỦA_BẠN"; 
   String password = "MẬT_KHẨU_ORACLE_CỦA_BẠN";
