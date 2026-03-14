package common.report;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import model.Inventory; // Import model mà ông muốn xuất báo cáo

public class ExcelExporter {

    /**
     * Hàm xuất báo cáo Tồn kho ra file Excel (.xlsx)
     *
     * @param list Danh sách tồn kho lấy từ Database
     * @param filePath Đường dẫn lưu file (vd: "E:\\BaoCao.xlsx")
     * @throws java.io.IOException
     */
    public static void exportInventory(List<Inventory> list, String filePath) throws IOException {
        try ( // 1. Khởi tạo Workbook và Sheet
                Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Báo cáo Tồn kho");
            // 2. Tạo Header Style (Làm cho tiêu đề nổi bật)
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            Font font = workbook.createFont();
            font.setBold(true);
            font.setColor(IndexedColors.BLACK.getIndex());
            headerStyle.setFont(font);
            // 3. Tạo hàng tiêu đề
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Mã Sản Phẩm", "Mã Cửa Hàng", "Số Lượng", "Đơn Vị", "Ngày Cập Nhật"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            // 4. Đổ dữ liệu từ Database vào Excel
            int rowNum = 1;
            for (Inventory inv : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(inv.getProductId());
                row.createCell(1).setCellValue(inv.getStoreId());
                row.createCell(2).setCellValue(inv.getQuantity());
                row.createCell(3).setCellValue(inv.getUnit());
                row.createCell(4).setCellValue(inv.getLastUpdated().toString());
            }
            // 5. Tự động chỉnh độ rộng cột cho vừa nội dung
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            // 6. Ghi file và đóng luồng
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
        System.out.println("✅ Đã xuất file Excel thành công tại: " + filePath);
    }
}
