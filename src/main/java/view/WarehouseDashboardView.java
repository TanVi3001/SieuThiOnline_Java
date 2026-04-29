package view;

import javax.swing.*;
import java.awt.*;
import view.components.WarehouseSidebar;

public class WarehouseDashboardView extends JFrame {
    
    private JPanel mainContentPanel;
    private WarehouseSidebar warehouseSidebar;
    private final Color bgWarehouse = new Color(244, 246, 250); // Nền xám sáng hiện đại
    
    public WarehouseDashboardView() {
        setupWarehouseUI();
    }

    private void setupWarehouseUI() {
        // 1. Lấy thông tin user đăng nhập
        model.account.Account currentUser = business.service.LoginService.getCurrentUser();
        String username = (currentUser != null && currentUser.getUsername() != null) 
                          ? currentUser.getUsername() : "Warehouse_Staff";

        // 2. Thiết lập cửa sổ JFrame
        this.setTitle("SMART SUPERMARKET - WAREHOUSE PORTAL");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setMinimumSize(new Dimension(1024, 768));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());
        
        // 3. Khởi tạo Sidebar Kho
        warehouseSidebar = new WarehouseSidebar();
        warehouseSidebar.setMenuClickListener(title -> {
            switch (title) {
                case "Tổng quan kho":
                    // showPanel(new view.InventoryOverviewPanel());
                    break;
                case "Quản lý tồn kho":
                    // showPanel(new view.InventoryManagementPanel());
                    break;
                case "Quản lý sản phẩm":
                    showPanel(new view.ProductView()); // Gắn thẳng màn hình ProductView vào đây
                    break;
                case "Nhà cung cấp":
                    // showPanel(new view.SupplierView());
                    break;
                case "Danh mục & Thuế VAT":
                    // showPanel(new view.CategoryTaxView());
                    break;
                case "Cài đặt":
                    // showPanel(new view.SettingsView());
                    break;
                case "Đăng xuất":
                    handleLogout();
                    break;
            }
        });

        // 4. TẠO THANH HEADER SIÊU HIỆN ĐẠI (Thay thế cục màu xanh đen cũ)
        JPanel topHeader = createModernHeader(username);

        // 5. Khởi tạo Panel chứa nội dung chính
        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(bgWarehouse);

        // 6. Ráp mọi thứ vào Layout
        this.getContentPane().add(topHeader, BorderLayout.NORTH); // Header nằm trên cùng
        this.getContentPane().add(warehouseSidebar, BorderLayout.WEST); // Sidebar bên trái
        this.getContentPane().add(mainContentPanel, BorderLayout.CENTER); // Nội dung ở giữa
        
        // Mặc định hiển thị màn hình Sản phẩm khi vừa vào
        showPanel(new view.ProductView());
    }

    // ========================================================
    // HÀM VẼ THANH HEADER (TOP BAR) CHUẨN MODERN UI
    // ========================================================
    private JPanel createModernHeader(String username) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 75)); // Chiều cao thanh header
        // Kẻ một đường line xám mỏng ở dưới cùng để phân cách
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        // --- BÊN TRÁI: TIÊU ĐỀ ---
        JLabel lblTitle = new JLabel("Hệ Thống Quản Lý Kho (Warehouse Portal)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(43, 54, 116));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        header.add(lblTitle, BorderLayout.WEST);

        // --- BÊN PHẢI: THÔNG TIN USER & AVATAR ---
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        userPanel.setOpaque(false);
        
        // Lời chào
        JLabel lblGreeting = new JLabel("Xin chào, " + username);
        lblGreeting.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblGreeting.setForeground(new Color(112, 126, 174));

        // Cục Avatar tròn chứa chữ cái đầu của tên
        String firstLetter = username.isEmpty() ? "W" : username.substring(0, 1).toUpperCase();
        JLabel lblAvatar = new JLabel(firstLetter, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 201, 135)); // Màu xanh ngọc (hợp với Sidebar Kho)
                g2.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
                g2.dispose();
            }
        };
        lblAvatar.setPreferredSize(new Dimension(42, 42));
        lblAvatar.setForeground(Color.WHITE);
        lblAvatar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblAvatar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        userPanel.add(lblGreeting);
        userPanel.add(lblAvatar);
        userPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Đẩy vào một chút cho khỏi dính lề phải

        header.add(userPanel, BorderLayout.EAST);
        return header;
    }

    // ========================================================
    // CÁC HÀM XỬ LÝ SỰ KIỆN
    // ========================================================
    public void showPanel(JPanel panel) {
        mainContentPanel.removeAll();
        mainContentPanel.add(panel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có muốn đăng xuất khỏi Cổng Kho Hàng?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            business.service.LoginService.logout();
            new LoginView().setVisible(true);
            this.dispose();
        }
    }

    public static void main(String args[]) {
        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(() -> new WarehouseDashboardView().setVisible(true));
    }
}