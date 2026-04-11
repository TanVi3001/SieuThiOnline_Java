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
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

/**
 * SettingsView - Màn hình Cài đặt cho Hệ thống Quản lý Siêu thị.
 * Giao diện 2 cột: Cột trái (Hồ sơ) - Cột phải (Các form cài đặt).
 * Sử dụng JSplitPane, GridBagLayout, BoxLayout, FlatLaf theme switching.
 *
 * @author Le Tan Vi
 */
public class SettingsView extends JPanel {

    // ===== Màu sắc chung =====
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color COLOR_BORDER = new Color(220, 220, 220);  // Xám nhạt cho viền
    private static final Color COLOR_GRAY_TEXT = new Color(120, 120, 120); // Xám cho chữ phụ
    private static final Color COLOR_PRIMARY = new Color(41, 128, 185);  // Xanh dương chính
    private static final Color COLOR_PRIMARY_HOVER = new Color(52, 152, 219);

    // ===== Dữ liệu mặc định =====
    private static final String DEFAULT_NAME = "Nguyễn Văn A";
    private static final String DEFAULT_POSITION = "Quản trị viên";
    private static final String DEFAULT_PHONE = "0123456789";
    private static final String DEFAULT_EMAIL = "admin@supermarket.com";

    // ===== Các thành phần giao diện =====
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JPasswordField txtCurrentPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private JRadioButton rbLight;
    private JRadioButton rbDark;
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
     * Khởi tạo toàn bộ giao diện chính với JSplitPane chia 2 cột.
     */
    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 247));

        // === Cột trái: Panel Hồ sơ (30%) ===
        JPanel profilePanel = createProfilePanel();

        // === Cột phải: Các form Cài đặt (70%) bọc trong JScrollPane ===
        JPanel settingsPanel = createSettingsPanel();
        JScrollPane scrollPane = new JScrollPane(settingsPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // === JSplitPane chia 2 cột, ẩn thanh kéo ===
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, profilePanel, scrollPane);
        splitPane.setDividerSize(0);          // Ẩn thanh kéo
        splitPane.setEnabled(false);          // Không cho kéo thay đổi kích thước
        splitPane.setResizeWeight(0.3);       // Cột trái chiếm 30%
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);
    }

    // =========================================================================
    // CỘT TRÁI - PANEL HỒ SƠ
    // =========================================================================

    /**
     * Tạo panel hồ sơ (cột trái) với avatar, tên, chức vụ và nút chọn ảnh.
     */
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 0, 1, COLOR_BORDER), // Viền phải xám nhạt
                new EmptyBorder(20, 20, 20, 20)             // Padding 20px các bên
        ));

        // Khoảng trống phía trên
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- Avatar (hình tròn mặc định) ---
        lblAvatar = new JLabel();
        lblAvatar.setIcon(createDefaultAvatarIcon(100));
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblAvatar);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- Tên nhân viên ---
        lblName = new JLabel(DEFAULT_NAME);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblName);

        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        // --- Chức vụ ---
        lblPosition = new JLabel(DEFAULT_POSITION);
        lblPosition.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPosition.setForeground(COLOR_GRAY_TEXT);
        lblPosition.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblPosition);

        // Khoảng trống dọc trước nút
        panel.add(Box.createRigidArea(new Dimension(0, 40)));

        // --- Nút "Chọn ảnh đại diện" ---
        JButton btnSelectAvatar = new JButton("Chọn ảnh đại diện");
        btnSelectAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSelectAvatar.setMaximumSize(new Dimension(200, 35));
        btnSelectAvatar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSelectAvatar.addActionListener(e -> onSelectAvatar());
        panel.add(btnSelectAvatar);

        // Đẩy phần còn lại xuống dưới
        panel.add(Box.createVerticalGlue());

        return panel;
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

        // Vẽ hình tròn nền
        g2.setColor(COLOR_PRIMARY);
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
     * Xử lý sự kiện khi nhấn nút "Chọn ảnh đại diện".
     */
    private void onSelectAvatar() {
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
    // CỘT PHẢI - CÁC FORM CÀI ĐẶT
    // =========================================================================

    /**
     * Tạo panel chứa 3 thẻ cài đặt xếp chồng theo chiều dọc.
     */
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 247));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Thẻ 1: Thông tin liên hệ
        panel.add(createContactCard());
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Thẻ 2: Bảo mật
        panel.add(createSecurityCard());
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Thẻ 3: Giao diện
        panel.add(createAppearanceCard());

        // Đẩy phần thừa xuống dưới
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // =========================================================================
    // THẺ 1: THÔNG TIN LIÊN HỆ
    // =========================================================================

    /**
     * Tạo thẻ "Thông tin liên hệ" với các trường Số điện thoại và Email.
     */
    private JPanel createContactCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(COLOR_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        new MatteBorder(1, 1, 1, 1, COLOR_BORDER),
                        "Thông tin liên hệ",
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

        // Dòng 1: Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        card.add(new JLabel("Số điện thoại:"), gbc);

        txtPhone = new JTextField(DEFAULT_PHONE, 20);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtPhone, gbc);

        // Dòng 2: Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField(DEFAULT_EMAIL, 20);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtEmail, gbc);

        // Dòng 3: Nút "Cập nhật thông tin" (căn sát lề phải)
        JButton btnUpdateContact = new JButton("Cập nhật thông tin");
        btnUpdateContact.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdateContact.addActionListener(e -> onUpdateContact());

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 5, 5, 5);
        card.add(btnUpdateContact, gbc);

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

    // =========================================================================
    // THẺ 2: BẢO MẬT
    // =========================================================================

    /**
     * Tạo thẻ "Bảo mật" với các trường mật khẩu hiện tại, mới và xác nhận.
     */
    private JPanel createSecurityCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(COLOR_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        new MatteBorder(1, 1, 1, 1, COLOR_BORDER),
                        "Bảo mật",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14)
                ),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Dòng 1: Mật khẩu hiện tại
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        card.add(new JLabel("Mật khẩu hiện tại:"), gbc);

        txtCurrentPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtCurrentPassword, gbc);

        // Dòng 2: Mật khẩu mới
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("Mật khẩu mới:"), gbc);

        txtNewPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtNewPassword, gbc);

        // Dòng 3: Xác nhận mật khẩu mới
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        card.add(new JLabel("Xác nhận mật khẩu mới:"), gbc);

        txtConfirmPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(txtConfirmPassword, gbc);

        // Dòng 4: Nút "Cập nhật mật khẩu" (styling primary - nổi bật)
        JButton btnUpdatePassword = new JButton("Cập nhật mật khẩu");
        btnUpdatePassword.setBackground(COLOR_PRIMARY);
        btnUpdatePassword.setForeground(Color.WHITE);
        btnUpdatePassword.setFocusPainted(false);
        btnUpdatePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdatePassword.putClientProperty("JButton.buttonType", "roundRect");
        btnUpdatePassword.addActionListener(e -> onUpdatePassword());

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 5, 5, 5);
        card.add(btnUpdatePassword, gbc);

        return card;
    }

    /**
     * Xử lý sự kiện khi nhấn "Cập nhật mật khẩu".
     */
    private void onUpdatePassword() {
        String newPass = new String(txtNewPassword.getPassword());
        String confirmPass = new String(txtConfirmPassword.getPassword());

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
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
    // THẺ 3: GIAO DIỆN
    // =========================================================================

    /**
     * Tạo thẻ "Giao diện" với lựa chọn chủ đề Sáng / Tối.
     */
    private JPanel createAppearanceCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(COLOR_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        new MatteBorder(1, 1, 1, 1, COLOR_BORDER),
                        "Giao diện",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14)
                ),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Label "Chủ đề"
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        card.add(new JLabel("Chủ đề:"), gbc);

        // Panel chứa các JRadioButton (Sáng / Tối)
        JPanel themePanel = new JPanel();
        themePanel.setLayout(new BoxLayout(themePanel, BoxLayout.X_AXIS));
        themePanel.setBackground(COLOR_WHITE);

        rbLight = new JRadioButton("Sáng");
        rbDark = new JRadioButton("Tối");

        // Nhóm radio button để chỉ chọn 1
        ButtonGroup themeGroup = new ButtonGroup();
        themeGroup.add(rbLight);
        themeGroup.add(rbDark);

        // Mặc định chọn "Sáng"
        rbLight.setSelected(true);
        rbLight.setBackground(COLOR_WHITE);
        rbDark.setBackground(COLOR_WHITE);

        // ItemListener để chuyển đổi theme
        rbLight.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                switchTheme(false);
            }
        });

        rbDark.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                switchTheme(true);
            }
        });

        themePanel.add(rbLight);
        themePanel.add(Box.createRigidArea(new Dimension(15, 0)));
        themePanel.add(rbDark);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        card.add(themePanel, gbc);

        return card;
    }

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
