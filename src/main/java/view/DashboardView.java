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

        SystemName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Lấy user hiện tại 1 lần
        model.account.Account currentUser = business.service.LoginService.getCurrentUser();
        String username = "";
        if (currentUser != null && currentUser.getUsername() != null) {
            username = currentUser.getUsername().trim();
            SystemName.setText(String.format("HỆ THỐNG SIÊU THỊ - Chào, %s", username));
        } else {
            SystemName.setText("HỆ THỐNG SIÊU THỊ - Chưa đăng nhập");
        }

        // ===== PHÂN QUYỀN THEO USERNAME =====
        boolean isStaff = "staff".equalsIgnoreCase(username);

        // Sidebar cũ (NetBeans) không dùng nữa
        pnLeft.setVisible(false);

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
                    if (isStaff) {
                        javax.swing.JOptionPane.showMessageDialog(
                                this,
                                "Bạn không có quyền truy cập chức năng này!",
                                "Từ chối truy cập",
                                javax.swing.JOptionPane.WARNING_MESSAGE
                        );
                    } else {
                        showPanel(new StatisticView());
                    }
                    break;

                case "Cài đặt":
                    showPanel(new SettingsView());
                    break;

                case "Đăng xuất":
                    btnLogoutActionPerformed(null);
                    break;
            }
        });

        this.getContentPane().add(pnTop, java.awt.BorderLayout.NORTH);
        this.getContentPane().add(newSidebar, java.awt.BorderLayout.WEST);
        this.getContentPane().add(mainContentPanel, java.awt.BorderLayout.CENTER);

        // Hiển thị TongQuanPanel làm màn hình mặc định
        showPanel(new TongQuanPanel());

        this.revalidate();
        this.repaint();
    }

    /*
     * DashboardView() {
     * throw new UnsupportedOperationException("Not supported yet."); // Generated
     * from
     * nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
     * }
     */
    private void authorize() {
        try {
            // 1. Lấy thông tin người dùng
            model.account.Account user = business.service.LoginService.getCurrentUser();

            // Kiểm tra xem có lấy được User không
            if (user == null) {
                System.err.println("DEBUG: Không tìm thấy thông tin người dùng (User is null)");
                return;
            }

            // 2. Lấy Role và làm sạch chuỗi (Trim để xóa khoảng trắng dư thừa)
            String role = user.getRole();
            if (role == null) {
                System.err.println("DEBUG: User không có quyền (Role is null)");
                return;
            }
            role = role.trim(); // Xóa dấu cách nếu có (ví dụ "ADMIN " thành "ADMIN")

            // 3. Thực thi phân quyền
            // Kiểm tra: Nếu KHÔNG PHẢI là Admin thì ẩn nút
            if (!role.equalsIgnoreCase("ADMIN")) {

                // Ẩn nút Quản lý nhân viên
                if (btnEmployee != null) {
                    btnEmployee.setVisible(false);
                }

                // Ẩn nút Thống kê
                if (btnStatistic != null) {
                    btnStatistic.setVisible(false);
                }

                // In ra console để ông kiểm tra xem code có chạy vào đây không
                System.out.println("DEBUG: Đã nhận diện Staff. Đang ẩn các nút Admin...");
            } else {
                System.out.println("DEBUG: Đã nhận diện Admin. Giữ nguyên các nút.");
            }

            // 4. Ép giao diện vẽ lại (Quan trọng để các nút biến mất ngay lập tức)
            this.revalidate();
            this.repaint();

        } catch (Exception e) {
            System.err.println("Lỗi phân quyền: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnTop = new javax.swing.JPanel();
        SystemName = new javax.swing.JLabel();
        pnRight = new javax.swing.JPanel();
        pnLeft = new javax.swing.JPanel();
        btnProduct = new javax.swing.JButton();
        btnEmployee = new javax.swing.JButton();
        btnCustomer = new javax.swing.JButton();
        btnOrder = new javax.swing.JButton();
        btnStatistic = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnTop.setBackground(new java.awt.Color(44, 62, 80));

        SystemName.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        SystemName.setForeground(new java.awt.Color(255, 255, 255));
        SystemName.setText("HỆ THỐNG SIÊU THỊ");

        javax.swing.GroupLayout pnTopLayout = new javax.swing.GroupLayout(pnTop);
        pnTop.setLayout(pnTopLayout);
        pnTopLayout.setHorizontalGroup(
            pnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTopLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(SystemName)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnTopLayout.setVerticalGroup(
            pnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTopLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(SystemName)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pnRight.setBackground(new java.awt.Color(44, 62, 80));

        javax.swing.GroupLayout pnRightLayout = new javax.swing.GroupLayout(pnRight);
        pnRight.setLayout(pnRightLayout);
        pnRightLayout.setHorizontalGroup(
            pnRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 254, Short.MAX_VALUE)
        );
        pnRightLayout.setVerticalGroup(
            pnRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        btnProduct.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnProduct.setText("Quản lý sản phẩm");
        btnProduct.setBorderPainted(false);
        btnProduct.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnProduct.addActionListener(this::btnProductActionPerformed);

        btnEmployee.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnEmployee.setText("Quản lý nhân viên");
        btnEmployee.setBorderPainted(false);
        btnEmployee.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEmployee.addActionListener(this::btnEmployeeActionPerformed);

        btnCustomer.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnCustomer.setText("Khách hàng");
        btnCustomer.setBorderPainted(false);
        btnCustomer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCustomer.addActionListener(this::btnCustomerActionPerformed);

        btnOrder.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnOrder.setText("Hóa đơn");
        btnOrder.setBorderPainted(false);
        btnOrder.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOrder.addActionListener(this::btnOrderActionPerformed);

        btnStatistic.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnStatistic.setText("Thống kê");
        btnStatistic.setBorderPainted(false);
        btnStatistic.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnStatistic.addActionListener(this::btnStatisticActionPerformed);

        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnLogout.setText("Đăng xuất");
        btnLogout.setBorderPainted(false);
        btnLogout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLogout.addActionListener(this::btnLogoutActionPerformed);

        javax.swing.GroupLayout pnLeftLayout = new javax.swing.GroupLayout(pnLeft);
        pnLeft.setLayout(pnLeftLayout);
        pnLeftLayout.setHorizontalGroup(
            pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnLogout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnStatistic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnOrder, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
            .addComponent(btnProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnLeftLayout.setVerticalGroup(
            pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnStatistic, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLogout, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(pnRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProductActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnProductActionPerformed
        // TODO add your handling code here:
        showPanel(new ProductView()); // Gọi cái JPanel
    }// GEN-LAST:event_btnProductActionPerformed

    private void btnEmployeeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnEmployeeActionPerformed
        // TODO add your handling code here:
        showPanel(new view.EmployeeView());
    }// GEN-LAST:event_btnEmployeeActionPerformed

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

    private void btnStatisticActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnStatisticActionPerformed
        // TODO add your handling code here:
        showPanel(new StatisticView());
    }// GEN-LAST:event_btnStatisticActionPerformed

    private void btnOrderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnOrderActionPerformed
        // TODO add your handling code here:
        showPanel(new OrderView());
    }// GEN-LAST:event_btnOrderActionPerformed

    private void btnCustomerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCustomerActionPerformed
        // TODO add your handling code here:
        showPanel(new CustomerView());

    }// GEN-LAST:event_btnCustomerActionPerformed

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
    private javax.swing.JLabel SystemName;
    private javax.swing.JButton btnCustomer;
    private javax.swing.JButton btnEmployee;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnOrder;
    private javax.swing.JButton btnProduct;
    private javax.swing.JButton btnStatistic;
    private javax.swing.JPanel pnLeft;
    private javax.swing.JPanel pnRight;
    private javax.swing.JPanel pnTop;
    // End of variables declaration//GEN-END:variables
}
