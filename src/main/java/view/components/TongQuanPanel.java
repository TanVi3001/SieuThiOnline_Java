/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * TongQuanPanel - Modern Dashboard Panel hiển thị mặc định khi mở Dashboard.
 * Hiển thị thống kê, biểu đồ doanh thu 7 ngày, và thao tác nhanh.
 * Sử dụng UIManager.getColor() để tương thích Light/Dark mode (FlatLaf).
 *
 * Hướng dẫn kết nối với DashboardView:
 * <pre>
 *   TongQuanPanel panel = new TongQuanPanel();
 *   panel.setDashboardCallback(action -> {
 *       switch (action) {
 *           case "Tạo đơn hàng"   -> showPanel(new OrderView());
 *           case "Thêm sản phẩm"  -> showPanel(new ProductView());
 *           case "Thêm khách hàng"-> showPanel(new CustomerView());
 *           case "Xem thống kê"   -> showPanel(new StatisticView());
 *       }
 *   });
 * </pre>
 *
 * @author Le Tan Vi
 */
public class TongQuanPanel extends JPanel {

    // ===== Callback Interface =====
    /**
     * Interface để kết nối các nút thao tác nhanh với DashboardView.
     * Implement interface này và gọi setDashboardCallback() để nhận sự kiện.
     */
    public interface DashboardCallback {
        void onQuickActionClicked(String action);
    }

    // ===== Constants =====
    private static final String FONT_FAMILY = "Segoe UI";
    private static final int TITLE_FONT_SIZE = 24;
    private static final int STAT_VALUE_FONT_SIZE = 20;
    private static final int STAT_TITLE_FONT_SIZE = 12;
    private static final int EMOJI_FONT_SIZE = 28;
    private static final int ACCENT_BAR_HEIGHT = 4;
    private static final int CARD_PADDING = 15;
    private static final int PANEL_PADDING = 20;
    private static final int GAP = 8;

    // Dữ liệu mẫu biểu đồ doanh thu 7 ngày (đơn vị: triệu VNĐ)
    private static final double[] REVENUE_DATA = {2.5, 3.2, 2.1, 4.8, 3.5, 5.2, 2.9};
    private static final String[] DAY_LABELS = {"T2", "T3", "T4", "T5", "T6", "T7", "CN"};
    private static final double REVENUE_TARGET = 4.0; // Mục tiêu doanh thu (triệu VNĐ)

    // ===== Các thành phần giao diện =====
    private DashboardCallback dashboardCallback;

    /**
     * Constructor - Khởi tạo giao diện Modern Dashboard.
     */
    public TongQuanPanel() {
        initUI();
    }

    /**
     * Gán callback để nhận sự kiện từ các nút thao tác nhanh.
     *
     * @param callback đối tượng DashboardCallback
     */
    public void setDashboardCallback(DashboardCallback callback) {
        this.dashboardCallback = callback;
    }

    /**
     * Getter cho callback hiện tại.
     *
     * @return DashboardCallback hiện tại hoặc null
     */
    public DashboardCallback getDashboardCallback() {
        return dashboardCallback;
    }

    // =========================================================================
    // KHỞI TẠO GIAO DIỆN CHÍNH
    // =========================================================================

