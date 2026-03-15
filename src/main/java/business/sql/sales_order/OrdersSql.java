package business.sql.sales_order;

import model.Order;
import business.sql.SqlInterface;
import java.util.ArrayList;

public class OrdersSql implements SqlInterface<Order> {
    public static OrdersSql getInstance() { return new OrdersSql(); }

    @Override public int insert(Order t) { return 0; }
    @Override public int update(Order t) { return 0; }
    @Override public int delete(String id) { return 0; }
    @Override public ArrayList<Order> selectAll() { return new ArrayList<>(); }
    @Override public Order selectById(String id) { return null; }
}