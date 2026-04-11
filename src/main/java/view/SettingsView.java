/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import business.service.LoginService;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import model.account.Account;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * SettingsView - Màn hình Cài đặt cho Hệ thống Quản lý Siêu thị.
 */
public class SettingsView extends JPanel {

    // ===== Dữ liệu mặc định =====
    private String userName = "Nguyễn Văn A";
    private String userRole = "Quản trị viên";
    private String userEmail = "admin@supermarket.com";
    private String userDepartment = "Quản lý";
    private String joinDate = "01/01/2024";
    private String usernameId = "admin";

    // ===== Các thành phần giao diện =====
    private JPasswordField txtCurrentPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private JCheckBox chkShowPassword;
    
    private JComboBox<String> cboTheme;
    private JComboBox<String> cboFontSize;
    private JComboBox<String> cboLanguage;
    
    private JCheckBox chkSysNotification;
    private JCheckBox chkStockAlert;
    private JCheckBox chkTransactionAlert;
    private JTextField txtNotifyPhone;
    private JTextField txtNotifyEmail;
    
    private JCheckBox chkAutoSave;
    private JLabel lblAvatar;
    private JLabel lblName;
    private JLabel lblPosition;

    public SettingsView() {
        loadCurrentUser();
        initUI();
    }

    private void loadCurrentUser() {
        Account currentUser = LoginService.getCurrentUser();
        if (currentUser != null) {
            userName = currentUser.getUsername();
            usernameId = currentUser.getUsername();
            userRole = currentUser.getRole() != null ? currentUser.getRole() : "Người dùng";
            // Optional: Map other fields if Account model has them
        }
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        
        // Cột trái: Hồ sơ (35%)
        gbc.gridx = 0;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 10);
        add(createProfilePanel(), gbc);

        // Cột phải: Cài đặt hệ thống (65%)
        JPanel settingsPanel = createSettingsPanel();
        JScrollPane scrollPane = new JScrollPane(settingsPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 10, 0, 0);
        add(scrollPane, gbc);
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor"), 1),
                new EmptyBorder(30, 20, 30, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        // Avatar
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0);
        lblAvatar = new JLabel();
        lblAvatar.setIcon(createDefaultAvatarIcon(120));
        panel.add(lblAvatar, gbc);

