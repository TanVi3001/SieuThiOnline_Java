/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import business.sql.sales_order.StatisticSql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class StatisticView extends javax.swing.JPanel {

    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0.##");

    /**
     * Creates new form StatisticView
     */
    public StatisticView() {
        if (!business.service.AuthorizationService.canAccessStatistics()) {
            showAccessDenied();
            return;
        }
        initComponents();
        initStatisticTable();
        loadStatistics();
    }

    private void showAccessDenied() {
        setLayout(new java.awt.BorderLayout());
        javax.swing.JLabel message = new javax.swing.JLabel(
                "Bạn không có quyền truy cập chức năng thống kê.",
                javax.swing.SwingConstants.CENTER
        );
        message.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        add(message, java.awt.BorderLayout.CENTER);
    }

    private void initStatisticTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Ma san pham", "Ten san pham", "So luong ban", "Doanh thu"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(model);
        jTable1.setAutoCreateRowSorter(true);
    }

    private void loadStatistics() {
        try {
            StatisticSql statisticSql = StatisticSql.getInstance();
            double revenue = statisticSql.getMonthlyRevenue();
            int totalCustomers = statisticSql.getTotalCustomers();
            int totalOrders = statisticSql.getTotalOrders();

            TotalRevenue.setText("Doanh thu thang: " + moneyFormat.format(revenue));
            TotalCustomer.setText("Tong khach hang: " + totalCustomers);
            TotalOrder.setText("Tong don hang: " + totalOrders);

            fillBestSellingProducts(statisticSql.getBestSellingProducts(20));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Loi tai thong ke: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillBestSellingProducts(List<Map<String, Object>> rows) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        for (Map<String, Object> row : rows) {
            model.addRow(new Object[]{
                row.get("product_id"),
                row.get("product_name"),
                row.get("total_sold"),
                moneyFormat.format(row.get("total_revenue"))
            });
        }
    }
// 1. Hàm lấy số lượng thẻ cảnh báo (Alert Cards)

    public Map<String, Integer> getDashboardAlerts() throws SQLException {
        Map<String, Integer> alerts = new java.util.HashMap<>();

        // Đếm số sản phẩm sắp hết hàng (Dưới 10 cái)
        String sqlLowStock = "SELECT COUNT(*) FROM INVENTORY WHERE quantity < 10 AND is_deleted = 0";
        // Đếm số đơn hàng đang chờ xử lý
        String sqlPendingOrder = "SELECT COUNT(*) FROM ORDERS WHERE status = N'Đang xử lý' AND is_deleted = 0";
        // Đếm khách hàng mới trong tháng
        String sqlNewCustomer = "SELECT COUNT(*) FROM CUSTOMERS WHERE is_deleted = 0 AND EXTRACT(MONTH FROM SYSDATE) = EXTRACT(MONTH FROM SYSDATE)"; // Giả định điều kiện khách mới

        try (Connection con = common.db.DatabaseConnection.getConnection()) {
            try (PreparedStatement ps1 = con.prepareStatement(sqlLowStock); ResultSet rs1 = ps1.executeQuery()) {
                alerts.put("low_stock", rs1.next() ? rs1.getInt(1) : 0);
            }
            try (PreparedStatement ps2 = con.prepareStatement(sqlPendingOrder); ResultSet rs2 = ps2.executeQuery()) {
                alerts.put("pending_orders", rs2.next() ? rs2.getInt(1) : 0);
            }
            try (PreparedStatement ps3 = con.prepareStatement(sqlNewCustomer); ResultSet rs3 = ps3.executeQuery()) {
                alerts.put("new_customers", rs3.next() ? rs3.getInt(1) : 0);
            }
        }
        return alerts;
    }

    // 2. Hàm lấy danh sách công việc khẩn cấp (Sắp hết hàng + Đơn chờ)
    public List<Object[]> getPriorityTasks() throws SQLException {
        List<Object[]> tasks = new java.util.ArrayList<>();

        // Truy vấn 1: Sản phẩm sắp hết hàng
        String sqlProd = "SELECT 'Hết hàng' AS loai, p.product_id, p.product_name, 'Tồn: ' || i.quantity AS trang_thai "
                + "FROM PRODUCTS p JOIN INVENTORY i ON p.product_id = i.product_id "
                + "WHERE i.quantity < 10 AND p.is_deleted = 0 AND i.is_deleted = 0 "
                + "ORDER BY i.quantity ASC FETCH FIRST 5 ROWS ONLY";

        // Truy vấn 2: Đơn hàng chưa giao
        String sqlOrder = "SELECT 'Đơn hàng' AS loai, o.order_id, c.customer_name, o.status "
                + "FROM ORDERS o JOIN CUSTOMERS c ON o.customer_id = c.customer_id "
                + "WHERE o.status = N'Đang xử lý' AND o.is_deleted = 0 "
                + "ORDER BY o.order_date ASC FETCH FIRST 5 ROWS ONLY";

        try (Connection con = common.db.DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sqlProd); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlOrder); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
                }
            }
        }
        return tasks;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tbStatistic = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        pnTop = new javax.swing.JPanel();
        pnTotalRevenue = new javax.swing.JPanel();
        TotalRevenue = new javax.swing.JLabel();
        pnTotalCustomer = new javax.swing.JPanel();
        TotalCustomer = new javax.swing.JLabel();
        pnTotalOrder = new javax.swing.JPanel();
        TotalOrder = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Số lượng bán", "Doanh thu"
            }
        ));
        tbStatistic.setViewportView(jTable1);

        pnTop.setLayout(new java.awt.GridLayout(1, 0));

        pnTotalRevenue.setBackground(new java.awt.Color(204, 255, 204));
        pnTotalRevenue.setPreferredSize(new java.awt.Dimension(92, 38));

        TotalRevenue.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        TotalRevenue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalRevenue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/image/money-Photoroom.png"))); // NOI18N
        TotalRevenue.setText("Tổng doanh thu");

        javax.swing.GroupLayout pnTotalRevenueLayout = new javax.swing.GroupLayout(pnTotalRevenue);
        pnTotalRevenue.setLayout(pnTotalRevenueLayout);
        pnTotalRevenueLayout.setHorizontalGroup(
            pnTotalRevenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TotalRevenue, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
        );
        pnTotalRevenueLayout.setVerticalGroup(
            pnTotalRevenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TotalRevenue, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
        );

        pnTop.add(pnTotalRevenue);

        pnTotalCustomer.setBackground(new java.awt.Color(255, 204, 153));

        TotalCustomer.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        TotalCustomer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/image/multiple-users-silhouette-Photoroom.png"))); // NOI18N
        TotalCustomer.setText("Tổng khách hàng");

        javax.swing.GroupLayout pnTotalCustomerLayout = new javax.swing.GroupLayout(pnTotalCustomer);
        pnTotalCustomer.setLayout(pnTotalCustomerLayout);
        pnTotalCustomerLayout.setHorizontalGroup(
            pnTotalCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TotalCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
        );
        pnTotalCustomerLayout.setVerticalGroup(
            pnTotalCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TotalCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
        );

        pnTop.add(pnTotalCustomer);

        pnTotalOrder.setBackground(new java.awt.Color(153, 204, 255));
        pnTotalOrder.setPreferredSize(new java.awt.Dimension(92, 38));

        TotalOrder.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        TotalOrder.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/image/trolley-Photoroom.png"))); // NOI18N
        TotalOrder.setText("Tổng đơn hàng");

        javax.swing.GroupLayout pnTotalOrderLayout = new javax.swing.GroupLayout(pnTotalOrder);
        pnTotalOrder.setLayout(pnTotalOrderLayout);
        pnTotalOrderLayout.setHorizontalGroup(
            pnTotalOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TotalOrder, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
        );
        pnTotalOrderLayout.setVerticalGroup(
            pnTotalOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TotalOrder, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
        );

        pnTop.add(pnTotalOrder);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnTop, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)
            .addComponent(tbStatistic)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tbStatistic, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel TotalCustomer;
    private javax.swing.JLabel TotalOrder;
    private javax.swing.JLabel TotalRevenue;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel pnTop;
    private javax.swing.JPanel pnTotalCustomer;
    private javax.swing.JPanel pnTotalOrder;
    private javax.swing.JPanel pnTotalRevenue;
    private javax.swing.JScrollPane tbStatistic;
    // End of variables declaration//GEN-END:variables
}
