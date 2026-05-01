package view.components;

import business.sql.sales_order.StatisticSql;
import common.db.DatabaseConnection;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class TongQuanPanel extends JPanel {

    // =============================================
    // INNER CLASS: IconHelper (dùng nội bộ panel)
    // =============================================
    private static ImageIcon loadIcon(String fileName, int size) {
        try {
            // Cách 1: load qua classpath (khi build JAR)
            URL url = TongQuanPanel.class.getClassLoader()
                    .getResource("view/image/" + fileName);

            // Cách 2: load trực tiếp từ file khi chạy trong NetBeans
            if (url == null) {
                File f = new File("src/main/resources/view/image/" + fileName);
                if (f.exists()) url = f.toURI().toURL();
            }

            if (url == null) {
                System.out.println("[IconHelper] Không tìm thấy: " + fileName);
                return null;
            }

            Image scaled = new ImageIcon(url).getImage()
                    .getScaledInstance(size, size, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =============================================
    // FIELDS
    // =============================================
    private JLabel lblRevenue;
    private JLabel lblCustomers;
    private JLabel lblOrders;

    private DefaultCategoryDataset barDataset;
    private DefaultCategoryDataset lineDataset;
    private DefaultPieDataset pieDataset;

    private JPanel statsPanel;
    private DefaultTableModel tableModel;

    // Màu chủ đạo
    private static final Color COLOR_BG      = new Color(245, 247, 252);
    private static final Color COLOR_PURPLE  = new Color(84, 92, 200);
    private static final Color COLOR_BLUE    = new Color(46, 125, 185);
    private static final Color COLOR_ORANGE  = new Color(242, 98, 5);
    private static final Color COLOR_WHITE   = Color.WHITE;
    private static final Color COLOR_BORDER  = new Color(220, 220, 220);

    // =============================================
    // CONSTRUCTOR
    // =============================================
    public TongQuanPanel() {
        barDataset  = new DefaultCategoryDataset();
        lineDataset = new DefaultCategoryDataset();
        pieDataset  = new DefaultPieDataset();

        initComponents();
        loadRealData();
    }

    // =============================================
    // INIT UI
    // =============================================
    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // ------------------------------------------
        // DÒNG 1: 3 THẺ TỔNG QUAN
        // ------------------------------------------
        JPanel topCardsPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        topCardsPanel.setBackground(COLOR_BG);
        topCardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        topCardsPanel.setPreferredSize(new Dimension(1000, 110));

        lblRevenue   = new JLabel("0");
        lblCustomers = new JLabel("0");
        lblOrders    = new JLabel("0");

        topCardsPanel.add(createSummaryCard(
            "Hôm nay", "DOANH THU", lblRevenue,
            new Color(84, 92, 200), "money-Photoroom.png"
        ));
topCardsPanel.add(createSummaryCard(
    "Hôm nay", "KHÁCH HÀNG", lblCustomers,
    new Color(46, 125, 185), "multiple-users-silhouette-Photoroom.png"
));
topCardsPanel.add(createSummaryCard(
    "Hôm nay", "ĐƠN HÀNG", lblOrders,
    new Color(242, 98, 5), "trolley-Photoroom.png"
));

        add(topCardsPanel);
        add(Box.createRigidArea(new Dimension(0, 16)));

        // ------------------------------------------
        // DÒNG 2: 3 BIỂU ĐỒ
        // ------------------------------------------
        JPanel middleChartsPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        middleChartsPanel.setBackground(COLOR_BG);
        middleChartsPanel.setPreferredSize(new Dimension(1000, 280));
        middleChartsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        middleChartsPanel.add(wrapChart(createBarChartPanel(),  "DOANH THU 5 THÁNG QUA", "graph.png"));
        middleChartsPanel.add(wrapChart(createLineChartPanel(), "LƯỢT KHÁCH TRONG TUẦN",  "chart.png"));
        middleChartsPanel.add(wrapChart(createPieChartPanel(),  "TỶ TRỌNG NGÀNH HÀNG",   "public-service.png"));

        add(middleChartsPanel);
        add(Box.createRigidArea(new Dimension(0, 16)));

        // ------------------------------------------
        // DÒNG 3: TỒN KHO + BẢNG ĐƠN HÀNG
        // ------------------------------------------
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        bottomPanel.setBackground(COLOR_BG);
        bottomPanel.setPreferredSize(new Dimension(1000, 230));
        bottomPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));

        // Trái: Thống kê tồn kho
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(COLOR_WHITE);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                new EmptyBorder(14, 14, 14, 14)
        ));
        bottomPanel.add(statsPanel);

        // Phải: Bảng đơn hàng mới nhất
        bottomPanel.add(createOrderTablePanel());

        add(bottomPanel);
    }

    // =============================================
    // SUMMARY CARD — Bo góc + Icon + Đơn vị
    // =============================================
    private JPanel createSummaryCard(String subtitle, String title, JLabel valueLabel, Color bgColor, String iconFile) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(new EmptyBorder(15, 20, 15, 20));

        // TRÁI: icon nhỏ + tiêu đề xếp dọc
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setForeground(new Color(255, 255, 255, 180));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Icon + tiêu đề cùng 1 hàng ngang
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        titleRow.setOpaque(false);
        JLabel iconLabel = new JLabel(loadIcon(iconFile, 22)); // icon nhỏ 22px
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleRow.add(iconLabel);
        titleRow.add(lblTitle);

        leftPanel.add(lblSub);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(titleRow); // ← icon + chữ cùng hàng

        // PHẢI: số lớn
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));

        card.add(leftPanel, BorderLayout.WEST);
        card.add(valueLabel, BorderLayout.EAST); // ← số bên phải
        return card;
    }

    // =============================================
    // WRAP CHART — Thêm tiêu đề + icon phía trên
    // =============================================
    private JPanel wrapChart(JPanel chartPanel, String title, String iconFile) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(COLOR_WHITE);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                new EmptyBorder(0, 0, 0, 0)
        ));

        // Header của chart box
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        header.setBackground(COLOR_WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        ImageIcon icon = loadIcon(iconFile, 18);
        JLabel lblIcon  = new JLabel(icon);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(new Color(80, 80, 80));

        header.add(lblIcon);
        header.add(lblTitle);

        wrapper.add(header, BorderLayout.NORTH);
        wrapper.add(chartPanel, BorderLayout.CENTER);
        return wrapper;
    }

    // =============================================
    // TIÊU ĐỀ BOX (có icon)
    // =============================================
    private JLabel createBoxTitle(String titleStr, String iconFile) {
        ImageIcon icon = loadIcon(iconFile, 16);
        JLabel title = new JLabel("  " + titleStr, icon, JLabel.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 13));
        title.setForeground(new Color(70, 70, 70));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        return title;
    }

    // =============================================
    // PROGRESS ROW — Tồn kho
    // =============================================
    private JPanel createProgressRow(String name, int percent, String displayValue, Color color) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JLabel lblName = new JLabel(name);
        lblName.setPreferredSize(new Dimension(140, 20));
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblName.setForeground(new Color(60, 60, 60));

        JProgressBar progress = new JProgressBar(0, 100);
        progress.setValue(percent);
        progress.setForeground(color);
        progress.setBackground(new Color(235, 235, 235));
        progress.setStringPainted(false);
        progress.setPreferredSize(new Dimension(100, 8));
        progress.setBorderPainted(false);

        JLabel lblVal = new JLabel(displayValue);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblVal.setForeground(color);
        lblVal.setPreferredSize(new Dimension(60, 20));
        lblVal.setHorizontalAlignment(JLabel.RIGHT);

        row.add(lblName,    BorderLayout.WEST);
        row.add(progress,   BorderLayout.CENTER);
        row.add(lblVal,     BorderLayout.EAST);
        return row;
    }

    // =============================================
    // BẢNG ĐƠN HÀNG MỚI NHẤT
    // =============================================
    private JPanel createOrderTablePanel() {
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(COLOR_WHITE);
        tableContainer.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));

        // Header bảng có icon
        JPanel tableHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        tableHeader.setBackground(COLOR_WHITE);
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
        ImageIcon billIcon = loadIcon("bill.png", 18);
        tableHeader.add(new JLabel(billIcon));
        JLabel tblTitle = new JLabel("ĐƠN HÀNG MỚI NHẤT");
        tblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblTitle.setForeground(new Color(80, 80, 80));
        tableHeader.add(tblTitle);
        tableContainer.add(tableHeader, BorderLayout.NORTH);

        // Table model
        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"STT", "MÃ ĐƠN", "TRẠNG THÁI", "TIẾN ĐỘ"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(32);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(240, 240, 240));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setSelectionBackground(new Color(227, 242, 253));
        table.setSelectionForeground(Color.BLACK);
        table.setBackground(COLOR_WHITE);

        // Header style
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(COLOR_PURPLE);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setBorder(new EmptyBorder(6, 6, 6, 6));
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Cell căn giữa + màu xen kẽ
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                setFont(new Font("Segoe UI", Font.PLAIN, 12));
                if (!sel) {
                    setBackground(row % 2 == 0 ? COLOR_WHITE : new Color(248, 249, 255));
                    setForeground(Color.DARK_GRAY);
                }
                // Tô màu cột TRẠNG THÁI
                if (col == 2 && val != null) {
                    String s = val.toString();
                    if (s.equalsIgnoreCase("COMPLETED") || s.equalsIgnoreCase("Hoàn thành")) {
                        setForeground(new Color(46, 160, 67));
                        setFont(new Font("Segoe UI", Font.BOLD, 12));
                    } else if (s.equalsIgnoreCase("PROCESSING") || s.equalsIgnoreCase("Đang xử lý")) {
                        setForeground(new Color(230, 120, 0));
                        setFont(new Font("Segoe UI", Font.BOLD, 12));
                    } else {
                        setForeground(new Color(180, 50, 50));
                    }
                }
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(COLOR_WHITE);
        tableContainer.add(scroll, BorderLayout.CENTER);
        return tableContainer;
    }

    // =============================================
    // BIỂU ĐỒ — Đẹp hơn, có màu tùy chỉnh
    // =============================================
    private JPanel createBarChartPanel() {
        JFreeChart chart = ChartFactory.createBarChart(
                null, "Tháng", "Triệu VNĐ",
                barDataset, PlotOrientation.VERTICAL, false, true, false);
        chart.setBackgroundPaint(COLOR_WHITE);
        chart.setBorderVisible(false);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(COLOR_WHITE);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(new Color(230, 230, 230));
        plot.setDomainGridlinesVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, COLOR_PURPLE);
        renderer.setDrawBarOutline(false);
        renderer.setShadowVisible(false);
        renderer.setMaximumBarWidth(0.4);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
        domainAxis.setAxisLineVisible(false);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
        rangeAxis.setAxisLineVisible(false);

        ChartPanel panel = new ChartPanel(chart);
        panel.setPopupMenu(null);
        return panel;
    }

    private JPanel createLineChartPanel() {
        JFreeChart chart = ChartFactory.createLineChart(
                null, "Ngày", "Số đơn",
                lineDataset, PlotOrientation.VERTICAL, false, true, false);
        chart.setBackgroundPaint(COLOR_WHITE);
        chart.setBorderVisible(false);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(COLOR_WHITE);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(new Color(230, 230, 230));
        plot.setDomainGridlinesVisible(false);

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, COLOR_BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(2.5f));
        renderer.setSeriesShapesVisible(0, true);

        plot.getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
        plot.getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));

        ChartPanel panel = new ChartPanel(chart);
        panel.setPopupMenu(null);
        return panel;
    }

    private JPanel createPieChartPanel() {
        JFreeChart chart = ChartFactory.createPieChart(
                null, pieDataset, true, true, false);
        chart.setBackgroundPaint(COLOR_WHITE);
        chart.setBorderVisible(false);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(COLOR_WHITE);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 10));

        // Màu cho các phần pie
        Color[] pieColors = {
            COLOR_PURPLE,
            COLOR_BLUE,
            COLOR_ORANGE,
            new Color(46, 175, 80),
            new Color(255, 193, 7)
        };
        // Gán màu động khi có dữ liệu (sẽ áp dụng khi load data)

        ChartPanel panel = new ChartPanel(chart);
        panel.setPopupMenu(null);
        return panel;
    }

    // =============================================
    // LOAD DỮ LIỆU THẬT TỪ DATABASE
    // =============================================
    private void loadRealData() {
        try (Connection con = DatabaseConnection.getConnection()) {
            DecimalFormat df = new DecimalFormat("#,###");

            // 1. 3 THẺ TỔNG QUAN
            String sqlRev = "SELECT NVL(SUM(total_amount), 0) FROM ORDERS "
                    + "WHERE NVL(is_deleted, 0) = 0 "
                    + "AND UPPER(NVL(status, '')) <> 'CANCELLED' "
                    + "AND TRUNC(order_date) = TRUNC(SYSDATE)";
            try (PreparedStatement ps = con.prepareStatement(sqlRev);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) lblRevenue.setText(df.format(rs.getDouble(1)));
            }

            String sqlCus = "SELECT COUNT(*) FROM CUSTOMERS WHERE NVL(is_deleted, 0) = 0";
            try (PreparedStatement ps = con.prepareStatement(sqlCus);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) lblCustomers.setText(String.valueOf(rs.getInt(1)));
            }

            String sqlOrd = "SELECT COUNT(*) FROM ORDERS "
                    + "WHERE NVL(is_deleted, 0) = 0 AND TRUNC(order_date) = TRUNC(SYSDATE)";
            try (PreparedStatement ps = con.prepareStatement(sqlOrd);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) lblOrders.setText(String.valueOf(rs.getInt(1)));
            }

            // 2. BIỂU ĐỒ
            StatisticSql sqlDao = StatisticSql.getInstance();

            barDataset.clear();
            for (Map.Entry<String, Double> e : sqlDao.getRevenueByMonth().entrySet())
                barDataset.addValue(e.getValue() / 1_000_000.0, "Doanh thu", e.getKey());

            lineDataset.clear();
            for (Map.Entry<String, Integer> e : sqlDao.getOrdersByDay().entrySet())
                lineDataset.addValue(e.getValue(), "Đơn", e.getKey());

            pieDataset.clear();
            for (Map.Entry<String, Integer> e : sqlDao.getCategoryDistribution().entrySet())
                pieDataset.setValue(e.getKey(), e.getValue());

            // 3. TỒN KHO
            statsPanel.removeAll();
            statsPanel.add(createBoxTitle("THỐNG KÊ TỒN KHO", "supermarket (2).png"));
            statsPanel.add(Box.createRigidArea(new Dimension(0, 12)));

            String sqlStock = "SELECT p.product_name, NVL(i.quantity, 0) as qty "
                    + "FROM PRODUCTS p JOIN INVENTORY i ON p.product_id = i.product_id "
                    + "WHERE p.is_deleted = 0 AND i.is_deleted = 0 "
                    + "ORDER BY i.quantity DESC FETCH FIRST 4 ROWS ONLY";

            Color[] colors = {
                new Color(84, 92, 200),
                new Color(46, 125, 185),
                new Color(242, 98, 5),
                new Color(46, 160, 67)
            };
            int idx = 0;
            try (PreparedStatement ps = con.prepareStatement(sqlStock);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("product_name");
                    int qty = rs.getInt("qty");
                    statsPanel.add(createProgressRow(name, Math.min(qty, 100), qty + " cái", colors[idx % 4]));
                    statsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
                    idx++;
                }
            }
            statsPanel.revalidate();
            statsPanel.repaint();

            // 4. BẢNG ĐƠN HÀNG MỚI
            tableModel.setRowCount(0);
            int stt = 1;
            for (Map<String, Object> order : sqlDao.getRecentOrders(4)) {
                String orderId = (String) order.get("order_id");
                String status  = (String) order.get("status");
                String progress = (status.equalsIgnoreCase("Hoàn thành") || status.equalsIgnoreCase("COMPLETED")) ? "100%"
                        : (status.equalsIgnoreCase("Đang xử lý") || status.equalsIgnoreCase("PROCESSING")) ? "50%"
                        : "20%";
                tableModel.addRow(new Object[]{stt++, orderId, status, progress});
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi kết nối dữ liệu: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}