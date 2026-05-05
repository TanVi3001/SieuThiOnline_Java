<p align="center"><img width="454" height="126" alt="image" src="https://github.com/user-attachments/assets/2036c003-62d1-42f1-9817-6cca86de0fc8" /> </p>


## GIỚI THIỆU ĐỒ ÁN

* **Đề tài:** Xây dựng phần mềm quản lý Siêu thị Online
* **Repository:** [LẬP TRÌNH JAVA - SIÊU THỊ ONLINE](https://github.com/TanVi3001/SieuThiOnline_Java)
* **Mô tả:** Đề tài "Xây dựng Hệ thống Quản trị Siêu thị Online" là một dự án phần mềm Desktop toàn diện, được thiết kế chuyên biệt để giải quyết bài toán vận hành của mô hình bán lẻ hiện đại. Không chỉ dừng lại ở các nghiệp vụ quản lý cơ bản, hệ thống đóng vai trò như một cổng thông tin cửa hàng (Store Portal) tập trung, hỗ trợ bộ phận quản lý và nhân viên xử lý đồng bộ các khâu: Bán hàng trực tuyến (Telesale), Quản lý kho, Chăm sóc khách hàng và Quản trị nhân sự.
Điểm sáng giá nhất của dự án là việc áp dụng kiến trúc N-Tier chuẩn mực, kết hợp cùng công nghệ đồng bộ dữ liệu thời gian thực (Real-time) qua mạng LAN, giúp mọi thay đổi về tồn kho hay doanh thu đều được cập nhật lập tức trên toàn hệ thống mà không cần tải lại trang.

## CÁC TÍNH NĂNG VÀ NGHIỆP VỤ NỔI BẬT
Hệ thống được chia thành các phân hệ lõi với hàm lượng kỹ thuật cao:
* **Bán hàng & Xử lý Đơn hàng (Sales & Order Management):** Xây dựng luồng tạo đơn hàng nội bộ chuyên nghiệp với giỏ hàng, tích hợp tính toán giảm giá (Promotion), đa dạng hình thức thanh toán (Tiền mặt, Chuyển khoản) và phương thức nhận hàng (Giao tận nơi, Nhận tại quầy).


## CÔNG NGHỆ VÀ CÔNG CỤ SỬ DỤNG

**Application Development**

* [Java](https://www.java.com/) (JDK 11+) - Ngôn ngữ lập trình chính 
* [Java Swing](https://docs.oracle.com/javase/tutorial/uiswing/) - Thư viện xây dựng giao diện người dùng (GUI)
* [N-Tier Architecture](https://en.wikipedia.org/wiki/Multitier_architecture) - Kiến trúc phân tầng dự án (common, business, model, view, controller)

**Database & Tools**

* [Oracle](https://www.oracle.com/) - Hệ quản trị cơ sở dữ liệu 
* [JDBC](https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/) - Công cụ kết nối và truy cập dữ liệu
* [NetBeans](https://netbeans.apache.org/) - IDE phát triển chính
* [GitHub](https://github.com/) - Quản lý mã nguồn và làm việc nhóm

**Third-party Services**

* [Apache POI](https://poi.apache.org/) - Xử lý và xuất báo cáo file Excel (.xlsx)
* [iText 7](https://itextpdf.com/products/itext-7) - Công cụ xuất báo cáo/hóa đơn định dạng PDF (Unicode)

## CẤU TRÚC DỰ ÁN

```text
SieuThiOnline [Project Root]
├── src/main/java
│   ├── business.main
│   │   └── SieuThiOnline.java       <-- File chạy thử nghiệm & Main App
│   ├── business.service
│   │   └── PaymentService.java      <-- Xử lý Logic (Transaction, tính toán)
│   ├── business.sql
│   │   ├── SqlInterface.java        <-- "BẢN HIẾN PHÁP" CHUNG (Duy nhất ở đây)
│   │   ├── prod_inventory           <-- Phân hệ Kho & Sản phẩm
│   │   │   ├── CategoriesSql.java
│   │   │   ├── InventorySql.java
│   │   │   ├── ProductsSql.java
│   │   │   └── SuppliersSql.java
│   │   ├── rbac                     <-- Phân hệ Phân quyền & Tài khoản
│   │   │   ├── AccountSql.java
│   │   │   ├── FunctionsSql.java
│   │   │   └── ...
│   │   └── sales_order              <-- Phân hệ Đơn hàng & Khách hàng
│   │       ├── CustomersSql.java
│   │       ├── OrdersSql.java
│   │       └── OrderDetailsSql.java
│   ├── common.db
│   │   └── DatabaseConnection.java  <-- Kết nối Oracle (Singleton)
│   ├── common.report
│   │   └── ExcelExporter.java       <-- Module xuất Excel
│   └── model                        <-- Chứa các thực thể (POJO)
│       ├── Category.java
│       ├── Product.java
│       ├── Supplier.java
│       └── ...
├── .gitignore                       <-- Chặn file rác 
└── README.md                        <-- Hướng dẫn dự án
```
## THÀNH VIÊN NHÓM

| STT | MSSV | Họ và Tên | GitHub | Email | 
| :--- | :--- | :--- | :--- | :--- | 
| 1 | 24521985 | Lê Tấn Vĩ | https://github.com/TanVi3001 | 24521985@gm.uit.edu.vn |
| 2 | 24521949 | Nguyễn Đinh Tùng | https://github.com/DeeTung | 24521949@gm.uit.edu.vn |
| 3 | 24521176 | Hoàng Khôi Nguyên | https://github.com/Paulhoang8326 | 24521176@gm.uit.edu.vn | 
| 4 | 24521507 | Dương Thúy Quỳnh | https://github.com/duongthuyquynh | 24521507@gm.uit.edu.vn | 
