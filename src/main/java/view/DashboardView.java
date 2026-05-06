package view;

import common.events.AppDataChangedEvent;
import common.events.EventBus;
import view.components.TongQuanPanel;
import view.components.Sidebar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class DashboardView extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DashboardView.class.getName());

    private Timer sessionTimer;
    private boolean isLoggingOut = false;
    private JPanel mainContentPanel;

    private String currentMenu = "Tổng quan";

    public DashboardView() {
        setupUI();
        startSessionCheck();

        common.security.SecurityGuard.attach(mainContentPanel);

        EventBus.subscribe(AppDataChangedEvent.class, e -> {
            SwingUtilities.invokeLater(() -> {
                if ("Tổng quan".equals(currentMenu)) {
                    showPanel(new TongQuanPanel());
                } else if ("Báo cáo & Thống kê".equals(currentMenu)) {
                    if (business.service.AuthorizationService.canAccessStatisticsAndEmployees()) {
                        showPanel(new StatisticView());
                    }
                }
            });
        });
    }

    private void setupUI() {
        model.account.Account u = business.service.LoginService.getCurrentUser();
        String tk = business.service.LoginService.getToken();
        String username = "";

        if (u != null) {
            username = u.getUsername().trim();
            this.setTitle("SMART SUPERMARKET - STORE PORTAL | Chào, " + username);
        } else {
            this.setTitle("SMART SUPERMARKET - STORE PORTAL");
        }

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setMinimumSize(new Dimension(1024, 768));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());

        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(new java.awt.Color(245, 245, 247));

        String roleForSidebar = common.auth.UserSession.getInstance().getUserRole();
        boolean isStaff = "R_STAFF_SALE".equals(roleForSidebar);

        Sidebar newSidebar = new Sidebar(roleForSidebar);
        newSidebar.setMenuClickListener(title -> {
            currentMenu = title;

            switch (title) {
                case "Tổng quan":
                    showPanel(new TongQuanPanel());
                    break;
                case "Quản lý sản phẩm":
                    if (isStaff) {
                        JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập!", "Từ chối", JOptionPane.WARNING_MESSAGE);
                    } else {
                        showPanel(new ProductView());
                    }
                    break;
                case "Quản lý nhân viên":
                    if (isStaff) {
                        JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập!", "Từ chối", JOptionPane.WARNING_MESSAGE);
                    } else {
                        showPanel(new view.EmployeeView());
                    }
                    break;
                case "Khách hàng":
                    showPanel(new CustomerView());
                    break;
                case "Hóa đơn":
                    showPanel(new OrderView());
                    break;
                case "Báo cáo & Thống kê":
                    if (isStaff) {
                        JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập!", "Từ chối", JOptionPane.WARNING_MESSAGE);
                    } else {
                        showPanel(new StatisticView());
                    }
                    break;
                case "Cài đặt":
                    showPanel(new view.SettingsView());
                    break;
                case "Đăng xuất":
                    handleLogout();
                    break;
            }
        });

        this.getContentPane().add(newSidebar, BorderLayout.WEST);
        this.getContentPane().add(mainContentPanel, BorderLayout.CENTER);

        showPanel(new TongQuanPanel());
    }

    public void showPanel(JPanel childPanel) {
        mainContentPanel.removeAll();
        mainContentPanel.add(childPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có thực sự muốn đăng xuất không?", "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            this.isLoggingOut = true;
            if (sessionTimer != null && sessionTimer.isRunning()) {
                sessionTimer.stop();
            }

            String tk = business.service.LoginService.getToken();
            business.sql.rbac.TokenSql.getInstance().revokeToken(tk);
            business.service.LoginService.logout();

            // TUI ĐÃ THÊM DÒNG NÀY ĐỂ XÓA SẠCH GỐC RỄ PHÂN QUYỀN TRONG CACHE SESSION
            common.auth.UserSession.getInstance().clear();

            java.awt.EventQueue.invokeLater(() -> {
                view.LoginView login = new view.LoginView();
                login.setVisible(true);
                login.setLocationRelativeTo(null);
            });

            this.dispose();
        }
    }

    private void startSessionCheck() {
        sessionTimer = new Timer(1000, e -> {
            if (isLoggingOut) {
                ((Timer) e.getSource()).stop();
                return;
            }

            String currentToken = business.service.LoginService.getToken();
            boolean isValid = business.sql.rbac.TokenSql.getInstance().isTokenValid(currentToken);

            if (!isValid) {
                if (!isLoggingOut) {
                    ((Timer) e.getSource()).stop();
                    JOptionPane.showMessageDialog(this, "Phiên đăng nhập của bạn đã hết hạn hoặc quyền truy cập đã thay đổi!", "Thông báo bảo mật", JOptionPane.ERROR_MESSAGE);

                    // TUI ĐÃ THÊM DÒNG NÀY ĐỂ XÓA SẠCH GỐC RỄ PHÂN QUYỀN TRONG CACHE SESSION
                    common.auth.UserSession.getInstance().clear();
                    business.service.LoginService.logout();

                    java.awt.EventQueue.invokeLater(() -> {
                        view.LoginView login = new view.LoginView();
                        login.setVisible(true);
                        login.setLocationRelativeTo(null);
                    });

                    this.dispose();
                }
            }
        });
        sessionTimer.start();
    }

    public static void main(String args[]) {
        System.setProperty("sun.java2d.uiScale", "1.5");
        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> {
            new DashboardView().setVisible(true);
        });
    }
}
