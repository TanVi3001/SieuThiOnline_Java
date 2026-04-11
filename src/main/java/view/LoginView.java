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
        // Setup lại layout tổng thể để chứa cả HomePanel và Login
        this.getContentPane().removeAll(); // Xóa sạch để build lại layout mới
        this.getContentPane().setLayout(new java.awt.BorderLayout()); // Dùng BorderLayout cho dễ chia đôi
        this.getContentPane().add(HomePanel, java.awt.BorderLayout.WEST); // Đưa HomePanel về bên trái
    
        // Tạo một vùng chứa mới cho phần Login bên phải
        javax.swing.JPanel rightContainer = new javax.swing.JPanel(new java.awt.GridBagLayout());
        rightContainer.setBackground(new java.awt.Color(44, 62, 80)); // Màu nền xanh than 
    
        // 1. Dọn dẹp content pane
        /*this.getContentPane().removeAll();
        this.getContentPane().setLayout(new java.awt.GridBagLayout()); // Căn giữa Card
        this.getContentPane().setBackground(java.awt.Color.WHITE); // Đổi bề mặt thành Trắng tinh*/

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
        btnSignUp.addActionListener(e -> {
            // Khởi tạo trang đăng ký
            RegisterView regView = new RegisterView();
            regView.setVisible(true);
            
            // Đóng trang đăng nhập hiện tại
            this.dispose(); 
        });
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
        // Thêm sự kiện Click cho nhãn Quên mật khẩu
        lblForgot.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Mở form Quên mật khẩu
                String name = txtUsername.getText().trim(); // QUAN TRỌNG: Phải lấy text ở đây
                ForgotPasswordView forgotPass = new ForgotPasswordView(name); // Truyền nó vào đây
                forgotPass.setVisible(true);

                // Đóng form Đăng nhập hiện tại
                dispose();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Hiệu ứng đổi màu khi rê chuột vào cho giống link web
                lblForgot.setForeground(new java.awt.Color(255, 69, 0)); // Màu cam giống logo
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Trả lại màu cũ khi rê chuột ra
                lblForgot.setForeground(new java.awt.Color(44, 62, 80));
            }
        });

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

        // --- ĐOẠN THÊM 2: Gắn Card vào vùng bên phải ---
        rightContainer.add(cardPanel, new java.awt.GridBagConstraints());
        this.getContentPane().add(rightContainer, java.awt.BorderLayout.CENTER);
        // Chỉnh size cho cả 2 bên hiện cùng lúc 
        this.setSize(900, 550);
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
        java.awt.GridBagConstraints gridBagConstraints;

        LoginView = new javax.swing.JPanel();
        Login = new javax.swing.JLabel();
        Username = new javax.swing.JLabel();
        Password = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        HomePanel = new javax.swing.JPanel();
        SystemName = new javax.swing.JLabel();
        Separator = new javax.swing.JSeparator();
        Greeting = new javax.swing.JLabel();
        ClassName = new javax.swing.JLabel();
        IconMarket = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 102));

        LoginView.setBackground(new java.awt.Color(255, 255, 255));
        LoginView.setForeground(new java.awt.Color(255, 255, 255));
        LoginView.setPreferredSize(new java.awt.Dimension(450, 480));
        LoginView.setLayout(new java.awt.GridBagLayout());

        Login.setBackground(new java.awt.Color(0, 0, 0));
        Login.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        Login.setText("ĐĂNG NHẬP");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(46, 18, 0, 0);
        LoginView.add(Login, gridBagConstraints);

        Username.setBackground(new java.awt.Color(0, 0, 0));
        Username.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        Username.setText("TÀI KHOẢN");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(31, 36, 0, 0);
        LoginView.add(Username, gridBagConstraints);

        Password.setBackground(new java.awt.Color(0, 0, 0));
        Password.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        Password.setText("MẬT KHẨU");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 36, 0, 0);
        LoginView.add(Password, gridBagConstraints);

        txtUsername.addActionListener(this::txtUsernameActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 115;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(34, 18, 0, 42);
        LoginView.add(txtUsername, gridBagConstraints);

        btnLogin.setBackground(new java.awt.Color(44, 62, 80));
        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Đăng nhập");
        btnLogin.addActionListener(this::btnLoginActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(32, 37, 28, 0);
        LoginView.add(btnLogin, gridBagConstraints);

        txtPassword.addActionListener(this::txtPasswordActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 115;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 18, 0, 42);
        LoginView.add(txtPassword, gridBagConstraints);

        HomePanel.setBackground(new java.awt.Color(236, 240, 241));
        HomePanel.setLayout(new java.awt.GridBagLayout());

        SystemName.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        SystemName.setForeground(new java.awt.Color(44, 62, 80));
        SystemName.setText("HỆ THỐNG QUẢN LÝ SIÊU THỊ ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 62, 0, 62);
        HomePanel.add(SystemName, gridBagConstraints);

        Separator.setForeground(new java.awt.Color(0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 219;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 75, 0, 0);
        HomePanel.add(Separator, gridBagConstraints);

        Greeting.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Greeting.setForeground(new java.awt.Color(44, 62, 80));
        Greeting.setText("Chào mừng đến với hệ thống!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 103, 0, 0);
        HomePanel.add(Greeting, gridBagConstraints);

        ClassName.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        ClassName.setForeground(new java.awt.Color(44, 62, 80));
        ClassName.setText("Nhóm - IS216.Q22");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 138, 69, 0);
        HomePanel.add(ClassName, gridBagConstraints);

        IconMarket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view.image/market-Photoroom.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(33, 148, 0, 0);
        HomePanel.add(IconMarket, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(HomePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LoginView, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LoginView, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(HomePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLoginActionPerformed
        // 1. Lấy dữ liệu
        String user = txtUsername.getText().trim();

        // SỬA DÒNG NÀY: JPasswordField dùng getPassword() trả về mảng char,
        // mình phải bọc nó trong new String(...) để lấy chuỗi mật khẩu.
        String pass = new String(txtPassword.getPassword());

        System.out.println("User nhap: [" + user + "]");
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
    private javax.swing.JLabel ClassName;
    private javax.swing.JLabel Greeting;
    private javax.swing.JPanel HomePanel;
    private javax.swing.JLabel IconMarket;
    private javax.swing.JLabel Login;
    private javax.swing.JPanel LoginView;
    private javax.swing.JLabel Password;
    private javax.swing.JSeparator Separator;
    private javax.swing.JLabel SystemName;
    private javax.swing.JLabel Username;
    private javax.swing.JButton btnLogin;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}

// hi
