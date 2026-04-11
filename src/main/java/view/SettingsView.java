/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * SettingsView - Màn hình Cài đặt cho Hệ thống Quản lý Siêu thị.
 * Giao diện 2 cột tỉ lệ 4:6 với GridBagLayout.
 * Cột trái: Hồ sơ cá nhân. Cột phải: 3 Card cài đặt hệ thống.
 * Sử dụng FlatLaf theme colors để tương thích Light/Dark mode.
 *
 * @author Le Tan Vi
 */
public class SettingsView extends JPanel {

    // ===== Dữ liệu mặc định =====
    private static final String DEFAULT_NAME = "Nguyễn Văn A";
    private static final String DEFAULT_POSITION = "Quản trị viên";
    private static final String DEFAULT_PHONE = "0123456789";
    private static final String DEFAULT_EMAIL = "admin@supermarket.com";
    private static final String DEFAULT_DEPARTMENT = "Quản lý";
    private static final String DEFAULT_JOIN_DATE = "01/01/2024";

    // ===== Các thành phần giao diện =====
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JPasswordField txtCurrentPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private JComboBox<String> cboTheme;
    private JComboBox<String> cboFontSize;
    private JCheckBox chkNotification;
    private JCheckBox chkAutoSave;
    private JLabel lblAvatar;
    private JLabel lblName;
    private JLabel lblPosition;

    /**
     * Constructor - Khởi tạo giao diện SettingsView.
     */
    public SettingsView() {
        initUI();
    }

    /**
     * Khởi tạo toàn bộ giao diện chính với GridBagLayout chia 2 cột tỉ lệ 4:6.
     */
    private void initUI() {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 10);

        // === Cột trái: Hồ sơ (40%) ===
        gbc.gridx = 0;
        gbc.weightx = 0.4;
        add(createProfilePanel(), gbc);