    /**
     * Khởi tạo toàn bộ giao diện chính với GridBagLayout.
     * Bố cục: Tiêu đề > 4 Stat Cards > Biểu đồ + Thao tác nhanh.
     */
    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));

        // === Tiêu đề ===
        JLabel lblTitle = new JLabel("\uD83D\uDCCA Tổng Quan");
        lblTitle.setFont(new Font(FONT_FAMILY, Font.BOLD, TITLE_FONT_SIZE));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // === Nội dung chính ===
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(GAP, GAP, GAP, GAP);
        gbc.weighty = 0;

        // --- Hàng 1: 4 Card thống kê ---
        gbc.gridy = 0;
        gbc.weightx = 0.25;

        gbc.gridx = 0;
        contentPanel.add(createStatCard("Sản phẩm", "1,250", "\uD83D\uDCE6",
                new Color(33, 150, 243)), gbc);  // Xanh dương

        gbc.gridx = 1;
        contentPanel.add(createStatCard("Đơn hàng hôm nay", "48", "\uD83D\uDCC4",
                new Color(76, 175, 80)), gbc);    // Xanh lá

        gbc.gridx = 2;
        contentPanel.add(createStatCard("Khách hàng", "3,820", "\uD83D\uDC65",
                new Color(255, 152, 0)), gbc);    // Cam

        gbc.gridx = 3;
        contentPanel.add(createStatCard("Doanh thu tháng", "125,000,000 ₫", "\uD83D\uDCB0",
                new Color(233, 30, 99)), gbc);    // Hồng

        // --- Hàng 2: Biểu đồ (chiếm 3 cột) + Thao tác nhanh (1 cột) ---
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 0.75;
        gbc.weighty = 1.0;
        contentPanel.add(createRevenueChartPanel(), gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.25;
        contentPanel.add(createQuickActionsPanel(), gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    // =========================================================================
    // STAT CARDS VỚI ACCENT BAR VÀ HOVER EFFECT
    // =========================================================================

    /**
     * Tạo Card thống kê với icon emoji, tiêu đề, giá trị, thanh accent,
     * và hiệu ứng hover đổi màu nền.
     *
     * @param title      Tiêu đề thẻ
     * @param value      Giá trị hiển thị
     * @param emoji      Emoji icon lớn
     * @param accentColor Màu thanh accent phía dưới
     * @return JPanel card thống kê
     */
    private JPanel createStatCard(String title, String value, String emoji, Color accentColor) {
        // Panel chính chứa nội dung card và thanh accent
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, getThemeBorderColor()),
                new EmptyBorder(0, 0, 0, 0)
        ));

        // Panel nội dung card
        JPanel card = new JPanel(new GridBagLayout());
        card.setBorder(new EmptyBorder(CARD_PADDING, CARD_PADDING, CARD_PADDING, CARD_PADDING));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Emoji icon (28px+)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.anchor = GridBagConstraints.NORTH;
        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(new Font(FONT_FAMILY, Font.PLAIN, EMOJI_FONT_SIZE));
        card.add(lblEmoji, gbc);

        // Tiêu đề
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 2, 0);
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font(FONT_FAMILY, Font.PLAIN, STAT_TITLE_FONT_SIZE));
        lblTitle.setForeground(getThemeSecondaryTextColor());
        card.add(lblTitle, gbc);

        // Giá trị
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font(FONT_FAMILY, Font.BOLD, STAT_VALUE_FONT_SIZE));
        card.add(lblValue, gbc);

        wrapper.add(card, BorderLayout.CENTER);

        // Thanh accent màu phía dưới (cao 4px)
        JPanel accentBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        accentBar.setPreferredSize(new Dimension(0, ACCENT_BAR_HEIGHT));
        wrapper.add(accentBar, BorderLayout.SOUTH);

        // Hover effect: Đổi màu nền khi chuột qua
        Color normalBg = card.getBackground();
        wrapper.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Color hoverBg = getThemeHoverColor();
                card.setBackground(hoverBg);
                wrapper.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Color bg = UIManager.getColor("Panel.background");
                if (bg == null) bg = normalBg;
                card.setBackground(bg);
                wrapper.setBackground(bg);
            }
        });

        // Tooltip cho card
        wrapper.setToolTipText(title + ": " + value);

        return wrapper;
    }

    // =========================================================================
    // BIỂU ĐỒ CỘT DOANH THU 7 NGÀY
    // =========================================================================

    /**
     * Tạo panel chứa biểu đồ cột "Doanh thu 7 ngày" vẽ bằng paintComponent.
     * Bao gồm: 7 cột gradient, trục tọa độ, grid lines, nhãn dữ liệu, legend.
     *
     * @return JPanel biểu đồ doanh thu
     */
    private JPanel createRevenueChartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createMatteBorder(1, 1, 1, 1, getThemeBorderColor()),
                        "\uD83D\uDCCA Doanh thu 7 ngày gần nhất",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font(FONT_FAMILY, Font.BOLD, 14)
                ),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Panel vẽ biểu đồ
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawRevenueChart((Graphics2D) g, getWidth(), getHeight());
            }
        };
        chartPanel.setOpaque(false);
        panel.add(chartPanel, BorderLayout.CENTER);

        // Legend panel
        JPanel legendPanel = createChartLegend();
        panel.add(legendPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Vẽ biểu đồ cột doanh thu với gradient, trục tọa độ, grid lines, nhãn dữ liệu.
     *
     * @param g2     Graphics2D context
     * @param width  Chiều rộng vùng vẽ
     * @param height Chiều cao vùng vẽ
     */
    private void drawRevenueChart(Graphics2D g2, int width, int height) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tính toán vùng vẽ biểu đồ
        int leftMargin = 60;
        int rightMargin = 20;
        int topMargin = 20;
        int bottomMargin = 40;

        int chartWidth = width - leftMargin - rightMargin;
        int chartHeight = height - topMargin - bottomMargin;

        if (chartWidth <= 0 || chartHeight <= 0) return;

        // Tìm giá trị max
        double maxVal = REVENUE_TARGET;
        for (double val : REVENUE_DATA) {
            if (val > maxVal) maxVal = val;
        }
        maxVal = Math.ceil(maxVal); // Làm tròn lên

        // Màu từ UIManager
        Color textColor = getThemePrimaryTextColor();
        Color gridColor = getThemeBorderColor();
        Color barColor = new Color(33, 150, 243); // #2196F3
        Color targetColor = new Color(255, 87, 34); // Cam đỏ cho mục tiêu

        // --- Vẽ grid lines ngang ---
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
                new float[]{4, 4}, 0));
        g2.setColor(gridColor);
        int gridLines = 5;
        for (int i = 0; i <= gridLines; i++) {
            int y = topMargin + (int) ((double) i / gridLines * chartHeight);
            g2.drawLine(leftMargin, y, leftMargin + chartWidth, y);

            // Nhãn trục Y (tiền tệ)
            double labelVal = maxVal * (gridLines - i) / gridLines;
            String yLabel = String.format("%.1fM", labelVal);
            g2.setFont(new Font(FONT_FAMILY, Font.PLAIN, 10));
            g2.setColor(textColor);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(yLabel, leftMargin - fm.stringWidth(yLabel) - 5, y + fm.getAscent() / 2);
            g2.setColor(gridColor);
        }

        // --- Vẽ trục X và Y ---
        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(textColor);
        g2.drawLine(leftMargin, topMargin, leftMargin, topMargin + chartHeight);        // Trục Y
        g2.drawLine(leftMargin, topMargin + chartHeight,
                leftMargin + chartWidth, topMargin + chartHeight);                      // Trục X

        // --- Vẽ đường mục tiêu ---
        int targetY = topMargin + (int) ((maxVal - REVENUE_TARGET) / maxVal * chartHeight);
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
                new float[]{8, 4}, 0));
        g2.setColor(targetColor);
        g2.drawLine(leftMargin, targetY, leftMargin + chartWidth, targetY);
        g2.setFont(new Font(FONT_FAMILY, Font.PLAIN, 10));
        g2.drawString("Mục tiêu: " + String.format("%.1fM", REVENUE_TARGET),
                leftMargin + chartWidth - 90, targetY - 4);

        // --- Vẽ các cột biểu đồ ---
        int barCount = REVENUE_DATA.length;
        double barTotalWidth = (double) chartWidth / barCount;
        int barPadding = (int) (barTotalWidth * 0.2);
        int barWidth = (int) (barTotalWidth - barPadding * 2);
        if (barWidth < 10) barWidth = 10;

        g2.setStroke(new BasicStroke(1));
        for (int i = 0; i < barCount; i++) {
            int barHeight = (int) (REVENUE_DATA[i] / maxVal * chartHeight);
            int x = leftMargin + (int) (i * barTotalWidth) + barPadding;
            int y = topMargin + chartHeight - barHeight;

            // Gradient cho cột
            GradientPaint gradient = new GradientPaint(
                    x, y, barColor,
                    x, y + barHeight, barColor.darker()
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(x, y, barWidth, barHeight, 4, 4);

            // Viền cột
            g2.setColor(barColor.darker());
            g2.drawRoundRect(x, y, barWidth, barHeight, 4, 4);

            // Nhãn dữ liệu trên mỗi cột
            g2.setColor(textColor);
            g2.setFont(new Font(FONT_FAMILY, Font.BOLD, 10));
            String dataLabel = String.format("%.1fM", REVENUE_DATA[i]);
            FontMetrics fm = g2.getFontMetrics();
            int labelX = x + (barWidth - fm.stringWidth(dataLabel)) / 2;
            int labelY = y - 5;
            if (labelY < topMargin) labelY = y + 15;
            g2.drawString(dataLabel, labelX, labelY);

            // Nhãn trục X (ngày)
            g2.setFont(new Font(FONT_FAMILY, Font.PLAIN, 11));
            fm = g2.getFontMetrics();
            int dayLabelX = x + (barWidth - fm.stringWidth(DAY_LABELS[i])) / 2;
            g2.drawString(DAY_LABELS[i], dayLabelX, topMargin + chartHeight + 18);
        }
    }

    /**
     * Tạo panel legend cho biểu đồ, hiển thị "Doanh thu" và "Mục tiêu".
     *
     * @return JPanel legend
     */
    private JPanel createChartLegend() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        legendPanel.setOpaque(false);

        // Legend item: Doanh thu
        JPanel revenueItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        revenueItem.setOpaque(false);
        JPanel blueBox = new JPanel();
        blueBox.setBackground(new Color(33, 150, 243));
        blueBox.setPreferredSize(new Dimension(14, 14));
        revenueItem.add(blueBox);
        JLabel lblRevenue = new JLabel("Doanh thu");
        lblRevenue.setFont(new Font(FONT_FAMILY, Font.PLAIN, 11));
        revenueItem.add(lblRevenue);
        legendPanel.add(revenueItem);

        // Legend item: Mục tiêu
        JPanel targetItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        targetItem.setOpaque(false);
        JPanel orangeBox = new JPanel();
        orangeBox.setBackground(new Color(255, 87, 34));
        orangeBox.setPreferredSize(new Dimension(14, 14));
        targetItem.add(orangeBox);
        JLabel lblTarget = new JLabel("Mục tiêu");
        lblTarget.setFont(new Font(FONT_FAMILY, Font.PLAIN, 11));
        targetItem.add(lblTarget);
        legendPanel.add(targetItem);

        return legendPanel;
    }

    // =========================================================================
    // THAO TÁC NHANH VỚI JBUTTON VÀ ACTION LISTENER
    // =========================================================================

    /**
     * Tạo panel "Thao tác nhanh" với các JButton thực thụ.
     * Mỗi nút gọi callback khi được nhấn (nếu đã set).
     *
     * @return JPanel thao tác nhanh
     */
    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createMatteBorder(1, 1, 1, 1, getThemeBorderColor()),
                        "\uD83C\uDFAF Thao tác nhanh",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font(FONT_FAMILY, Font.BOLD, 14)
                ),
                new EmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Định nghĩa các nút: [emoji + tên hiển thị, action key]
        String[][] actions = {
            {"\uD83D\uDED2 Tạo đơn hàng", "Tạo đơn hàng"},
            {"\uD83D\uDCE6 Thêm sản phẩm", "Thêm sản phẩm"},
            {"\uD83D\uDC64 Thêm khách hàng", "Thêm khách hàng"},
            {"\uD83D\uDCCA Xem thống kê", "Xem thống kê"}
        };

        for (int i = 0; i < actions.length; i++) {
            gbc.gridy = i;
            JButton btn = createQuickActionButton(actions[i][0], actions[i][1]);
            panel.add(btn, gbc);
        }

        // Glue để đẩy buttons lên trên
        gbc.gridy = actions.length;
        gbc.weighty = 1.0;
        panel.add(Box.createGlue(), gbc);

        return panel;
    }

    /**
     * Tạo JButton cho thao tác nhanh với ActionListener gọi callback.
     *
     * @param label     Text hiển thị trên nút (có emoji)
     * @param actionKey Key gửi qua callback
     * @return JButton đã cấu hình
     */
    private JButton createQuickActionButton(String label, String actionKey) {
        JButton btn = new JButton(label);
        btn.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        // ActionListener: gọi callback nếu đã được set
        btn.addActionListener(e -> {
            if (dashboardCallback != null) {
                dashboardCallback.onQuickActionClicked(actionKey);
            } else {
                // Fallback: Hiển thị thông báo nếu chưa kết nối callback
                JOptionPane.showMessageDialog(this,
                        "Chưa kết nối callback. Hành động: " + actionKey
                        + "\n\nHướng dẫn: Gọi setDashboardCallback() để kết nối.",
                        "Thao tác nhanh",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return btn;
    }

    // =========================================================================
    // HELPER METHODS - LẤY MÀU TỪ UIMANAGER
    // =========================================================================

    /**
     * Lấy màu viền từ UIManager, fallback sang xám nhạt.
     */
    private Color getThemeBorderColor() {
        Color c = UIManager.getColor("Component.borderColor");
        return c != null ? c : new Color(200, 200, 200);
    }

    /**
     * Lấy màu text chính từ UIManager, fallback sang đen.
     */
    private Color getThemePrimaryTextColor() {
        Color c = UIManager.getColor("Label.foreground");
        return c != null ? c : Color.BLACK;
    }

    /**
     * Lấy màu text phụ (xám) từ UIManager, fallback sang xám.
     */
    private Color getThemeSecondaryTextColor() {
        Color c = UIManager.getColor("Label.disabledForeground");
        return c != null ? c : Color.GRAY;
    }

    /**
     * Lấy màu hover từ UIManager, fallback sang xám rất nhạt.
     */
    private Color getThemeHoverColor() {
        Color c = UIManager.getColor("List.selectionBackground");
        if (c != null) {
            return new Color(c.getRed(), c.getGreen(), c.getBlue(), 40);
        }
        return new Color(0, 0, 0, 20);
    }
}
