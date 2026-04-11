/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import com.mycompany.sieuthionline.common.utils.EmailUtils;
import javax.swing.JOptionPane;
/**
 *
 * @author nguye
 */
public class ForgotPasswordView extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ForgotPasswordView.class.getName());
    /**
     * Creates new form ForgotPasswordView
     */
    
    private String usernameFromLogin; // Biến lưu tên đăng nhập từ LoginView truyền sang
    
    public ForgotPasswordView(String username) {
        this.usernameFromLogin = username;
        initComponents();
        setupModernUI();
    }
    
    public ForgotPasswordView() {
        this(""); // Gọi lại constructor ở trên với chuỗi rỗng
    }
  
    private void setupModernUI() {
        // 1. Dọn dẹp content pane - Đặt nền xanh than bao quanh Card cho đồng bộ LoginView
        this.getContentPane().removeAll();
        this.getContentPane().setLayout(new java.awt.GridBagLayout()); 
        this.getContentPane().setBackground(java.awt.Color.WHITE); 

        // 2. Tạo Card chứa form (Trắng tinh khôi, bo góc được xử lý qua layout)
        javax.swing.JPanel cardPanel = new javax.swing.JPanel(null);
        cardPanel.setBackground(java.awt.Color.WHITE); 
        cardPanel.setPreferredSize(new java.awt.Dimension(450, 480)); 

        // --- SAO CHÉP LOGO & APP NAME TỪ LOGINVIEW ---
        javax.swing.JLabel lblLogoCircle = new javax.swing.JLabel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new java.awt.BasicStroke(3));
                g2.setColor(new java.awt.Color(255, 69, 0)); // Màu cam đặc trưng
                g2.drawOval(2, 2, 16, 16);
                g2.dispose();
            }
        };
        lblLogoCircle.setBounds(30, 20, 20, 20);
        cardPanel.add(lblLogoCircle);

        javax.swing.JLabel lblAppName = new javax.swing.JLabel("Smart Supermarket");
        lblAppName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        lblAppName.setBounds(55, 17, 180, 26);
        cardPanel.add(lblAppName);

        // Header "NHẬP EMAIL" - Màu Xanh Navy đậm
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("Quên mật khẩu");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));
        lblTitle.setForeground(new java.awt.Color(20, 30, 50));
        lblTitle.setBounds(100, 80, 250, 40);
        cardPanel.add(lblTitle);

        // Chú thích nhỏ
        javax.swing.JLabel lblSubtitle = new javax.swing.JLabel("<html>Vui lòng nhập email để chúng tôi có thể<br>gửi thông tin cho bạn</html>");
        lblSubtitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        lblSubtitle.setForeground(new java.awt.Color(44, 62, 80));
        lblSubtitle.setBounds(100, 125, 300, 40);
        cardPanel.add(lblSubtitle);

        // --- THIẾT KẾ Ô NHẬP EMAIL (ĐỒNG BỘ ĐỘ BO VÀ VIỀN MỜ) ---
        txtUserEmail.setBounds(100, 180, 250, 50); // Chiều cao 50px giống Login
        txtUserEmail.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

        // Thuộc tính quan trọng tạo nên độ bo và placeholder mờ
        txtUserEmail.putClientProperty("JComponent.roundRect", true); 
        txtUserEmail.putClientProperty("JTextField.placeholderText", "Nhập Email của bạn...");
        txtUserEmail.putClientProperty("JTextField.padding", new java.awt.Insets(0, 15, 0, 15));

        // Đặt viền mỏng nhẹ cho đồng bộ
        txtUserEmail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200), 1));
        cardPanel.add(txtUserEmail);

        // --- NÚT XÁC NHẬN (BO TRÒN XANH THAN) ---
        javax.swing.JButton btnConfirm = new javax.swing.JButton("Xác nhận") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new java.awt.Color(44, 62, 80)); // Màu xanh than
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btnConfirm.setBounds(100, 250, 250, 50);
        btnConfirm.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        btnConfirm.setForeground(java.awt.Color.WHITE);
        btnConfirm.setContentAreaFilled(false);
        btnConfirm.setBorderPainted(false);
        btnConfirm.setFocusPainted(false);
        btnConfirm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfirm.addActionListener(evt -> handleSendEmail());
        cardPanel.add(btnConfirm);

        // Quay lại đăng nhập
        javax.swing.JLabel lblBack = new javax.swing.JLabel("Quay lại đăng nhập");
        lblBack.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        lblBack.setForeground(new java.awt.Color(44, 62, 80));
        lblBack.setBounds(100, 310, 200, 20);
        lblBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new LoginView().setVisible(true);
                dispose();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblBack.setForeground(new java.awt.Color(255, 69, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblBack.setForeground(new java.awt.Color(44, 62, 80));
            }
        });
        cardPanel.add(lblBack);

        this.getContentPane().add(cardPanel, new java.awt.GridBagConstraints());
        this.setSize(500, 600); 
        this.setLocationRelativeTo(null);
        this.revalidate();
        this.repaint();
    }

    /**
     * Logic xử lý gửi mail
     */
    private void handleSendEmail() {
        String systemEmail = "nguyentung28012006@gmail.com"; 
        String appPassword = "zulx asyc wosl hagf"; // Mã 16 ký tự của Tùng
        String userEmail = txtUserEmail.getText().trim();

        if (userEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Email để nhận mật khẩu!");
            return;
        }

        try {
            // TRUY VẤN: Lấy mật khẩu của USERNAME đang chọn và có EMAIL tương ứng
            // Tùng cần sửa hàm findPasswordByEmail trong AccountSql để nhận thêm tham số username
            String passwordFromDB = business.sql.rbac.AccountSql.getInstance()
                    .findPassByUsernameAndEmail(usernameFromLogin, userEmail);

            if (passwordFromDB != null) {
                EmailUtils.sendEmail(systemEmail, appPassword, userEmail, 
                        "Khoi phuc mat khau cho tai khoan: " + usernameFromLogin, 
                        "Chao ban, mat khau cua tai khoan '" + usernameFromLogin + "' la: " + passwordFromDB);

                JOptionPane.showMessageDialog(this, "Mat khau da duoc gui den Email cua ban!");
            } else {
                JOptionPane.showMessageDialog(this, "Email khong khop voi tai khoan '" + usernameFromLogin + "'!", 
                        "Loi xac thuc", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Loi: " + ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LoginView = new javax.swing.JPanel();
        Login = new javax.swing.JLabel();
        Username = new javax.swing.JLabel();
        txtUserEmail = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        LoginView.setBackground(new java.awt.Color(44, 62, 80));
        LoginView.setForeground(new java.awt.Color(255, 255, 255));

        Login.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        Login.setForeground(new java.awt.Color(255, 255, 255));
        Login.setText("NHẬP EMAIL");

        Username.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        Username.setForeground(new java.awt.Color(255, 255, 255));
        Username.setText("EMAIL");

        txtUserEmail.addActionListener(this::txtUserEmailActionPerformed);

        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLogin.setText("XÁC NHẬN");
        btnLogin.addActionListener(this::btnLoginActionPerformed);

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Vui lòng nhập email để chúng tôi có thể gửi thông tin cho bạn");

        javax.swing.GroupLayout LoginViewLayout = new javax.swing.GroupLayout(LoginView);
        LoginView.setLayout(LoginViewLayout);
        LoginViewLayout.setHorizontalGroup(
            LoginViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginViewLayout.createSequentialGroup()
                .addGroup(LoginViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LoginViewLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LoginViewLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(Username)
                        .addGap(18, 18, 18)
                        .addComponent(txtUserEmail)))
                .addContainerGap(49, Short.MAX_VALUE))
            .addGroup(LoginViewLayout.createSequentialGroup()
                .addGroup(LoginViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LoginViewLayout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addComponent(btnLogin))
                    .addGroup(LoginViewLayout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(Login)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        LoginViewLayout.setVerticalGroup(
            LoginViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginViewLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(Login)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(LoginViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUserEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(btnLogin)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LoginView, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LoginView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUserEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserEmailActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        handleSendEmail();
    }//GEN-LAST:event_btnLoginActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new ForgotPasswordView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Login;
    private javax.swing.JPanel LoginView;
    private javax.swing.JLabel Username;
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtUserEmail;
    // End of variables declaration//GEN-END:variables
}