        // === Cột phải: Cài đặt hệ thống (60%) bọc trong JScrollPane ===
        JPanel settingsPanel = createSettingsPanel();
        JScrollPane scrollPane = new JScrollPane(settingsPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 10, 0, 0);
        add(scrollPane, gbc);
    }

    // =========================================================================
    // CỘT TRÁI - PANEL HỒ SƠ (40%)
    // =========================================================================

    /**
     * Tạo panel hồ sơ (cột trái) với avatar hình tròn, tên, chức vụ,
     * thông tin liên hệ có icon emoji, và nút "Chỉnh sửa hồ sơ".
     */
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor"), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 5, 0);

        // --- Avatar (hình tròn) ---
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        lblAvatar = new JLabel();
        lblAvatar.setIcon(createDefaultAvatarIcon(100));
        panel.add(lblAvatar, gbc);

        // --- Tên nhân viên (in đậm) ---
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 2, 0);
        lblName = new JLabel(DEFAULT_NAME);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(lblName, gbc);

        // --- Chức vụ (in đậm) ---
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        lblPosition = new JLabel(DEFAULT_POSITION);
        lblPosition.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPosition.setForeground(UIManager.getColor("Label.disabledForeground"));
        panel.add(lblPosition, gbc);

        // --- Thông tin chi tiết với emoji icon ---
        gbc.gridy = 3;
        gbc.insets = new Insets(4, 0, 4, 0);
        panel.add(createInfoLabel("\uD83D\uDCE7", "Email: " + DEFAULT_EMAIL), gbc);

        gbc.gridy = 4;
        panel.add(createInfoLabel("\uD83C\uDFE2", "Bộ phận: " + DEFAULT_DEPARTMENT), gbc);

        gbc.gridy = 5;
        panel.add(createInfoLabel("\uD83D\uDCC5", "Ngày tham gia: " + DEFAULT_JOIN_DATE), gbc);

        // --- Đẩy phần còn lại xuống dưới ---
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(Box.createGlue(), gbc);

        // --- Nút "Chỉnh sửa hồ sơ" - kéo dài hết chiều ngang ---
        gbc.gridy = 7;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        JButton btnEditProfile = new JButton("Chỉnh sửa hồ sơ");
        btnEditProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditProfile.putClientProperty("JButton.buttonType", "roundRect");
        btnEditProfile.addActionListener(e -> onEditProfile());
        panel.add(btnEditProfile, gbc);

        return panel;
    }

    /**
     * Tạo JLabel hiển thị thông tin với icon emoji phía trước.
     */
    private JLabel createInfoLabel(String emoji, String text) {
        JLabel label = new JLabel(emoji + "  " + text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return label;
    }

    /**
     * Tạo icon avatar mặc định hình tròn với chữ cái đầu.
     *
     * @param size Kích thước icon (pixel)
     * @return ImageIcon hình tròn
     */
    private ImageIcon createDefaultAvatarIcon(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ hình tròn nền - sử dụng màu FlatLaf tương thích theme
        Color avatarBg = UIManager.getColor("Component.accentColor");
        if (avatarBg == null) {
            avatarBg = new Color(41, 128, 185);
        }
        g2.setColor(avatarBg);
        g2.fill(new Ellipse2D.Double(0, 0, size, size));

        // Vẽ chữ cái đầu tiên của tên
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, size / 2));
        FontMetrics fm = g2.getFontMetrics();
        String initial = DEFAULT_NAME.isEmpty() ? "?" : DEFAULT_NAME.substring(0, 1).toUpperCase();
        int textX = (size - fm.stringWidth(initial)) / 2;
        int textY = (size - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(initial, textX, textY);

        g2.dispose();
        return new ImageIcon(image);
    }

    /**
     * Xử lý sự kiện khi nhấn nút "Chỉnh sửa hồ sơ".
     */
    private void onEditProfile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh đại diện");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Hình ảnh (*.jpg, *.png, *.gif)", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "Đã chọn ảnh: " + fileChooser.getSelectedFile().getName(),
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // =========================================================================
    // CỘT PHẢI - CÁC CARD CÀI ĐẶT (60%)
    // =========================================================================

    /**
     * Tạo panel chứa 3 Card cài đặt xếp chồng theo chiều dọc.
     */
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Card 1: Giao diện & Hiển thị
        panel.add(createAppearanceCard());
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Card 2: Thông báo & Bảo mật
        panel.add(createNotificationSecurityCard());
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Card 3: Lưu trữ & Hệ thống
        panel.add(createStorageCard());

        // Đẩy phần thừa xuống dưới
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // =========================================================================
    // CARD 1: GIAO DIỆN & HIỂN THỊ
    // =========================================================================

    /**
     * Tạo Card "Giao diện & Hiển thị" với JComboBox cho theme và kích cỡ chữ.
     */
    private JPanel createAppearanceCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createMatteBorder(1, 1, 1, 1,
                                UIManager.getColor("Component.borderColor") != null
                                        ? UIManager.getColor("Component.borderColor")
                                        : new Color(200, 200, 200)),
                        "Giao diện & Hiển thị",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14)
                ),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Dòng 1: Chủ đề (☀️ icon)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        card.add(new JLabel("☀️ Chủ đề:"), gbc);

        cboTheme = new JComboBox<>(new String[]{"☀️ Sáng (Light)", "\uD83C\uDF19 Tối (Dark)"});
        cboTheme.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selected = (String) cboTheme.getSelectedItem();
                if (selected != null) {
                    switchTheme(selected.contains("Tối"));
                }
            }
        });
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(cboTheme, gbc);

        // Dòng 2: Kích cỡ chữ (📏 icon, JComboBox thay cho JSlider)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("\uD83D\uDCCF Kích cỡ chữ:"), gbc);

        cboFontSize = new JComboBox<>(new String[]{"Nhỏ (12px)", "Vừa (14px)", "Lớn (16px)", "Rất lớn (18px)"});
        cboFontSize.setSelectedIndex(1); // Mặc định "Vừa (14px)"
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(cboFontSize, gbc);

        return card;
    }

    // =========================================================================
    // CARD 2: THÔNG BÁO & BẢO MẬT
    // =========================================================================

    /**
     * Tạo Card "Thông báo & Bảo mật" với các trường đổi Email/SĐT,
     * bật/tắt thông báo, và đổi mật khẩu.
     */
    private JPanel createNotificationSecurityCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createMatteBorder(1, 1, 1, 1,
                                UIManager.getColor("Component.borderColor") != null
                                        ? UIManager.getColor("Component.borderColor")
                                        : new Color(200, 200, 200)),
                        "Thông báo & Bảo mật",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14)
                ),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Dòng 1: Bật thông báo (🔔 icon)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        card.add(new JLabel("\uD83D\uDD14 Thông báo:"), gbc);

        chkNotification = new JCheckBox("Bật thông báo hệ thống");
        chkNotification.setSelected(true);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(chkNotification, gbc);

        // Dòng 2: Số điện thoại (📱 icon)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("\uD83D\uDCF1 Số điện thoại:"), gbc);

        txtPhone = new JTextField(DEFAULT_PHONE, 20);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtPhone, gbc);

        // Dòng 3: Email (✉️ icon)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("✉️ Email:"), gbc);

        txtEmail = new JTextField(DEFAULT_EMAIL, 20);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtEmail, gbc);

        // Nút cập nhật thông tin
        JButton btnUpdateContact = new JButton("Cập nhật thông tin");
        btnUpdateContact.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdateContact.addActionListener(e -> onUpdateContact());
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 5, 10, 5);
        card.add(btnUpdateContact, gbc);

        // Separator
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        card.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Dòng 5: Mật khẩu hiện tại (🔐 icon)
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("\uD83D\uDD10 Mật khẩu hiện tại:"), gbc);

        txtCurrentPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtCurrentPassword, gbc);

        // Dòng 6: Mật khẩu mới (🔐 icon)
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("\uD83D\uDD10 Mật khẩu mới:"), gbc);

        txtNewPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtNewPassword, gbc);

        // Dòng 7: Xác nhận mật khẩu mới (🔐 icon)
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("\uD83D\uDD10 Xác nhận mật khẩu:"), gbc);

        txtConfirmPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtConfirmPassword, gbc);

        // Nút cập nhật mật khẩu
        JButton btnUpdatePassword = new JButton("Cập nhật mật khẩu");
        btnUpdatePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdatePassword.putClientProperty("JButton.buttonType", "roundRect");
        btnUpdatePassword.addActionListener(e -> onUpdatePassword());
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 5, 5, 5);
        card.add(btnUpdatePassword, gbc);

        return card;
    }

    /**
     * Xử lý sự kiện khi nhấn "Cập nhật thông tin".
     */
    private void onUpdateContact() {
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        JOptionPane.showMessageDialog(this,
                "Cập nhật thông tin liên hệ thành công!\n"
                + "Số điện thoại: " + phone + "\n"
                + "Email: " + email,
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Xử lý sự kiện khi nhấn "Cập nhật mật khẩu".
     */
    private void onUpdatePassword() {
        String currentPass = new String(txtCurrentPassword.getPassword());
        String newPass = new String(txtNewPassword.getPassword());
        String confirmPass = new String(txtConfirmPassword.getPassword());

        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền đầy đủ thông tin mật khẩu!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this,
                    "Mật khẩu mới và xác nhận mật khẩu không khớp!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Cập nhật mật khẩu thành công!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);

        // Xóa trắng các trường mật khẩu sau khi cập nhật
        txtCurrentPassword.setText("");
        txtNewPassword.setText("");
        txtConfirmPassword.setText("");
    }

    // =========================================================================
    // CARD 3: LƯU TRỮ & HỆ THỐNG
    // =========================================================================

    /**
     * Tạo Card "Lưu trữ & Hệ thống" với các cài đặt lưu trữ và xóa cache.
     */
    private JPanel createStorageCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createMatteBorder(1, 1, 1, 1,
                                UIManager.getColor("Component.borderColor") != null
                                        ? UIManager.getColor("Component.borderColor")
                                        : new Color(200, 200, 200)),
                        "Lưu trữ & Hệ thống",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14)
                ),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Dòng 1: Lưu tự động (💾 icon)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        card.add(new JLabel("\uD83D\uDCBE Lưu trữ:"), gbc);

        chkAutoSave = new JCheckBox("Lưu tự động");
        chkAutoSave.setSelected(true);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(chkAutoSave, gbc);

        // Dòng 2: Xóa bộ nhớ cache (🗑️ icon)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("\uD83D\uDDD1️ Bộ nhớ cache:"), gbc);

        JButton btnClearCache = new JButton("Xóa bộ nhớ cache");
        btnClearCache.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClearCache.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                    "Bộ nhớ cache đã được xóa thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE)
        );
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        card.add(btnClearCache, gbc);

        // Dòng 3: Phiên bản hệ thống (ℹ️ icon)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("ℹ️ Phiên bản:"), gbc);

        JLabel lblVersion = new JLabel("v1.0.0 - SNAPSHOT");
        lblVersion.setForeground(UIManager.getColor("Label.disabledForeground"));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        card.add(lblVersion, gbc);

        return card;
    }

    // =========================================================================
    // THEME SWITCHING
    // =========================================================================

    /**
     * Chuyển đổi giữa FlatLightLaf và FlatDarkLaf.
     *
     * @param isDark true = Dark theme, false = Light theme
     */
    private void switchTheme(boolean isDark) {
        try {
            if (isDark) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            // Cập nhật giao diện cho toàn bộ cây component
            Window topLevel = SwingUtilities.getWindowAncestor(this);
            if (topLevel != null) {
                SwingUtilities.updateComponentTreeUI(topLevel);
            } else {
                SwingUtilities.updateComponentTreeUI(this);
            }
        } catch (UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(this,
                    "Không thể chuyển đổi giao diện: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
