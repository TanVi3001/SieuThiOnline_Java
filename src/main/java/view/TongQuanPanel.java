package view;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * TongQuanPanel - Dashboard tổng quan cho Hệ thống Quản lý Siêu thị.
 * Hiển thị 4 thẻ thống kê, biểu đồ doanh thu 7 ngày và bảng giao dịch gần nhất.
 */
public class TongQuanPanel extends JPanel {

    // ===== Color scheme =====
    private static final Color BG_COLOR = new Color(245, 245, 247);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);

    private static final Color GREEN = new Color(40, 167, 69);
    private static final Color BLUE = new Color(0, 123, 255);
    private static final Color ORANGE = new Color(255, 153, 0);
    private static final Color PURPLE = new Color(111, 66, 193);

    // ===== Components =====
    private StatisticCard cardRevenue;
    private StatisticCard cardOrders;
    private StatisticCard cardLowStock;
    private StatisticCard cardEmployees;
    private ChartPlaceholder chartPanel;
    private JTable recentTable;
    private DefaultTableModel tableModel;

    public TongQuanPanel() {
        initUI();
        loadSampleData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG_COLOR);

        // Wrapper with padding
        JPanel wrapper = new JPanel(new BorderLayout(0, 16));
        wrapper.setBackground(BG_COLOR);
        wrapper.setBorder(new EmptyBorder(20, 24, 20, 24));

        // --- Header ---
        JLabel title = new JLabel("Tổng quan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Dashboard hệ thống quản lý siêu thị");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_SECONDARY);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BG_COLOR);
        headerPanel.add(title);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        headerPanel.add(subtitle);

        // --- Top cards ---
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        cardsPanel.setBackground(BG_COLOR);

        cardRevenue = new StatisticCard("\uD83D\uDCB5", "Tổng doanh thu", "0 đ", GREEN);
        cardOrders = new StatisticCard("\uD83D\uDED2", "Tổng đơn hàng", "0", BLUE);
        cardLowStock = new StatisticCard("⚠", "Sắp hết hàng", "0", ORANGE);
        cardEmployees = new StatisticCard("\uD83D\uDC65", "Nhân viên", "0", PURPLE);

        cardsPanel.add(cardRevenue);
        cardsPanel.add(cardOrders);
        cardsPanel.add(cardLowStock);
        cardsPanel.add(cardEmployees);

        // Top section = header + cards
        JPanel topSection = new JPanel(new BorderLayout(0, 16));
        topSection.setBackground(BG_COLOR);
        topSection.add(headerPanel, BorderLayout.NORTH);
        topSection.add(cardsPanel, BorderLayout.CENTER);

        // --- Middle section: chart + table ---
        JPanel middleSection = new JPanel(new GridLayout(1, 2, 16, 0));
        middleSection.setBackground(BG_COLOR);

        // Chart placeholder (left)
        chartPanel = new ChartPlaceholder();

        // Recent transactions table (right)
        JPanel tablePanel = createTransactionPanel();

        middleSection.add(chartPanel);
        middleSection.add(tablePanel);

        wrapper.add(topSection, BorderLayout.NORTH);
        wrapper.add(middleSection, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
    }

    /**
     * Tạo panel bảng giao dịch gần nhất.
     */
    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(16, 16, 16, 16)
        ));
        panel.putClientProperty("FlatLaf.style", "arc: 12");

        JLabel lblTitle = new JLabel("5 Giao dịch gần nhất");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(TEXT_PRIMARY);
        panel.add(lblTitle, BorderLayout.NORTH);

        // Table
        String[] columns = {"Mã hóa đơn", "Khách hàng", "Tổng tiền", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recentTable = new JTable(tableModel);
        recentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        recentTable.setRowHeight(36);
        recentTable.setShowGrid(false);
        recentTable.setIntercellSpacing(new Dimension(0, 0));
        recentTable.setSelectionBackground(new Color(232, 240, 254));
        recentTable.setSelectionForeground(TEXT_PRIMARY);
        recentTable.setFillsViewportHeight(true);

        JTableHeader header = recentTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(TEXT_SECONDARY);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Center-align columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < recentTable.getColumnCount(); i++) {
            recentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(recentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD_BG);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Load dữ liệu mẫu cho dashboard.
     */
    public void loadSampleData() {
        DecimalFormat df = new DecimalFormat("#,###");

        cardRevenue.setValue(df.format(125_680_000) + " đ");
        cardOrders.setValue("1,024");
        cardLowStock.setValue("12");
        cardEmployees.setValue("36");

        // Sample transactions
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{"HD001", "Nguyễn Văn A", df.format(1_250_000) + " đ", "Hoàn thành"});
        tableModel.addRow(new Object[]{"HD002", "Trần Thị B", df.format(890_000) + " đ", "Hoàn thành"});
        tableModel.addRow(new Object[]{"HD003", "Lê Văn C", df.format(2_100_000) + " đ", "Đang xử lý"});
        tableModel.addRow(new Object[]{"HD004", "Phạm Thị D", df.format(560_000) + " đ", "Hoàn thành"});
        tableModel.addRow(new Object[]{"HD005", "Hoàng Văn E", df.format(1_780_000) + " đ", "Đang xử lý"});

        // Sample chart data
        int[] sampleData = {18_500_000, 22_300_000, 19_800_000, 25_100_000, 21_600_000, 28_400_000, 24_700_000};
        chartPanel.setData(sampleData);
    }

    /**
     * Refresh lại toàn bộ dữ liệu trên dashboard.
     */
    public void refreshData() {
        loadSampleData();
        revalidate();
        repaint();
    }

    // =========================================================================
    // Inner class: StatisticCard
    // =========================================================================
    /**
     * Thẻ thống kê nhanh với icon, tiêu đề, giá trị và màu nhấn.
     */
    static class StatisticCard extends JPanel {

        private final JLabel lblValue;
        private final Color accentColor;

        StatisticCard(String icon, String title, String value, Color accentColor) {
            this.accentColor = accentColor;

            setLayout(new BorderLayout(12, 0));
            setBackground(CARD_BG);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                    new EmptyBorder(18, 18, 18, 18)
            ));
            putClientProperty("FlatLaf.style", "arc: 12");

            // Icon circle
            JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
            lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
            lblIcon.setOpaque(true);
            lblIcon.setBackground(new Color(
                    accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
            lblIcon.setPreferredSize(new Dimension(52, 52));
            lblIcon.putClientProperty("FlatLaf.style", "arc: 26");

            // Text panel
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setBackground(CARD_BG);

            JLabel lblTitle = new JLabel(title);
            lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblTitle.setForeground(TEXT_SECONDARY);

            lblValue = new JLabel(value);
            lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
            lblValue.setForeground(TEXT_PRIMARY);

            textPanel.add(lblTitle);
            textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
            textPanel.add(lblValue);

            add(lblIcon, BorderLayout.WEST);
            add(textPanel, BorderLayout.CENTER);

            // Accent bottom bar
            JPanel accentBar = new JPanel();
            accentBar.setBackground(accentColor);
            accentBar.setPreferredSize(new Dimension(0, 3));
            add(accentBar, BorderLayout.SOUTH);
        }

        void setValue(String value) {
            lblValue.setText(value);
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Lightweight drop shadow
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 15));
            g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =========================================================================
    // Inner class: ChartPlaceholder
    // =========================================================================
    /**
     * Vùng placeholder hiển thị biểu đồ cột doanh thu 7 ngày gần nhất.
     */
    static class ChartPlaceholder extends JPanel {

        private int[] data = new int[7];
        private static final String[] DAY_LABELS = {"T2", "T3", "T4", "T5", "T6", "T7", "CN"};
        private static final Color BAR_COLOR = new Color(0, 123, 255);
        private static final Color BAR_HOVER = new Color(0, 86, 179);

        ChartPlaceholder() {
            setBackground(CARD_BG);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                    new EmptyBorder(16, 16, 16, 16)
            ));
            putClientProperty("FlatLaf.style", "arc: 12");
        }

        void setData(int[] data) {
            this.data = data != null ? data.clone() : new int[7];
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            int w = getWidth();
            int h = getHeight();
            int pad = 16;

            // Title
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2.setColor(TEXT_PRIMARY);
            g2.drawString("Doanh thu 7 ngày", pad, pad + 16);

            // Chart area
            int chartTop = pad + 40;
            int chartBottom = h - pad - 30;
            int chartLeft = pad + 10;
            int chartRight = w - pad - 10;
            int chartHeight = chartBottom - chartTop;
            int chartWidth = chartRight - chartLeft;

            if (chartHeight <= 0 || chartWidth <= 0) {
                g2.dispose();
                return;
            }

            // Find max value
            int maxVal = 1;
            for (int v : data) {
                if (v > maxVal) maxVal = v;
            }

            // Grid lines
            g2.setColor(new Color(233, 236, 239));
            g2.setStroke(new BasicStroke(1f));
            for (int i = 0; i <= 4; i++) {
                int y = chartTop + (chartHeight * i / 4);
                g2.drawLine(chartLeft, y, chartRight, y);
            }

            // Draw bars
            int barCount = Math.min(data.length, 7);
            int barGap = 16;
            int totalGaps = (barCount + 1) * barGap;
            int barWidth = Math.max(1, (chartWidth - totalGaps) / barCount);

            DecimalFormat df = new DecimalFormat("#,###");
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));

            for (int i = 0; i < barCount; i++) {
                int x = chartLeft + barGap + i * (barWidth + barGap);
                int barHeight = (int) ((double) data[i] / maxVal * chartHeight);
                int y = chartBottom - barHeight;

                // Bar with gradient
                GradientPaint gp = new GradientPaint(x, y, BAR_COLOR, x, chartBottom, BAR_HOVER);
                g2.setPaint(gp);
                g2.fillRoundRect(x, y, barWidth, barHeight, 6, 6);

                // Day label
                g2.setColor(TEXT_SECONDARY);
                FontMetrics fm = g2.getFontMetrics();
                String label = i < DAY_LABELS.length ? DAY_LABELS[i] : "";
                int labelW = fm.stringWidth(label);
                g2.drawString(label, x + (barWidth - labelW) / 2, chartBottom + 18);

                // Value on top
                String valStr = df.format(data[i] / 1_000_000) + "M";
                int valW = fm.stringWidth(valStr);
                g2.setColor(TEXT_PRIMARY);
                g2.drawString(valStr, x + (barWidth - valW) / 2, y - 6);
            }

            g2.dispose();
        }
    }
}
