package business.sql.sales_order;

import model.order.Customer;
import business.sql.SqlInterface;
import java.util.ArrayList;
import java.util.List;

public class CustomersSql implements SqlInterface<Customer> {
    public static CustomersSql getInstance() { return new CustomersSql(); }

    @Override public int insert(Customer t) { return 0; }
    @Override public int update(Customer t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<Customer> selectAll() { return new ArrayList<>(); }
    @Override public Customer selectById(String id) { return null; }
    
    // Hàm đặc thù: Cập nhật điểm thưởng
    public int updateRewardPoints(String customerId, int points) { return 0; }

    @Override
    public List<Customer> selectByCondition(String condition) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}