package business.sql.prod_inventory;

import business.sql.rbac.AuditLogSql;
import common.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.product.Product;

public class ProductsSql {

    private static ProductsSql instance;

    private ProductsSql() {
    }

    public static ProductsSql getInstance() {
        if (instance == null) {
            instance = new ProductsSql();
        }
        return instance;
    }

    /**
     * Trừ tồn kho trong transaction dùng chung với PaymentService
     *
     * @param con
     * @param productId
     * @param quantity
     * @return
     * @throws java.sql.SQLException
     */
    public int subtractStockWithConn(Connection con, String productId, int quantity) throws SQLException {
        return subtractStockWithConn(con, productId, quantity, null);
    }

    public int subtractStockWithConn(Connection con, String productId, int quantity, String unitId) throws SQLException {
        int baseQuantity = ProductUnitsSql.getInstance()
                .convertToBaseQuantityWithConn(con, productId, unitId, quantity);
        String sql = "UPDATE INVENTORY "
                + "SET quantity = quantity - ? "
                + "WHERE product_id = ? AND quantity >= ? AND is_deleted = 0";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, baseQuantity);
            pst.setString(2, productId);
            pst.setInt(3, baseQuantity);

            int res = pst.executeUpdate();
            if (res == 0) {
                throw new SQLException("Không đủ tồn kho hoặc không tìm thấy sản phẩm: " + productId);
            }
            return res;
        }
    }

    /**
     * Cộng trả tồn kho trong transaction khi hủy đơn hàng
     *
     * @param con
     * @param productId
     * @param quantity
     * @return
     * @throws java.sql.SQLException
     */
    public int addStockWithConn(Connection con, String productId, int quantity) throws SQLException {
        return addStockWithConn(con, productId, quantity, null);
    }

    public int addStockWithConn(Connection con, String productId, int quantity, String unitId) throws SQLException {
        int baseQuantity = ProductUnitsSql.getInstance()
                .convertToBaseQuantityWithConn(con, productId, unitId, quantity);
        String sql = "UPDATE INVENTORY "
                + "SET quantity = quantity + ? "
                + "WHERE product_id = ? AND is_deleted = 0";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, baseQuantity);
            pst.setString(2, productId);

            int res = pst.executeUpdate();
            if (res == 0) {
                throw new SQLException("Không tìm thấy sản phẩm để hoàn kho: " + productId);
            }
            return res;
        }
    }

    /**
     * Lấy toàn bộ sản phẩm + tồn kho (join inventory)
     *
     * @return
     */
    public List<Product> selectAll() {
        List<Product> list = new ArrayList<>();

        String sql = "SELECT p.product_id, p.product_name, p.base_price, "
                + "       p.category_id, p.supplier_id, "
                + "       i.store_id, i.quantity, i.unit "
                + "FROM PRODUCTS p "
                + "LEFT JOIN INVENTORY i ON p.product_id = i.product_id AND i.is_deleted = 0 "
                + "WHERE p.is_deleted = 0 "
                + "ORDER BY p.product_id";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getString("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setBasePrice(rs.getBigDecimal("base_price"));
                p.setCategoryId(rs.getString("category_id"));
                p.setSupplierId(rs.getString("supplier_id"));

                // Nếu model có các field này thì set, không có thì bỏ 2 dòng dưới
                try {
                    p.setStoreId(rs.getString("store_id"));
                } catch (Exception ignored) {
                }
                try {
                    p.setUnit(rs.getString("unit"));
                } catch (Exception ignored) {
                }

                p.setQuantity(rs.getInt("quantity"));
                p.setIsDeleted(0);
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi ProductsSql.selectAll: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thêm mới sản phẩm + khởi tạo tồn kho YÊU CẦU: - category_id tồn tại trong
     * CATEGORIES - supplier_id tồn tại trong SUPPLIERS - store_id tồn tại trong
     * STORES
     *
     * @param p
     * @return
     */
    public boolean insert(Product p) {
        String sqlProduct = "INSERT INTO PRODUCTS "
                + "(product_id, product_name, base_price, category_id, supplier_id, is_deleted) "
                + "VALUES (?, ?, ?, ?, ?, 0)";

        String sqlInventory = "INSERT INTO INVENTORY "
                + "(product_id, store_id, quantity, unit, last_updated, is_deleted) "
                + "VALUES (?, ?, ?, ?, SYSDATE, 0)";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement psProd = con.prepareStatement(sqlProduct); PreparedStatement psInv = con.prepareStatement(sqlInventory)) {

                if (isBlank(p.getProductId()) || isBlank(p.getProductName())
                        || p.getBasePrice() == null
                        || isBlank(p.getCategoryId())
                        || isBlank(p.getSupplierId())
                        || isBlank(safeStoreId(p))) {
                    throw new SQLException("Thiếu dữ liệu bắt buộc khi thêm sản phẩm.");
                }

                // PRODUCTS
                psProd.setString(1, p.getProductId());
                psProd.setString(2, p.getProductName());
                psProd.setBigDecimal(3, p.getBasePrice());
                psProd.setString(4, p.getCategoryId());
                psProd.setString(5, p.getSupplierId());
                int prodRows = psProd.executeUpdate();

                // INVENTORY
                psInv.setString(1, p.getProductId());
                psInv.setString(2, safeStoreId(p));
                psInv.setInt(3, p.getQuantity());
                psInv.setString(4, safeUnit(p));
                int invRows = psInv.executeUpdate();
                ProductUnitsSql.getInstance().ensureBaseUnitWithConn(con, p.getProductId(), safeUnit(p));

                // AUDIT INSERT
                if (prodRows > 0) {
                    String newValue = joinPairs(
                            pair("product_name", p.getProductName()),
                            pair("base_price", p.getBasePrice()),
                            pair("category_id", p.getCategoryId()),
                            pair("supplier_id", p.getSupplierId()),
                            pair("is_deleted", 0),
                            pair("store_id", safeStoreId(p)),
                            pair("quantity", p.getQuantity()),
                            pair("unit", safeUnit(p))
                    );

                    logAuditWithConn(
                            con,
                            "CREATE_PRODUCT",
                            "PRODUCT",
                            p.getProductId(),
                            null,
                            newValue,
                            "Tao moi san pham"
                    );
                }

                con.commit();
                return prodRows > 0 && invRows > 0;

            } catch (Exception e) {
                con.rollback();
                System.err.println("Lỗi ProductsSql.insert: " + e.getMessage());
                e.printStackTrace();
                return false;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi kết nối/transaction ProductsSql.insert: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật thông tin sản phẩm + tồn kho
     *
     * @param p
     * @return
     */
    public boolean update(Product p) {
        String sqlProduct = "UPDATE PRODUCTS "
                + "SET product_name = ?, base_price = ?, category_id = ?, supplier_id = ? "
                + "WHERE product_id = ? AND is_deleted = 0";

        String sqlInventory = "UPDATE INVENTORY "
                + "SET quantity = ?, unit = ?, last_updated = SYSDATE "
                + "WHERE product_id = ? AND store_id = ? AND is_deleted = 0";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement psProd = con.prepareStatement(sqlProduct); PreparedStatement psInv = con.prepareStatement(sqlInventory)) {

                // Lấy giá cũ để log (nếu cần)
                java.math.BigDecimal oldPrice = null;
                String sqlOld = "SELECT base_price FROM PRODUCTS WHERE product_id = ? AND is_deleted = 0";
                try (PreparedStatement psOld = con.prepareStatement(sqlOld)) {
                    psOld.setString(1, p.getProductId());
                    try (ResultSet rs = psOld.executeQuery()) {
                        if (rs.next()) {
                            oldPrice = rs.getBigDecimal("base_price");
                        }
                    }
                }

                // UPDATE PRODUCTS
                psProd.setString(1, p.getProductName());
                psProd.setBigDecimal(2, p.getBasePrice());
                psProd.setString(3, p.getCategoryId());
                psProd.setString(4, p.getSupplierId());
                psProd.setString(5, p.getProductId());
                int prodRows = psProd.executeUpdate();
                System.out.println("DEBUG prodRows = " + prodRows + ", productId=" + p.getProductId());

                // UPDATE INVENTORY
                psInv.setInt(1, p.getQuantity());
                psInv.setString(2, safeUnit(p));
                psInv.setString(3, p.getProductId());
                psInv.setString(4, safeStoreId(p));
                int invRows = psInv.executeUpdate();

                // Nếu inventory chưa có dòng tương ứng thì insert mới
                if (invRows == 0) {
                    String insertInv = "INSERT INTO INVENTORY "
                            + "(product_id, store_id, quantity, unit, last_updated, is_deleted) "
                            + "VALUES (?, ?, ?, ?, SYSDATE, 0)";
                    try (PreparedStatement psInsInv = con.prepareStatement(insertInv)) {
                        psInsInv.setString(1, p.getProductId());
                        psInsInv.setString(2, safeStoreId(p));
                        psInsInv.setInt(3, p.getQuantity());
                        psInsInv.setString(4, safeUnit(p));
                        psInsInv.executeUpdate();
                    }
                }

                // Ghi audit log khi update product thành công
                if (prodRows > 0) {
                    ProductUnitsSql.getInstance().ensureBaseUnitWithConn(con, p.getProductId(), safeUnit(p));
                    model.account.AuditLog log = new model.account.AuditLog();
                    log.setAccountId(
                            business.service.SessionManager.getCurrentUser() != null
                            ? business.service.SessionManager.getCurrentUser().getAccountId()
                            : null
                    );
                    log.setActionType("UPDATE_PRICE");
                    log.setEntityType("PRODUCT");
                    log.setEntityId(p.getProductId());
                    log.setOldValue("price=" + (oldPrice != null ? oldPrice.toPlainString() : "null"));
                    log.setNewValue("price=" + (p.getBasePrice() != null ? p.getBasePrice().toPlainString() : "null"));
                    log.setReason("Cap nhat gia san pham");
                    log.setIpAddress("local");
                    log.setDeviceInfo(System.getProperty("os.name") + " | Java " + System.getProperty("java.version"));

                    int ar = business.sql.rbac.AuditLogSql.getInstance().insertWithConn(con, log);
                    System.out.println("DEBUG audit rows = " + ar);
                }

                con.commit();
                return prodRows > 0;

            } catch (Exception e) {
                con.rollback();
                System.err.println("Lỗi ProductsSql.update: " + e.getMessage());
                e.printStackTrace();
                return false;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi kết nối/transaction ProductsSql.update: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa mềm sản phẩm + inventory
     *
     * @param productId
     * @return
     */
    public boolean delete(String productId) {
        String sqlProduct = "UPDATE PRODUCTS SET is_deleted = 1 WHERE product_id = ? AND is_deleted = 0";
        String sqlInv = "UPDATE INVENTORY SET is_deleted = 1 WHERE product_id = ? AND is_deleted = 0";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement psProd = con.prepareStatement(sqlProduct); PreparedStatement psInv = con.prepareStatement(sqlInv)) {

                psProd.setString(1, productId);
                int prodRows = psProd.executeUpdate();

                psInv.setString(1, productId);
                int invRows = psInv.executeUpdate();

                // AUDIT DELETE
                if (prodRows > 0) {
                    String oldValue = joinPairs(
                            pair("product.is_deleted", 0),
                            pair("inventory.is_deleted", 0)
                    );
                    String newValue = joinPairs(
                            pair("product.is_deleted", 1),
                            pair("inventory.is_deleted", 1)
                    );

                    logAuditWithConn(
                            con,
                            "DELETE_PRODUCT",
                            "PRODUCT",
                            productId,
                            oldValue,
                            newValue,
                            "Xoa mem san pham"
                    );
                }

                con.commit();
                return prodRows > 0;

            } catch (Exception e) {
                con.rollback();
                System.err.println("Lỗi ProductsSql.delete: " + e.getMessage());
                e.printStackTrace();
                return false;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi kết nối/transaction ProductsSql.delete: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUsedInOrders(String productId) {
        String sql = "SELECT COUNT(*) FROM ORDER_DETAILS "
                + "WHERE product_id = ? AND NVL(is_deleted, 0) = 0";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Loi ProductsSql.isUsedInOrders: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tìm kiếm theo tên
     *
     * @param name
     * @return
     */
    public List<Product> searchByName(String name) {
        List<Product> list = new ArrayList<>();

        String sql = "SELECT p.product_id, p.product_name, p.base_price, "
                + "       p.category_id, p.supplier_id, "
                + "       i.store_id, i.quantity, i.unit "
                + "FROM PRODUCTS p "
                + "LEFT JOIN INVENTORY i ON p.product_id = i.product_id AND i.is_deleted = 0 "
                + "WHERE p.is_deleted = 0 AND LOWER(p.product_name) LIKE LOWER(?) "
                + "ORDER BY p.product_id";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getString("product_id"));
                    p.setProductName(rs.getString("product_name"));
                    p.setBasePrice(rs.getBigDecimal("base_price"));
                    p.setCategoryId(rs.getString("category_id"));
                    p.setSupplierId(rs.getString("supplier_id"));

                    try {
                        p.setStoreId(rs.getString("store_id"));
                    } catch (Exception ignored) {
                    }
                    try {
                        p.setUnit(rs.getString("unit"));
                    } catch (Exception ignored) {
                    }

                    p.setQuantity(rs.getInt("quantity"));
                    p.setIsDeleted(0);
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi ProductsSql.searchByName: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // ===== helper =====
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String safeStoreId(Product p) {
        try {
            String s = p.getStoreId();
            return (s == null || s.isBlank()) ? "ST001" : s.trim(); // default test
        } catch (Exception e) {
            return "ST001";
        }
    }

    private String safeUnit(Product p) {
        try {
            String u = (String) p.getUnit();
            return (u == null || u.isBlank()) ? "Cái" : u.trim();
        } catch (Exception e) {
            return "Cái";
        }
    }

    private String pair(String col, Object val) {
        return col + "=" + (val == null ? "null" : String.valueOf(val).trim());
    }

    private String joinPairs(String... parts) {
        StringBuilder sb = new StringBuilder();
        if (parts != null) {
            for (String p : parts) {
                if (p != null && !p.isBlank()) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(p);
                }
            }
        }
        return sb.length() == 0 ? null : sb.toString();
    }

    private void logAuditWithConn(Connection con, String actionType, String entityType, String entityId,
            String oldValue, String newValue, String reason) throws SQLException {
        model.account.AuditLog log = new model.account.AuditLog();
        log.setAccountId(
                business.service.SessionManager.getCurrentUser() != null
                ? business.service.SessionManager.getCurrentUser().getAccountId()
                : null
        );
        log.setActionType(actionType);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setReason(reason);
        log.setIpAddress("local");
        log.setDeviceInfo(System.getProperty("os.name") + " | Java " + System.getProperty("java.version"));

        int ar = AuditLogSql.getInstance().insertWithConn(con, log);
        System.out.println("AUDIT PRODUCT rows=" + ar + ", action=" + actionType + ", entityId=" + entityId);
    }
}
