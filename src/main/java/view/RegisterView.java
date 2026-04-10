/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author nguye
 */
public class RegisterView extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RegisterView.class.getName());

    /**
     * Creates new form RegisterView
     */
    public RegisterView() {
        initComponents();
        setupModernUI();
    }
    
    private void setupModernUI() {
        // 1. Chỉnh nền toàn bộ thành màu trắng để mất cái padding xanh than dư thừa
        this.getContentPane().removeAll();
        this.getContentPane().setLayout(new java.awt.GridBagLayout());
        this.getContentPane().setBackground(java.awt.Color.WHITE); 

        // 2. Tạo Card chứa form (Màu trắng)
        javax.swing.JPanel cardPanel = new javax.swing.JPanel(null);
        cardPanel.setBackground(java.awt.Color.WHITE);
        // Tăng chiều cao lên 650 để chứa thêm các trường Họ tên, SĐT
        cardPanel.setPreferredSize(new java.awt.Dimension(450, 650)); 

        // --- LOGO & APP NAME (Đồng bộ LoginView) ---
        javax.swing.JLabel lblLogoCircle = new javax.swing.JLabel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
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

        // Header "Đăng ký"
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("Đăng ký tài khoản");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));
        lblTitle.setForeground(new java.awt.Color(20, 30, 50));
        lblTitle.setBounds(100, 60, 300, 40);
        cardPanel.add(lblTitle);

        // --- DANH SÁCH CÁC TRƯỜNG THÔNG TIN MỚI ---
        String[] labels = {"HỌ VÀ TÊN", "EMAIL", "SỐ ĐIỆN THOẠI", "MẬT KHẨU", "XÁC NHẬN MẬT KHẨU"};
        String[] placeholders = {"Nguyễn Văn A", "example@gmail.com", "09xxxxxxx", "Nhập mật khẩu", "Nhập lại mật khẩu"};
        
        int startY = 120; // Vị trí bắt đầu
        int gap = 75;    // Khoảng cách giữa các ô
        
        for (int i = 0; i < labels.length; i++) {
            // Label tiêu đề nhỏ
            javax.swing.JLabel lbl = new javax.swing.JLabel(labels[i]);
            lbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
            lbl.setForeground(new java.awt.Color(44, 62, 80));
            lbl.setBounds(100, startY + (i * gap), 200, 20);
            cardPanel.add(lbl);

            // Ô nhập liệu (TextField hoặc PasswordField)
            javax.swing.JTextField txt;
            if (labels[i].contains("MẬT KHẨU")) {
                txt = new javax.swing.JPasswordField();
                ((javax.swing.JPasswordField)txt).putClientProperty("JPasswordField.showRevealButton", true);
            } else {
                txt = new javax.swing.JTextField();
            }
            
            txt.setBounds(100, startY + (i * gap) + 22, 250, 40);
            txt.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            txt.putClientProperty("JComponent.roundRect", true); // Bo tròn
            txt.putClientProperty("JTextField.placeholderText", placeholders[i]); // Chữ mờ
            txt.putClientProperty("JTextField.padding", new java.awt.Insets(0, 12, 0, 12));
            
            cardPanel.add(txt);
        }

        // --- NÚT ĐĂNG KÝ XANH THAN ---
        javax.swing.JButton btnReg = new javax.swing.JButton("ĐĂNG KÝ") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new java.awt.Color(44, 62, 80));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btnReg.setBounds(100, 520, 250, 45);
        btnReg.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btnReg.setForeground(java.awt.Color.WHITE);
        btnReg.setContentAreaFilled(false);
        btnReg.setBorderPainted(false);
        btnReg.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cardPanel.add(btnReg);

        // Nút Quay lại
        javax.swing.JLabel lblBack = new javax.swing.JLabel("Đã có tài khoản? Đăng nhập", javax.swing.SwingConstants.CENTER);
        lblBack.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        lblBack.setForeground(new java.awt.Color(44, 62, 80));
        lblBack.setBounds(125, 580, 200, 20);
        lblBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new LoginView().setVisible(true);
                dispose();
            }
            public void mouseEntered(java.awt.event.MouseEvent e) { lblBack.setText("<html><u>Đã có tài khoản? Đăng nhập</u></html>"); }
            public void mouseExited(java.awt.event.MouseEvent e) { lblBack.setText("Đã có tài khoản? Đăng nhập"); }
        });
        cardPanel.add(lblBack);

        // Gắn Card vào Center
        this.getContentPane().add(cardPanel, new java.awt.GridBagConstraints());
        
        // Chỉnh Size JFrame vừa đủ cao để không bị cắt form
        this.setSize(500, 720); 
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        java.awt.EventQueue.invokeLater(() -> new RegisterView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
