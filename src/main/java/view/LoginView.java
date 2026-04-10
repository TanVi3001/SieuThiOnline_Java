/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author Admin
 */
public class LoginView extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(LoginView.class.getName());

    /**
     * Creates new form LoginView
     */
    public LoginView() {
        initComponents();
        this.setLocationRelativeTo(null);
        // Bắt sự kiện bàn phím cho ô Mật khẩu (Manual KeyListener)
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    btnLoginActionPerformed(null);
                }
            }
        });

        setupModernUI();
    }

    private void setupModernUI() {
        // 1. Dọn dẹp content pane
        this.getContentPane().removeAll();
        this.getContentPane().setLayout(new java.awt.GridBagLayout()); // Căn giữa Card
        this.getContentPane().setBackground(java.awt.Color.WHITE); // Đổi bề mặt thành Trắng tinh

        // 2. Tạo Card chứa form
        javax.swing.JPanel cardPanel = new javax.swing.JPanel(null);
        cardPanel.setBackground(java.awt.Color.WHITE);
        cardPanel.setPreferredSize(new java.awt.Dimension(450, 480)); // Tăng chiều dọc card lên xíu để chứa form cao
        // hơn

        // Logo góc trên
        javax.swing.JLabel lblLogoCircle = new javax.swing.JLabel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new java.awt.BasicStroke(3));
                g2.setColor(new java.awt.Color(255, 69, 0));
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

        // Sign up Button góc trên (Viền xanh than)
        javax.swing.JButton btnSignUp = new javax.swing.JButton("Đăng ký") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new java.awt.Color(44, 62, 80));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btnSignUp.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        btnSignUp.setForeground(new java.awt.Color(44, 62, 80));
        btnSignUp.setBounds(340, 15, 80, 30);
        btnSignUp.setContentAreaFilled(false);
        btnSignUp.setBorderPainted(false);
        btnSignUp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cardPanel.add(btnSignUp);

        // Header "Sign In"
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("Đăng nhập");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));
        lblTitle.setForeground(new java.awt.Color(20, 30, 50));
        lblTitle.setBounds(100, 80, 250, 40);
        cardPanel.add(lblTitle);

        // Chuyển txtUsername từ cũ sang (Thu hẹp rộng, tăng cao)
        txtUsername.setBounds(100, 140, 250, 50);
        txtUsername.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        txtUsername.putClientProperty("JComponent.roundRect", true);
        txtUsername.putClientProperty("JTextField.placeholderText", "Email or Username");
        txtUsername.putClientProperty("JTextField.padding", new java.awt.Insets(0, 15, 0, 15));
        cardPanel.add(txtUsername);

        // Chuyển txtPassword từ cũ sang (Thu hẹp rộng, tăng cao)
        txtPassword.setBounds(100, 210, 250, 50);
        txtPassword.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        txtPassword.putClientProperty("JComponent.roundRect", true);
        txtPassword.putClientProperty("JTextField.placeholderText", "Password");
        txtPassword.putClientProperty("JPasswordField.showRevealButton", true);
        txtPassword.putClientProperty("JTextField.padding", new java.awt.Insets(0, 15, 0, 15));
        cardPanel.add(txtPassword);

        // Forgot password
        javax.swing.JLabel lblForgot = new javax.swing.JLabel("Quên mật khẩu?");
        lblForgot.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        lblForgot.setForeground(new java.awt.Color(44, 62, 80));
        lblForgot.setBounds(100, 275, 200, 20);
        lblForgot.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cardPanel.add(lblForgot);

        // Nút Đăng Nhập Xanh Than (Thu hẹp rộng, tăng cao)
        javax.swing.JButton btnGradientLogin = new javax.swing.JButton("Đăng nhập") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new java.awt.Color(44, 62, 80));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btnGradientLogin.setBounds(100, 315, 250, 50);
        btnGradientLogin.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        btnGradientLogin.setForeground(java.awt.Color.WHITE);
        btnGradientLogin.setContentAreaFilled(false);
        btnGradientLogin.setBorderPainted(false);
        btnGradientLogin.setFocusPainted(false);
        btnGradientLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Link chức năng từ btnLogin cũ sang nút mới
        btnGradientLogin.addActionListener(evt -> btnLoginActionPerformed(null));
        cardPanel.add(btnGradientLogin);

        this.getContentPane().add(cardPanel, new java.awt.GridBagConstraints());
        this.setSize(500, 600); // Thu hẹp rộng, tăng chiều cao cho form ngoài cùng theo tỉ lệ!
        this.setLocationRelativeTo(null);
        this.revalidate();
        this.repaint();
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

        LoginView = new javax.swing.JPanel();
        Login = new javax.swing.JLabel();
        Username = new javax.swing.JLabel();
        Password = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 102));

        LoginView.setBackground(new java.awt.Color(44, 62, 80));
        LoginView.setForeground(new java.awt.Color(255, 255, 255));

        Login.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        Login.setForeground(new java.awt.Color(255, 255, 255));
        Login.setText("ĐĂNG NHẬP");

        Username.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        Username.setForeground(new java.awt.Color(255, 255, 255));
        Username.setText("TÀI KHOẢN");

        Password.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        Password.setForeground(new java.awt.Color(255, 255, 255));
        Password.setText("MẬT KHẨU");

        txtUsername.addActionListener(this::txtUsernameActionPerformed);

        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLogin.setText("Đăng nhập");
        btnLogin.addActionListener(this::btnLoginActionPerformed);

        txtPassword.addActionListener(this::txtPasswordActionPerformed);

        javax.swing.GroupLayout LoginViewLayout = new javax.swing.GroupLayout(LoginView);
        LoginView.setLayout(LoginViewLayout);
        LoginViewLayout.setHorizontalGroup(
            LoginViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LoginViewLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(LoginViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Username, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Password, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addGroup(LoginViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUsername)
                    .addComponent(txtPassword))
                .addGap(42, 42, 42))
            .addGroup(LoginViewLayout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addGroup(LoginViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Login)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LoginViewLayout.createSequentialGroup()
                        .addComponent(btnLogin)
                        .addGap(2, 2, 2)))
                .addGap(18, 146, Short.MAX_VALUE))
        );
        LoginViewLayout.setVerticalGroup(
            LoginViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginViewLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(Login)
                .addGap(44, 44, 44)
                .addGroup(LoginViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(LoginViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(btnLogin)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LoginView, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LoginView, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLoginActionPerformed
        // 1. Lấy dữ liệu
        String user = txtUsername.getText().trim();

        // SỬA DÒNG NÀY: JPasswordField dùng getPassword() trả về mảng char,
        // mình phải bọc nó trong new String(...) để lấy chuỗi mật khẩu.
        String pass = new String(txtPassword.getPassword()).trim();

        System.out.println("User nhap: [" + user + "]");
        System.out.println("Pass nhap: [" + pass + "]");
        // 2. Kiểm tra nhanh (Validation)
        if (user.isEmpty() || pass.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ Tài khoản và Mật khẩu!",
                    "Nhắc nhở",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 3. Gọi Service để kiểm tra với Database Oracle
            if (business.service.LoginService.authenticate(user, pass)) {

                // --- ĐĂNG NHẬP THÀNH CÔNG ---
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Chào mừng " + user + "! Bạn đã đăng nhập thành công.",
                        "Thông báo",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);

                // 4. Mở Dashboard
                java.awt.EventQueue.invokeLater(() -> {
                    DashboardView mainScreen = new DashboardView();
                    mainScreen.setVisible(true);
                    mainScreen.setLocationRelativeTo(null);
                });

                // 5. Đóng màn hình Login (Giải phóng RAM cho LOQ)
                this.dispose();

            } else {
                // --- ĐĂNG NHẬP THẤT BẠI ---
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Tài khoản hoặc mật khẩu không chính xác!",
                        "Lỗi đăng nhập",
                        javax.swing.JOptionPane.ERROR_MESSAGE);

                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Hệ thống đang gặp sự cố kết nối: " + e.getMessage(),
                    "LỖI NGHIÊM TRỌNG",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }// GEN-LAST:event_btnLoginActionPerformed

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtUsernameActionPerformed
        txtPassword.requestFocus(); // Nhấn Enter ở đây thì nhảy xuống ô dưới
        btnLoginActionPerformed(evt); // Gọi trực tiếp hàm xử lý của nút Đăng nhập
    }// GEN-LAST:event_txtUsernameActionPerformed

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtPasswordActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setLocationRelativeTo(null);
            login.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Login;
    private javax.swing.JPanel LoginView;
    private javax.swing.JLabel Password;
    private javax.swing.JLabel Username;
    private javax.swing.JButton btnLogin;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}

// hi
