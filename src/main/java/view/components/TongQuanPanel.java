package view.components;

import business.sql.sales_order.StatisticSql;
import common.db.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

// Thư viện vẽ biểu đồ
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class TongQuanPanel extends JPanel {

    private JLabel lblRevenue;
    private JLabel lblCustomers;
    private JLabel lblOrders;

    private DefaultCategoryDataset barDataset;
    private DefaultCategoryDataset lineDataset;
    private DefaultPieDataset pieDataset;

    private JPanel statsPanel;
    private DefaultTableModel tableModel;

    public TongQuanPanel() {
        // Khởi tạo trước các Dataset cho biểu đồ
        barDataset = new DefaultCategoryDataset();
        lineDataset = new DefaultCategoryDataset();
        pieDataset = new DefaultPieDataset();

        initComponents();
        loadRealData(); // Gọi hàm load DỮ LIỆU THẬT từ DB
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(240, 242, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // ==========================================
        // DÒNG 1: 3 THẺ TỔNG QUAN (Summary Cards)
        // ==========================================
        JPanel topCardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        topCardsPanel.setBackground(new Color(240, 242, 245));
        topCardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        topCardsPanel.setPreferredSize(new Dimension(1000, 120));

        lblRevenue = new JLabel("0");
        lblCustomers = new JLabel("0");
        lblOrders = new JLabel("0");

        topCardsPanel.add(createSummaryCard("Hôm nay", "DOANH THU", lblRevenue, new Color(84, 92, 200)));
        topCardsPanel.add(createSummaryCard("Hôm nay", "KHÁCH HÀNG", lblCustomers, new Color(85, 85, 85)));
        topCardsPanel.add(createSummaryCard("Hôm nay", "ĐƠN HÀNG", lblOrders, new Color(242, 98, 5)));

        add(topCardsPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));

        // ==========================================
        // DÒNG 2: 3 KHUNG BIỂU ĐỒ (Sử dụng JFreeChart)
        // ==========================================
        JPanel middleChartsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        middleChartsPanel.setBackground(new Color(240, 242, 245));
        middleChartsPanel.setPreferredSize(new Dimension(1000, 300));

        middleChartsPanel.add(createBarChartPanel());
        middleChartsPanel.add(createLineChartPanel());
        middleChartsPanel.add(createPieChartPanel());

        add(middleChartsPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));

        // ==========================================
        // DÒNG 3: THỐNG KÊ TỒN KHO & BẢNG ĐƠN HÀNG
        // ==========================================
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomPanel.setBackground(new Color(240, 242, 245));
        bottomPanel.setPreferredSize(new Dimension(1000, 250));

        // Trái: Thống kê tồn kho 
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        bottomPanel.add(statsPanel);

        // Phải: Bảng đơn hàng mới nhất
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"STT", "MÃ ĐƠN", "TRẠNG THÁI", "TIẾN ĐỘ"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setShowVerticalLines(false); // Ẩn đường kẻ dọc cho hiện đại
        table.setSelectionBackground(new Color(227, 242, 253)); // Màu khi click chọn dòng
        table.setSelectionForeground(Color.BLACK);

        // --- CODE TÔ MÀU TIÊU ĐỀ BẢNG (HEADER) ---
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(84, 92, 200)); // Màu Xanh Tím
        headerRenderer.setForeground(Color.WHITE); // Chữ Trắng
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        headerRenderer.setHorizontalAlignment(JLabel.CENTER); // Căn giữa
        headerRenderer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // --- CĂN GIỮA NỘI DUNG CÁC Ô TRONG BẢNG ---
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tableContainer.add(new JScrollPane(table), BorderLayout.CENTER);

        bottomPanel.add(tableContainer);
        add(bottomPanel);
    }

    // --- CÁC HÀM TIỆN ÍCH TẠO GIAO DIỆN ---
    private JPanel createSummaryCard(String subtitle, String title, JLabel valueLabel, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setForeground(new Color(255, 255, 255, 180));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

        leftPanel.add(lblSub);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(lblTitle);

        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));

        card.add(leftPanel, BorderLayout.WEST);
        card.add(valueLabel, BorderLayout.EAST);

        return card;
    }

    private JLabel createBoxTitle(String titleStr) {
        JLabel title = new JLabel(titleStr);
        title.setFont(new Font("Segoe UI", Font.BOLD, 12));
        title.setForeground(new Color(150, 150, 150));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        return title;
    }

    private JPanel createProgressRow(String name, int percent, String displayValue, Color color) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel lblName = new JLabel(name);
        lblName.setPreferredSize(new Dimension(150, 20));
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JProgressBar progress = new JProgressBar(0, 100);
        progress.setValue(percent);
        progress.setForeground(color);
        progress.setStringPainted(false);
        progress.setPreferredSize(new Dimension(100, 10));

        JLabel lblPercent = new JLabel(displayValue);
        lblPercent.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPercent.setForeground(Color.GRAY);

        row.add(lblName, BorderLayout.WEST);
        row.add(progress, BorderLayout.CENTER);
        row.add(lblPercent, BorderLayout.EAST);

        return row;
    }

    private JPanel createBarChartPanel() {
        JFreeChart chart = ChartFactory.createBarChart("DOANH THU 5 THÁNG QUA", "Tháng", "Triệu VNĐ", barDataset, PlotOrientation.VERTICAL, false, true, false);
        chart.setBackgroundPaint(Color.WHITE);
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        ChartPanel panel = new ChartPanel(chart);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        return panel;
    }

    private JPanel createLineChartPanel() {
        JFreeChart chart = ChartFactory.createLineChart("LƯỢT KHÁCH TRONG TUẦN", "Ngày", "Số đơn", lineDataset, PlotOrientation.VERTICAL, false, true, false);
        chart.setBackgroundPaint(Color.WHITE);
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        ChartPanel panel = new ChartPanel(chart);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        return panel;
    }

    private JPanel createPieChartPanel() {
        JFreeChart chart = ChartFactory.createPieChart("TỶ TRỌNG NGÀNH HÀNG", pieDataset, true, true, false);
        chart.setBackgroundPaint(Color.WHITE);
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        ChartPanel panel = new ChartPanel(chart);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        return panel;
    }

    // =========================================================
    // HÀM QUAN TRỌNG: MÓC DỮ LIỆU THẬT TỪ DATABASE ORACLE
    // =========================================================
    private void loadRealData() {
        try (Connection con = DatabaseConnection.getConnection()) {
            DecimalFormat df = new DecimalFormat("#,###");

            // 1. CẬP NHẬT 3 THẺ TỔNG QUAN TRÊN CÙNG
            String sqlRev = "SELECT NVL(SUM(total_amount), 0) FROM ORDERS WHERE NVL(is_deleted, 0) = 0 AND UPPER(NVL(status, '')) <> 'CANCELLED' AND TRUNC(order_date) = TRUNC(SYSDATE)";
            try (PreparedStatement ps = con.prepareStatement(sqlRev); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lblRevenue.setText(df.format(rs.getDouble(1)));
                }
            }

            String sqlCus = "SELECT COUNT(*) FROM CUSTOMERS WHERE NVL(is_deleted, 0) = 0";
            try (PreparedStatement ps = con.prepareStatement(sqlCus); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lblCustomers.setText(String.valueOf(rs.getInt(1)));
                }
            }

            String sqlOrd = "SELECT COUNT(*) FROM ORDERS WHERE NVL(is_deleted, 0) = 0 AND TRUNC(order_date) = TRUNC(SYSDATE)";
            try (PreparedStatement ps = con.prepareStatement(sqlOrd); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lblOrders.setText(String.valueOf(rs.getInt(1)));
                }
            }

            // 2. CẬP NHẬT 3 BIỂU ĐỒ BẰNG STATISTIC SQL
            StatisticSql sqlDao = StatisticSql.getInstance();

            barDataset.clear();
            Map<String, Double> revData = sqlDao.getRevenueByMonth();
            for (Map.Entry<String, Double> entry : revData.entrySet()) {
                barDataset.addValue(entry.getValue() / 1000000.0, "Doanh thu", entry.getKey());
            }

            lineDataset.clear();
            Map<String, Integer> orderData = sqlDao.getOrdersByDay();
            for (Map.Entry<String, Integer> entry : orderData.entrySet()) {
                lineDataset.addValue(entry.getValue(), "Đơn", entry.getKey());
            }

            pieDataset.clear();
            Map<String, Integer> catData = sqlDao.getCategoryDistribution();
            for (Map.Entry<String, Integer> entry : catData.entrySet()) {
                pieDataset.setValue(entry.getKey(), entry.getValue());
            }

            // 3. CẬP NHẬT THANH TIẾN TRÌNH (TOP 4 SP TỒN KHO NHIỀU NHẤT)
            statsPanel.removeAll();
            statsPanel.add(createBoxTitle("THỐNG KÊ TỒN KHO"));
            statsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

            String sqlStock = "SELECT p.product_name, NVL(i.quantity, 0) as qty "
                    + "FROM PRODUCTS p JOIN INVENTORY i ON p.product_id = i.product_id "
                    + "WHERE p.is_deleted = 0 AND i.is_deleted = 0 "
                    + "ORDER BY i.quantity DESC FETCH FIRST 4 ROWS ONLY";

            Color[] colors = {new Color(76, 175, 80), new Color(255, 152, 0), new Color(244, 67, 54), new Color(33, 150, 243)};
            int colorIdx = 0;
            try (PreparedStatement ps = con.prepareStatement(sqlStock); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("product_name");
                    int qty = rs.getInt("qty");
                    int percent = Math.min(qty, 100);
                    statsPanel.add(createProgressRow(name, percent, qty + " cái", colors[colorIdx % 4]));
                    statsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                    colorIdx++;
                }
            }
            statsPanel.revalidate();
            statsPanel.repaint();

            // 4. CẬP NHẬT BẢNG (4 ĐƠN HÀNG MỚI NHẤT)
            // 4. CẬP NHẬT BẢNG (4 ĐƠN HÀNG MỚI NHẤT)
            tableModel.setRowCount(0);
            List<Map<String, Object>> recentOrders = sqlDao.getRecentOrders(4);
            int stt = 1;
            for (Map<String, Object> order : recentOrders) {
                String orderId = (String) order.get("order_id");
                String status = (String) order.get("status");

                // Đã fix: Bổ sung thêm điều kiện check chữ tiếng Anh của Database
                String progress = (status.equalsIgnoreCase("Hoàn thành") || status.equalsIgnoreCase("COMPLETED")) ? "100%"
                        : (status.equalsIgnoreCase("Đang xử lý") || status.equalsIgnoreCase("PROCESSING")) ? "50%"
                        : "20%";

                tableModel.addRow(new Object[]{stt++, orderId, status, progress});
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi kết nối dữ liệu: " + ex.getMessage());
        }
    }
}
