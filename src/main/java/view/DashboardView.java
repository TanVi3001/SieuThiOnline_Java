/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author Admin
 */
public class DashboardView extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(DashboardView.class.getName());

    private javax.swing.JPanel mainContentPanel;

    /**
     * Creates new form DashboardView
     */
    public DashboardView() {
        initComponents();

        this.setTitle("Hệ Thống Quản Lý Siêu Thị");
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        this.setMinimumSize(new java.awt.Dimension(1280, 720));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Lấy user hiện tại 1 lần
        model.account.Account currentUser = business.service.LoginService.getCurrentUser();
        String username = "";
        if (currentUser != null && currentUser.getUsername() != null) {
            username = currentUser.getUsername().trim();
            jLabel2.setText(String.format("HỆ THỐNG SIÊU THỊ - Chào, %s", username));
        } else {
            jLabel2.setText("HỆ THỐNG SIÊU THỊ - Chưa đăng nhập");
        }

        // ===== PHÂN QUYỀN THEO USERNAME =====
        boolean isStaff = "staff".equalsIgnoreCase(username);

        // Dựng layout mới
        this.getContentPane().removeAll();
        this.getContentPane().setLayout(new java.awt.BorderLayout());

        mainContentPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        mainContentPanel.setBackground(new java.awt.Color(245, 245, 247));

        // Truyền role cho Sidebar mới dựa trên username
        String roleForSidebar = isStaff ? "STAFF" : "ADMIN";
        view.components.Sidebar newSidebar = new view.components.Sidebar(roleForSidebar);

        // Menu click
        newSidebar.setMenuClickListener(title -> {
            switch (title) {
                case "Tổng quan":
                    showPanel(new TongQuanPanel());
                    break;

                case "Quản lý sản phẩm":
                    showPanel(new ProductView());
                    break;

                case "Quản lý nhân viên":
                    if (isStaff) {
                        javax.swing.JOptionPane.showMessageDialog(
                                this,
                                "Bạn không có quyền truy cập chức năng này!",
                                "Từ chối truy cập",
                                javax.swing.JOptionPane.WARNING_MESSAGE
                        );
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

                case "Thống kê":
                    showPanel(new StatisticView());
                    break;

                case "Cài đặt":
                    showPanel(new SettingsView());
                    break;

                case "Đăng xuất":
                    btnLogoutActionPerformed(null);
                    break;
            }
        });

        this.getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);
        this.getContentPane().add(newSidebar, java.awt.BorderLayout.WEST);
        this.getContentPane().add(mainContentPanel, java.awt.BorderLayout.CENTER);

        // Hiển thị TongQuanPanel làm màn hình mặc định
        showPanel(new TongQuanPanel());

        this.revalidate();
        this.repaint();
    }

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        // 1. Hiện bảng xác nhận cho "chắc cú"
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Bạn có thực sự muốn đăng xuất không?",
                "Xác nhận",
                javax.swing.JOptionPane.YES_NO_OPTION);

        // 2. Nếu Nhóm trưởng chọn YES
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            // Gọi hàm xóa session đã viết ở Bước 1
            business.service.LoginService.logout();

            // 3. Mở lại màn hình Login
            java.awt.EventQueue.invokeLater(() -> {
                view.LoginView login = new view.LoginView();
                login.setVisible(true);
                login.setLocationRelativeTo(null); // Vẫn phải ở giữa màn hình cho đẹp
            });

            // 4. Đóng cái Dashboard hiện tại lại
            this.dispose();
        }

    }// GEN-LAST:event_btnLogoutActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>

        // PHẢI ĐẶT ĐẦU TIÊN: Trị lỗi màn hình 2K/4K bị bé xíu
        System.setProperty("sun.java2d.uiScale", "1.5");

        /* Set the FlatLaf look and feel */
        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(DashboardView.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }

        /* Khởi tạo và hiển thị */
        java.awt.EventQueue.invokeLater(() -> {
            DashboardView dashboard = new DashboardView();
            dashboard.setVisible(true);
        });
    }

    public void showPanel(javax.swing.JPanel childPanel) {
        // [SỬA LỖI] Bây giờ đổi qua dùng mainContentPanel thay vì jPanel3 bị bug
        mainContentPanel.removeAll();

        // Ép Layout để panel con lấp đầy vùng xanh
        mainContentPanel.setLayout(new java.awt.BorderLayout());
        mainContentPanel.add(childPanel, java.awt.BorderLayout.CENTER);

        // Làm tươi giao diện
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
