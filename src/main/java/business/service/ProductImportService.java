package service;

import java.io.*;
import java.sql.*;
import java.util.function.Consumer;
import common.db.DatabaseConnection;

public class ProductImportService {

    private static final int BATCH_SIZE = 5000;

    public void importProductCSV(String filePath, Consumer<Integer> progressCallback) {
        String sql = "INSERT INTO PRODUCTS (product_id, product_name, base_price, category_id, supplier_id) VALUES (?, ?, ?, ?, ?)";
        long totalLines = 1000000;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            if (conn == null) {
                throw new SQLException("Kết nối DB thất bại!");
            }
            conn.setAutoCommit(false);

            String line;
            br.readLine(); // Bỏ qua header
            int count = 0;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    pstmt.setString(1, data[0].trim());
                    pstmt.setString(2, data[1].trim());
                    pstmt.setDouble(3, Double.parseDouble(data[2].trim()));
                    pstmt.setString(4, data[3].trim());
                    pstmt.setString(5, data[4].trim());
                    pstmt.addBatch();
                }

                if (++count % BATCH_SIZE == 0) {
                    pstmt.executeBatch();
                    conn.commit();
                    progressCallback.accept((int) ((count / (double) totalLines) * 100));
                }
            }
            pstmt.executeBatch();
            conn.commit();
            progressCallback.accept(100);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
