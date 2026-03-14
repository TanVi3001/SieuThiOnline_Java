package business.sql.prod_inventory;

import model.Product;
import business.sql.SqlInterface;
import java.util.ArrayList;

/**
 * Class xử lý các thao tác Database cho bảng PRODUCTS
 * @author Tùng - UIT
 */
public class ProductsSql implements SqlInterface<Product> {

    // Áp dụng mẫu Singleton
    public static ProductsSql getInstance() {
        return new ProductsSql();
    }

    // --- CÁC HÀM RỖNG ĐỊNH NGHĨA SẴN THEO YÊU CẦU ---

    @Override
    public int insert(Product t) {
        // TODO: Viết code INSERT INTO PRODUCTS...
        return 0; 
    }

    @Override
    public int update(Product t) {
        // TODO: Viết code UPDATE PRODUCTS...
        return 0;
    }

    @Override
    public int delete(String id) {
        // TODO: Viết code UPDATE is_deleted = 1...
        return 0;
    }

    @Override
    public ArrayList<Product> selectAll() {
        // TODO: Viết code SELECT * FROM PRODUCTS...
        return new ArrayList<>();
    }

    @Override
    public Product selectById(String id) {
        // TODO: Viết code SELECT WHERE product_id = ?...
        return null;
    }

    // Các hàm nghiệp vụ riêng cho sản phẩm (nếu cần)
    public void checkPrice(String productId) {
        // Đã định nghĩa hàm rỗng để xử lý sau
    }
}