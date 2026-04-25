/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.components;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * TongQuanPanel - Panel Tổng Quan hiển thị mặc định khi mở Dashboard.
 * Hiển thị các thống kê cơ bản và thông tin tổng quan hệ thống.
 *
 * @author Le Tan Vi
 */
public class TongQuanPanel extends JPanel {

    public TongQuanPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // === Tiêu đề ===
        JLabel lblTitle = new JLabel("Tổng Quan");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // === Nội dung chính ===
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weighty = 0;

        business.sql.sales_order.StatisticSql statSql = business.sql.sales_order.StatisticSql.getInstance();
        int totalProducts = statSql.getTotalProducts();
        int todayOrders = statSql.getTodayOrders();
        int totalCustomers = statSql.getTotalCustomers();
        double monthlyRevenue = statSql.getMonthlyRevenue();

        // --- Hàng 1: 4 Card thống kê ---
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        gbc.gridx = 0;
        contentPanel.add(createStatCard("Sản phẩm", String.format("%,d", totalProducts), "\uD83D\uDCE6"), gbc);

        gbc.gridx = 1;
        contentPanel.add(createStatCard("Đơn hàng hôm nay", String.format("%,d", todayOrders), "\uD83D\uDCC4"), gbc);

        gbc.gridx = 2;
        contentPanel.add(createStatCard("Khách hàng", String.format("%,d", totalCustomers), "\uD83D\uDC65"), gbc);

        gbc.gridx = 3;
        contentPanel.add(createStatCard("Doanh thu tháng", String.format("%,.0f ₫", monthlyRevenue), "\uD83D\uDCB0"), gbc);

        // --- Hàng 2: 2 panel phụ ---
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        contentPanel.add(createRecentOrdersPanel(), gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 2;
        contentPanel.add(createQuickActionsPanel(), gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Tạo Card thống kê với icon, tiêu đề và giá trị.
     */
    private JPanel createStatCard(String title, String value, String emoji) {
        JPanel card = new JPanel(new GridBagLayout());

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(resolveBorderColor(), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Emoji icon
        gbc.gridy = 0;
        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        card.add(lblEmoji, gbc);

        // Tiêu đề
        gbc.gridy = 1;
        gbc.insets = new Insets(8, 0, 2, 0);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setForeground(resolveSubTextColor());
        card.add(lblTitle, gbc);

        // Giá trị
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        card.add(lblValue, gbc);

        return card;
    }

    /**
     * Tạo panel "Đơn hàng gần đây" placeholder.
     */
    private JPanel createRecentOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        UIManager.getBorder("TitledBorder.border"),
                        "Đơn hàng gần đây",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14)
                ),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Lấy 5 đơn hàng gần đây từ CSDL
        String[] columns = {"Mã ĐH", "Khách hàng", "Tổng tiền", "Trạng thái"};
        java.util.List<java.util.Map<String, Object>> recentOrders = business.sql.sales_order.StatisticSql.getInstance().getRecentOrders(5);
        Object[][] data = new Object[recentOrders.size()][4];
        for (int i = 0; i < recentOrders.size(); i++) {
            java.util.Map<String, Object> order = recentOrders.get(i);
            data[i][0] = order.get("order_id");
            data[i][1] = order.get("customer_name");
            
            Double amount = (Double) order.get("total_amount");
            data[i][2] = String.format("%,.0f ₫", amount != null ? amount : 0.0);
            
            String status = (String) order.get("status");
            String displayStatus = status;
            if ("COMPLETED".equalsIgnoreCase(status)) displayStatus = "Hoàn thành";
            else if ("PROCESSING".equalsIgnoreCase(status)) displayStatus = "Đang xử lý";
            else if ("CANCELLED".equalsIgnoreCase(status)) displayStatus = "Đã hủy";
            
            data[i][3] = displayStatus;
        }

        JTable table = new JTable(data, columns);
        table.setEnabled(false);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Tạo panel "Thao tác nhanh" placeholder.
     */
    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        UIManager.getBorder("TitledBorder.border"),
                        "Thao tác nhanh",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14)
                ),
                new EmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] actions = {
            "\uD83D\uDED2  Tạo đơn hàng mới",
            "\uD83D\uDCE6  Thêm sản phẩm",
            "\uD83D\uDC64  Thêm khách hàng",
            "\uD83D\uDCCA  Xem thống kê"
        };

        for (int i = 0; i < actions.length; i++) {
            gbc.gridy = i;
            JButton btn = new JButton(actions[i]);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.putClientProperty("JButton.buttonType", "roundRect");
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            panel.add(btn, gbc);
        }

        // Glue để đẩy buttons lên trên
        gbc.gridy = actions.length;
        gbc.weighty = 1.0;
        panel.add(Box.createGlue(), gbc);

        return panel;
    }

    // ===== Helpers để không bị null màu khi đổi theme =====
    private java.awt.Color resolveBorderColor() {
        java.awt.Color c = UIManager.getColor("Component.borderColor");
        return c != null ? c : UIManager.getColor("Separator.foreground");
    }

    private java.awt.Color resolveSubTextColor() {
        java.awt.Color c = UIManager.getColor("Label.disabledForeground");
        return c != null ? c : UIManager.getColor("Label.foreground");
    }
}