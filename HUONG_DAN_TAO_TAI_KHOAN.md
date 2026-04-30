# Hướng Dẫn Khởi Tạo Và Quản Lý Tài Khoản (SSMS)
**Dự án:** Hệ thống Quản lý Siêu thị Thông minh (Smart Supermarket Management System)

Tài liệu này hướng dẫn chi tiết quy trình khởi tạo tài khoản Quản trị viên (Root Admin) và luồng cấp phát tài khoản cho nhân viên cấp dưới theo chuẩn bảo mật phân quyền RBAC (Role-Based Access Control).

---

## 1. Khởi Tạo Hệ Thống (Dành Cho Admin)
Hệ thống áp dụng cơ chế bảo mật: **Tài khoản đăng ký đầu tiên trên Database trống sẽ tự động được cấp quyền Quản trị viên tối cao (`R_ADMIN_ALL`)**.

**Các bước thực hiện:**
1. Đảm bảo Cơ sở dữ liệu (Database) đang ở trạng thái trống (Các bảng `USERS`, `ACCOUNTS`, `ACCOUNT_ASSIGN_ROLE` không có dữ liệu).
2. Mở ứng dụng, tại màn hình **Đăng nhập**, nhấn vào liên kết **Đăng ký**.
3. Điền đầy đủ thông tin cá nhân, tên đăng nhập và mật khẩu.
4. Nhấn **Đăng ký**. Hệ thống sẽ tự động nhận diện đây là người dùng đầu tiên và gán quyền Admin.
5. Quay lại màn hình **Đăng nhập** và đăng nhập bằng tài khoản vừa tạo. Hệ thống sẽ tự động điều hướng bạn vào **Cổng Quản Trị (Admin Portal)**.

---

## 2. Quy Trình Tạo Tài Khoản Nhân Viên
Nhân viên siêu thị, quản lý cửa hàng hoặc nhân viên kho **không được phép tự do đăng ký tài khoản**. Mọi tài khoản cấp dưới đều phải do Admin tạo và trực tiếp cấp quyền.

**Các bước thực hiện:**
1. Đăng nhập vào hệ thống bằng tài khoản **Admin**.
2. Tại thanh menu bên trái (Admin Sidebar), chọn mục **Tạo tài khoản**.
3. Tại giao diện "Tạo Tài Khoản Mới", điền đầy đủ các thông tin bắt buộc:
   - Họ và Tên
   - Tên đăng nhập (Username)
   - Email
   - Số điện thoại
4. Tại mục **Cấp Quyền (Role)**, chọn chức vụ tương ứng cho nhân viên (Ví dụ: *Quản lý cửa hàng, Nhân viên bán hàng, Nhân viên kho...*).
5. Nhấn **Tạo Tài Khoản**.

> **⚠️ LƯU Ý BẢO MẬT QUAN TRỌNG:** > Hệ thống không yêu cầu nhập mật khẩu lúc tạo. Tất cả tài khoản nhân viên được tạo qua Admin Portal đều có chung một mật khẩu mặc định là: `1234`.

---

## 3. Hướng Dẫn Đăng Nhập Lần Đầu (Dành Cho Nhân Viên)
Sau khi Quản trị viên thông báo tài khoản đã được tạo thành công, nhân viên thực hiện các bước sau:

1. Mở ứng dụng, vào màn hình **Đăng nhập**.
2. Nhập **Tên đăng nhập** (hoặc Email) do Admin cung cấp.
3. Nhập mật khẩu mặc định: `1234`.
4. Bấm **Đăng nhập**. 
5. Tùy thuộc vào quyền hạn (Role) được Admin cấp lúc tạo, hệ thống sẽ tự động ẩn/hiện các chức năng trên thanh công cụ (Ví dụ: Nhân viên bán hàng sẽ không nhìn thấy chức năng Quản lý Nhân sự hay Thống kê).
