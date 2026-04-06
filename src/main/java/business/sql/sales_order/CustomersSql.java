package business.sql.sales_order;

import business.sql.SqlInterface;
import common.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.order.Customer;

public class CustomersSql implements SqlInterface<Customer> {

    public static CustomersSql getInstance() {
        return new CustomersSql();
    }

    @Override
    public int insert(Customer t) {
        String sql = "INSERT INTO CUSTOMERS "
                + "(CUSTOMER_ID, CUSTOMER_NAME, ROLE_ID, REWARD_POINTS, IS_DELETED, PHONE, EMAIL, ADDRESS) "
                + "VALUES (?, ?, ?, ?, 0, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, t.getCustomerId());
            pst.setString(2, t.getCustomerName());
            pst.setString(3, t.getRoleId());
            pst.setInt(4, t.getRewardPoints());
            pst.setString(5, t.getPhone());
            pst.setString(6, t.getEmail());
            pst.setString(7, t.getAddress());

            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Insert lỗi: " + e.getMessage(),
                    "SQL Error", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }

    @Override
    public int update(Customer t) {
        String sql = "UPDATE CUSTOMERS SET "
                + "CUSTOMER_NAME = ?, ROLE_ID = ?, REWARD_POINTS = ?, PHONE = ?, EMAIL = ?, ADDRESS = ? "
                + "WHERE CUSTOMER_ID = ? AND IS_DELETED = 0";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, t.getCustomerName());
            pst.setString(2, t.getRoleId());
            pst.setInt(3, t.getRewardPoints());
            pst.setString(4, t.getPhone());
            pst.setString(5, t.getEmail());
            pst.setString(6, t.getAddress());
            pst.setString(7, t.getCustomerId());

            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Update lỗi: " + e.getMessage(),
                    "SQL Error", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }

    @Override
    public int delete(String id) {
        String sql = "UPDATE CUSTOMERS SET IS_DELETED = 1 WHERE CUSTOMER_ID = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, id);
            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Delete lỗi: " + e.getMessage(),
                    "SQL Error", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }

    @Override
    public Customer selectById(String id) {
        String sql = "SELECT * FROM CUSTOMERS WHERE CUSTOMER_ID = ? AND IS_DELETED = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Customer> selectAll() {
        ArrayList<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM CUSTOMERS WHERE IS_DELETED = 0";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Customer> selectByCondition(String condition) {
        ArrayList<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM CUSTOMERS WHERE IS_DELETED = 0 "
                + (condition == null ? "" : condition);

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Customer> search(String keyword) {
        ArrayList<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM CUSTOMERS WHERE IS_DELETED = 0 AND ("
                + "LOWER(CUSTOMER_ID) LIKE ? OR "
                + "LOWER(CUSTOMER_NAME) LIKE ? OR "
                + "LOWER(NVL(PHONE, '')) LIKE ? OR "
                + "LOWER(NVL(EMAIL, '')) LIKE ? OR "
                + "LOWER(NVL(ADDRESS, '')) LIKE ? OR "
                + "LOWER(NVL(ROLE_ID, '')) LIKE ?"
                + ")";

        String k = "%" + (keyword == null ? "" : keyword.trim().toLowerCase()) + "%";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, k);
            pst.setString(2, k);
            pst.setString(3, k);
            pst.setString(4, k);
            pst.setString(5, k);
            pst.setString(6, k);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int updateRewardPoints(String customerId, int points) {
        String sql = "UPDATE CUSTOMERS SET REWARD_POINTS = ? WHERE CUSTOMER_ID = ? AND IS_DELETED = 0";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, points);
            pst.setString(2, customerId);
            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private Customer map(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setCustomerId(rs.getString("CUSTOMER_ID"));
        c.setCustomerName(rs.getString("CUSTOMER_NAME"));
        c.setRoleId(rs.getString("ROLE_ID"));
        c.setRewardPoints(rs.getInt("REWARD_POINTS"));
        c.setIsDeleted(rs.getInt("IS_DELETED"));

        // map thêm 3 cột mới
        c.setPhone(rs.getString("PHONE"));
        c.setEmail(rs.getString("EMAIL"));
        c.setAddress(rs.getString("ADDRESS"));

        return c;
    }
}