        // Name
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 2, 0);
        lblName = new JLabel(userName);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panel.add(lblName, gbc);

        // Position
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        lblPosition = new JLabel(userRole);
        lblPosition.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblPosition.setForeground(UIManager.getColor("Label.disabledForeground"));
        panel.add(lblPosition, gbc);

        // Details
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridy = 3; gbc.insets = new Insets(6, 10, 6, 10);
        panel.add(createInfoLabel("\uD83D\uDC64", "Tên đăng nhập: " + usernameId), gbc);
        
        gbc.gridy = 4;
        panel.add(createInfoLabel("\uD83D\uDCE7", "Email: " + userEmail), gbc);

        gbc.gridy = 5;
        panel.add(createInfoLabel("\uD83C\uDFE2", "Bộ phận: " + userDepartment), gbc);

        gbc.gridy = 6;
        panel.add(createInfoLabel("\uD83D\uDCC5", "Ngày tham gia: " + joinDate), gbc);

        // Spacer
        gbc.gridy = 7;
        gbc.weighty = 1.0;
        panel.add(Box.createGlue(), gbc);

        // Edit Button
        gbc.gridy = 8;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 0, 10);
        JButton btnEditProfile = new JButton("Chỉnh sửa hồ sơ");
        btnEditProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditProfile.putClientProperty("JButton.buttonType", "roundRect");
        btnEditProfile.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEditProfile.setPreferredSize(new Dimension(200, 36));
        btnEditProfile.addActionListener(e -> onEditProfile());
        panel.add(btnEditProfile, gbc);

        return panel;
    }

    private JLabel createInfoLabel(String iconStr, String text) {
        JLabel label = new JLabel(iconStr + "  " + text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private ImageIcon createDefaultAvatarIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color avatarBg = UIManager.getColor("Component.accentColor");
        if (avatarBg == null) avatarBg = new Color(41, 128, 185);
        g2.setColor(avatarBg);
        g2.fill(new Ellipse2D.Double(0, 0, size, size));

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, size / 2));
        FontMetrics fm = g2.getFontMetrics();
        String initial = userName.isEmpty() ? "?" : userName.substring(0, 1).toUpperCase();
        int textX = (size - fm.stringWidth(initial)) / 2;
        int textY = (size - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(initial, textX, textY);

        g2.dispose();
        return new ImageIcon(image);
    }

    private void onEditProfile() {
        // Placeholder for edit profile
        JOptionPane.showMessageDialog(this,
                "Chức năng chỉnh sửa hồ sơ đang được phát triển.",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(0, 0, 0, 10));

        panel.add(createAppearanceCard());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        panel.add(createNotificationCard());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        panel.add(createSecurityCard());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        panel.add(createStorageCard());
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createCardPanel(String title) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        UIManager.getBorder("TitledBorder.border"),
                        title, TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 15)
                ),
                new EmptyBorder(15, 20, 15, 20)
        ));
        // Giới hạn chiều cao card để không bị kéo dãn quá đà
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        return card;
    }

    private JPanel createAppearanceCard() {
        JPanel card = createCardPanel("Giao diện & Hiển thị");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        card.add(new JLabel("Chủ đề:"), gbc);

        cboTheme = new JComboBox<>(new String[]{"Sáng (Light)", "Tối (Dark)"});
        cboTheme.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                switchTheme(((String) cboTheme.getSelectedItem()).contains("Tối"));
            }
        });
        gbc.gridx = 1; gbc.weightx = 0.8; gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(cboTheme, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2; gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("Ngôn ngữ:"), gbc);

        cboLanguage = new JComboBox<>(new String[]{"Tiếng Việt", "English"});
        gbc.gridx = 1; gbc.weightx = 0.8; gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(cboLanguage, gbc);

        return card;
    }

    private JPanel createNotificationCard() {
        JPanel card = createCardPanel("Thông báo & Liên hệ");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Các Checkbox
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        chkSysNotification = new JCheckBox("Bật thông báo trạng thái hệ thống");
        chkSysNotification.setSelected(true);
        card.add(chkSysNotification, gbc);

        gbc.gridy = 1; 
        chkStockAlert = new JCheckBox("Nhận cảnh báo khi tồn kho thấp");
        chkStockAlert.setSelected(true);
        card.add(chkStockAlert, gbc);

        gbc.gridy = 2; 
        chkTransactionAlert = new JCheckBox("Thông báo qua SMS/Email khi có giao dịch mới");
        card.add(chkTransactionAlert, gbc);
        
        // Đường phân cách
        gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 15);
        card.add(new JSeparator(), gbc);
        
        // Nhập thông tin liên hệ nhận thông báo
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 5, 5, 15);
        
        gbc.gridy = 4; gbc.gridx = 0; gbc.weightx = 0.3;
        card.add(new JLabel("Số điện thoại nhận SMS:"), gbc);
        txtNotifyPhone = new JTextField(currentUserPhone); // Hoặc load từ account
        gbc.gridx = 1; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtNotifyPhone, gbc);

        gbc.gridy = 5; gbc.gridx = 0; gbc.weightx = 0.3; gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("Email nhận thông báo:"), gbc);
        txtNotifyEmail = new JTextField(userEmail);
        gbc.gridx = 1; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtNotifyEmail, gbc);

        // Nút Cập nhật liên hệ
        JButton btnUpdateContact = new JButton("Lưu cấu hình thông báo");
        btnUpdateContact.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdateContact.putClientProperty("JButton.buttonType", "roundRect");
        btnUpdateContact.addActionListener(e -> onUpdateNotificationContact());
        
        gbc.gridy = 6; gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 5, 5, 15);
        card.add(btnUpdateContact, gbc);

        return card;
    }
    
    // Thêm biến currentUserPhone
    private String currentUserPhone = "0123456789";

    private void onUpdateNotificationContact() {
        String phone = txtNotifyPhone.getText().trim();
        String email = txtNotifyEmail.getText().trim();
        
        // Validate
        if (!chkSysNotification.isSelected() && !chkStockAlert.isSelected() && !chkTransactionAlert.isSelected()) {
            JOptionPane.showMessageDialog(this, "Đã tắt tất cả các thông báo.", "Thông tin", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Validate Phone (10-11 chữ số)
            if (!phone.isEmpty() && !phone.matches("\\d{10,11}")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại hợp lệ (10-11 chữ số).", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Validate Email đơn giản
            if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Email hợp lệ.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(this, "Đã cập nhật cấu hình và thông tin nhận thông báo thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JPanel createSecurityCard() {
        JPanel card = createCardPanel("Bảo mật (Đổi mật khẩu)");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        card.add(new JLabel("Mật khẩu hiện tại:"), gbc);
        txtCurrentPassword = new JPasswordField();
        gbc.gridx = 1; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtCurrentPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3; gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("Mật khẩu mới:"), gbc);
        txtNewPassword = new JPasswordField();
        gbc.gridx = 1; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtNewPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3; gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("Xác nhận mật khẩu:"), gbc);
        txtConfirmPassword = new JPasswordField();
        gbc.gridx = 1; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtConfirmPassword, gbc);
        
        // Hiện mật khẩu
        chkShowPassword = new JCheckBox("Hiển thị mật khẩu");
        chkShowPassword.addActionListener(e -> {
            char echo = chkShowPassword.isSelected() ? (char) 0 : '•';
            txtCurrentPassword.setEchoChar(echo);
            txtNewPassword.setEchoChar(echo);
            txtConfirmPassword.setEchoChar(echo);
        });
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.7; gbc.fill = GridBagConstraints.NONE;
        card.add(chkShowPassword, gbc);

        JButton btnUpdatePassword = new JButton("Đổi mật khẩu");
        btnUpdatePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdatePassword.putClientProperty("JButton.buttonType", "roundRect");
        btnUpdatePassword.setBackground(UIManager.getColor("Component.accentColor"));
        btnUpdatePassword.setForeground(Color.WHITE);
        btnUpdatePassword.addActionListener(e -> onUpdatePassword());
        
        gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        card.add(btnUpdatePassword, gbc);

        return card;
    }

    private void onUpdatePassword() {
        String currentPass = new String(txtCurrentPassword.getPassword());
        String newPass = new String(txtNewPassword.getPassword());
        String confirmPass = new String(txtConfirmPassword.getPassword());

        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các trường mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (newPass.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới phải có ít nhất 6 ký tự!", "Lỗi xác thực", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // TODO: Call Backend service to change password here
        JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        
        txtCurrentPassword.setText("");
        txtNewPassword.setText("");
        txtConfirmPassword.setText("");
        chkShowPassword.setSelected(false);
        txtCurrentPassword.setEchoChar('•');
        txtNewPassword.setEchoChar('•');
        txtConfirmPassword.setEchoChar('•');
    }

    private JPanel createStorageCard() {
        JPanel card = createCardPanel("Lưu trữ & Dữ liệu");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        chkAutoSave = new JCheckBox("Tự động lưu thay đổi cấu hình");
        chkAutoSave.setSelected(true);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(chkAutoSave, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        card.add(new JLabel("Dữ liệu hệ thống:"), gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton btnBackup = new JButton("Sao lưu");
        JButton btnRestore = new JButton("Khôi phục");
        btnBackup.putClientProperty("JButton.buttonType", "roundRect");
        btnRestore.putClientProperty("JButton.buttonType", "roundRect");
        btnPanel.add(btnBackup);
        btnPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        btnPanel.add(btnRestore);
        
        btnBackup.addActionListener(e -> JOptionPane.showMessageDialog(this, "Đang tiến hành sao lưu dữ liệu... (Placeholder)", "Thông báo", JOptionPane.INFORMATION_MESSAGE));
        btnRestore.addActionListener(e -> JOptionPane.showMessageDialog(this, "Mở cửa sổ khôi phục dữ liệu... (Placeholder)", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        gbc.gridx = 1; gbc.weightx = 0.7;
        card.add(btnPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        card.add(new JLabel("Phiên bản:"), gbc);
        JLabel lblVersion = new JLabel("v2.1.0 - Stable");
        lblVersion.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        gbc.gridx = 1; gbc.weightx = 0.7;
        card.add(lblVersion, gbc);

        return card;
    }

    private void switchTheme(boolean isDark) {
        try {
            if (isDark) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            Window topLevel = SwingUtilities.getWindowAncestor(this);
            if (topLevel != null) SwingUtilities.updateComponentTreeUI(topLevel);
            else SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }
}
