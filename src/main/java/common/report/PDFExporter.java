package common.report;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import java.util.List;
import java.util.Map;
import model.product.Inventory;

/**
 * Lớp hỗ trợ xuất báo cáo định dạng PDF Hỗ trợ hiển thị Tiếng Việt và định dạng
 * bảng chuyên nghiệp.
 *
 * @author Nhóm trưởng Vĩ
 */
public class PDFExporter {

    public static void exportInventoryPDF(List<Inventory> list, String filePath) {
        // Sử dụng try-with-resources để tự động đóng file an toàn
        try (PdfWriter writer = new PdfWriter(filePath); PdfDocument pdf = new PdfDocument(writer); Document document = new Document(pdf)) {

            // 1. Cấu hình Font tiếng Việt (Lấy từ hệ thống Windows)
            // Nếu chạy trên máy khác, hãy đảm bảo đường dẫn Arial.ttf là chính xác
            String fontPath = "C:\\Windows\\Fonts\\Arial.ttf";
            PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);
            document.setFont(font);

            // 2. Thêm tiêu đề báo cáo
            Paragraph title = new Paragraph("BÁO CÁO TỒN KHO SIÊU THỊ")
                    .setBold()
                    .setFontSize(20)
                    .setMarginBottom(15);
            document.add(title);

            // 3. Khởi tạo bảng dữ liệu (5 cột với tỉ lệ độ rộng khác nhau)
            float[] columnWidths = {100, 100, 80, 80, 120};
            Table table = new Table(UnitValue.createPointArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100)); // Bảng rộng 100% trang

            // 4. Thêm hàng tiêu đề cho bảng (Header)
            table.addHeaderCell(new Paragraph("Mã SP").setBold());
            table.addHeaderCell(new Paragraph("Mã Cửa Hàng").setBold());
            table.addHeaderCell(new Paragraph("Số lượng").setBold());
            table.addHeaderCell(new Paragraph("Đơn vị").setBold());
            table.addHeaderCell(new Paragraph("Ngày cập nhật").setBold());

            // 5. Đổ dữ liệu từ danh sách vào các ô trong bảng
            if (list == null || list.isEmpty()) {
                document.add(new Paragraph("Không có dữ liệu tồn kho để hiển thị."));
            } else {
                for (Inventory inv : list) {
                    table.addCell(inv.getProductId());
                    table.addCell(inv.getStoreId());
                    table.addCell(String.valueOf(inv.getQuantity()));
                    table.addCell(inv.getUnit());
                    table.addCell(inv.getLastUpdated().toString());
                }
            }

            // 6. Thêm bảng vào tài liệu
            document.add(table);

            // 7. Thêm chữ ký hoặc ngày xuất báo cáo ở cuối
            document.add(new Paragraph("\nNgày xuất báo cáo: " + new java.util.Date().toString())
                    .setItalic()
                    .setFontSize(10));

            System.out.println("Đã xuất PDF thành công tại: " + filePath);

        } catch (Exception e) {
            System.err.println("Lỗi nghiêm trọng khi xuất PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void exportProducts(List<Map<String, Object>> products, String filePath) throws Exception {
        try (PdfWriter writer = new PdfWriter(filePath);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            // Tiêu đề
            Paragraph title = new Paragraph("DANH SÁCH SẢN PHẨM")
                    .setBold()
                    .setFontSize(20)
                    .setMarginBottom(15);
            document.add(title);

            // Tạo bảng (4 cột)
            float[] columnWidths = {100, 150, 100, 80};
            Table table = new Table(UnitValue.createPointArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Header
            table.addHeaderCell(new Paragraph("Mã SP").setBold());
            table.addHeaderCell(new Paragraph("Tên Sản Phẩm").setBold());
            table.addHeaderCell(new Paragraph("Giá").setBold());
            table.addHeaderCell(new Paragraph("Số Lượng").setBold());

            // Dữ liệu
            if (products == null || products.isEmpty()) {
                document.add(new Paragraph("Không có dữ liệu sản phẩm để hiển thị."));
            } else {
                for (Map<String, Object> product : products) {
                    table.addCell(String.valueOf(product.get("productId")));
                    table.addCell(String.valueOf(product.get("productName")));
                    table.addCell(String.valueOf(product.get("price")));
                    table.addCell(String.valueOf(product.get("quantity")));
                }
            }

            document.add(table);

            // Ngày xuất
            document.add(new Paragraph("\nNgày xuất báo cáo: " + new java.util.Date().toString())
                    .setItalic()
                    .setFontSize(10));
        }
    }
}
