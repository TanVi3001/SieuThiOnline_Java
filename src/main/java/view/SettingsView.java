package view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * SettingsView - Màn hình Cài đặt cho Hệ thống Quản lý Siêu thị.
 *
 * Bố cục 2 cột sử dụng JSplitPane:
 *   - Cột trái (30%): Panel hồ sơ cá nhân (avatar, tên, chức vụ)
 *   - Cột phải (70%): Các form cài đặt (Thông tin liên hệ, Bảo mật, Giao diện)
 *
 * @author Admin
 */
public class SettingsView extends javax.swing.JPanel {

    // ==========================================
    // Hằng số màu sắc và font chữ
    // ==========================================
    private static final Color PRIMARY_COLOR = new Color(41, 98, 255);
    private static final Color CARD_BORDER_COLOR = new Color(220, 220, 220);
    private static final Color LABEL_GRAY = new Color(120, 120, 120);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_NAME = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_POSITION = new Font("Segoe UI", Font.PLAIN, 14);

    // ==========================================
    // Các thành phần giao diện (fields)
    // ==========================================
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JPasswordField txtCurrentPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private JComboBox<String> cboTheme;

    /**
     * Khởi tạo SettingsView.
     */
    public SettingsView() {
        initSettingsUI();
    }

    /**
     * Xây dựng toàn bộ giao diện màn hình Cài đặt.
     */
    private void initSettingsUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 247));

        // ==========================================
        // Tạo JSplitPane chia 2 cột (Trái 30% / Phải 70%)
        // ==========================================
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createProfilePanel());
        splitPane.setRightComponent(createSettingsPanel());
        splitPane.setDividerSize(0);           // Ẩn thanh kéo chia cột
        splitPane.setEnabled(false);           // Không cho phép kéo thay đổi kích thước
        splitPane.setBorder(null);
        splitPane.setResizeWeight(0.3);        // Cột trái chiếm 30%

        add(splitPane, BorderLayout.CENTER);
    }

    // ==========================================================================
    //  CỘT TRÁI – Panel Hồ sơ cá nhân
    // ==========================================================================

    /**
     * Tạo panel hồ sơ cá nhân (cột trái) với avatar, tên nhân viên,
     * chức vụ và nút chọn ảnh đại diện.
     */
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(40, 30, 40, 30));

        // --- Hình đại diện (Avatar) ---
        JLabel lblAvatar = createAvatarLabel();
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblAvatar);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- Tên nhân viên ---
        JLabel lblName = new JLabel("Nguyễn Văn A");
        lblName.setFont(FONT_NAME);
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblName);

        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        // --- Chức vụ ---
        JLabel lblPosition = new JLabel("Quản trị viên");
        lblPosition.setFont(FONT_POSITION);
        lblPosition.setForeground(LABEL_GRAY);
        lblPosition.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblPosition);

        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // --- Nút chọn ảnh đại diện ---
        JButton btnChooseAvatar = new JButton("Chọn ảnh đại diện");
        btnChooseAvatar.setFont(FONT_LABEL);
        btnChooseAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChooseAvatar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChooseAvatar.setFocusPainted(false);
        panel.add(btnChooseAvatar);

        // Đẩy nội dung lên trên cùng, phần dưới dãn ra
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Tạo JLabel hình đại diện với viền bo tròn.
     * Vẽ một hình tròn xám nhạt kèm chữ viết tắt làm avatar mặc định.
     */
    private JLabel createAvatarLabel() {
        final int size = 100;
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Vẽ hình tròn nền xám nhạt
                g2.setColor(new Color(200, 210, 230));
                g2.fillOval(0, 0, size, size);

                // Vẽ chữ viết tắt ở giữa
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 36));
                FontMetrics fm = g2.getFontMetrics();
                String initials = "NV";
                int textX = (size - fm.stringWidth(initials)) / 2;
                int textY = (size - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(initials, textX, textY);

                g2.dispose();
            }
        };
        label.setPreferredSize(new Dimension(size, size));
        label.setMinimumSize(new Dimension(size, size));
        label.setMaximumSize(new Dimension(size, size));
        return label;
    }

    // ==========================================================================
    //  CỘT PHẢI – Các form Cài đặt (bọc trong JScrollPane)
    // ==========================================================================

    /**
     * Tạo panel chứa 3 thẻ cài đặt, bọc trong JScrollPane cuộn dọc.
     */
    private JScrollPane createSettingsPanel() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(245, 245, 247));
        container.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Thêm 3 thẻ cài đặt
        container.add(createContactCard());
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        container.add(createSecurityCard());
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        container.add(createAppearanceCard());

        // Đẩy nội dung lên trên, phần dưới dãn ra
        container.add(Box.createVerticalGlue());

        // Bọc trong JScrollPane – không viền, chỉ cuộn dọc khi cần
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    // ==========================================================================
    //  THẺ 1 – Thông tin liên hệ
    // ==========================================================================

    /**
     * Tạo thẻ "Thông tin liên hệ" với trường Số điện thoại, Email
     * và nút Cập nhật thông tin.
     */
    private JPanel createContactCard() {
        JPanel card = createCard("Thông tin liên hệ");
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Dòng 1: Số điện thoại ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel lblPhone = new JLabel("Số điện thoại:");
        lblPhone.setFont(FONT_LABEL);
        card.add(lblPhone, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtPhone = new JTextField(20);
        txtPhone.setFont(FONT_LABEL);
        card.add(txtPhone, gbc);

        // --- Dòng 2: Email ---
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(FONT_LABEL);
        card.add(lblEmail, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        txtEmail.setFont(FONT_LABEL);
        card.add(txtEmail, gbc);

        // --- Dòng 3: Nút Cập nhật thông tin (căn phải) ---
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        JButton btnUpdate = new JButton("Cập nhật thông tin");
        btnUpdate.setFont(FONT_LABEL);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdate.setFocusPainted(false);
        card.add(btnUpdate, gbc);

        return card;
    }

    // ==========================================================================
    //  THẺ 2 – Bảo mật
    // ==========================================================================

    /**
     * Tạo thẻ "Bảo mật" với các trường mật khẩu hiện tại, mật khẩu mới,
     * xác nhận mật khẩu mới và nút Cập nhật mật khẩu.
     */
    private JPanel createSecurityCard() {
        JPanel card = createCard("Bảo mật");
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Dòng 1: Mật khẩu hiện tại ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel lblCurrent = new JLabel("Mật khẩu hiện tại:");
        lblCurrent.setFont(FONT_LABEL);
        card.add(lblCurrent, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtCurrentPassword = new JPasswordField(20);
        txtCurrentPassword.setFont(FONT_LABEL);
        card.add(txtCurrentPassword, gbc);

        // --- Dòng 2: Mật khẩu mới ---
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblNew = new JLabel("Mật khẩu mới:");
        lblNew.setFont(FONT_LABEL);
        card.add(lblNew, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtNewPassword = new JPasswordField(20);
        txtNewPassword.setFont(FONT_LABEL);
        card.add(txtNewPassword, gbc);

        // --- Dòng 3: Xác nhận mật khẩu mới ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel lblConfirm = new JLabel("Xác nhận mật khẩu mới:");
        lblConfirm.setFont(FONT_LABEL);
        card.add(lblConfirm, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtConfirmPassword = new JPasswordField(20);
        txtConfirmPassword.setFont(FONT_LABEL);
        card.add(txtConfirmPassword, gbc);

        // --- Dòng 4: Nút Cập nhật mật khẩu (căn phải, màu nổi bật) ---
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        JButton btnUpdatePassword = new JButton("Cập nhật mật khẩu");
        btnUpdatePassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnUpdatePassword.setBackground(PRIMARY_COLOR);
        btnUpdatePassword.setForeground(Color.WHITE);
        btnUpdatePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdatePassword.setFocusPainted(false);
        card.add(btnUpdatePassword, gbc);

        return card;
    }

    // ==========================================================================
    //  THẺ 3 – Giao diện (Chọn chủ đề Sáng / Tối)
    // ==========================================================================

    /**
     * Tạo thẻ "Giao diện" cho phép chuyển đổi chủ đề Sáng / Tối
     * sử dụng FlatLightLaf và FlatDarkLaf.
     */
    private JPanel createAppearanceCard() {
        JPanel card = createCard("Giao diện");
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Dòng 1: Label chủ đề ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel lblTheme = new JLabel("Chủ đề:");
        lblTheme.setFont(FONT_LABEL);
        card.add(lblTheme, gbc);

        // --- Dòng 1: ComboBox chọn chủ đề ---
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cboTheme = new JComboBox<>(new String[]{"Sáng", "Tối"});
        cboTheme.setFont(FONT_LABEL);

        // Xác định chủ đề hiện tại để đặt giá trị mặc định
        boolean isDark = UIManager.getLookAndFeel() instanceof FlatDarkLaf;
        cboTheme.setSelectedItem(isDark ? "Tối" : "Sáng");

        // --- Event Listener: Chuyển đổi chủ đề ---
        // Dùng cờ để tránh kích hoạt khi đặt giá trị mặc định lần đầu
        final boolean[] initialized = {false};
        cboTheme.addActionListener(e -> {
            if (!initialized[0]) {
                initialized[0] = true;
                return;
            }
            try {
                String selected = (String) cboTheme.getSelectedItem();
                boolean currentIsDark = UIManager.getLookAndFeel() instanceof FlatDarkLaf;

                // Chỉ chuyển đổi nếu chủ đề thực sự thay đổi
                if ("Tối".equals(selected) && !currentIsDark) {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                } else if ("Sáng".equals(selected) && currentIsDark) {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                } else {
                    return; // Không có thay đổi, bỏ qua
                }
                // Cập nhật giao diện toàn bộ cây component
                Window topWindow = SwingUtilities.getWindowAncestor(this);
                if (topWindow != null) {
                    SwingUtilities.updateComponentTreeUI(topWindow);
                }
            } catch (UnsupportedLookAndFeelException ex) {
                JOptionPane.showMessageDialog(this,
                        "Không thể chuyển đổi chủ đề: " + ex.getMessage(),
                        "Lỗi giao diện",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        initialized[0] = true;
        card.add(cboTheme, gbc);

        return card;
    }

    // ==========================================================================
    //  PHƯƠNG THỨC TIỆN ÍCH
    // ==========================================================================

    /**
     * Tạo một thẻ (card) JPanel cơ bản với tiêu đề, viền và padding.
     *
     * @param title Tiêu đề hiển thị trên viền thẻ
     * @return JPanel đã được định dạng sẵn
     */
    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Viền: TitledBorder bao ngoài + EmptyBorder padding bên trong
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, CARD_BORDER_COLOR),
                title);
        titledBorder.setTitleFont(FONT_TITLE);
        card.setBorder(BorderFactory.createCompoundBorder(
                titledBorder,
                new EmptyBorder(15, 15, 15, 15)));

        // Giới hạn chiều cao tối đa để BoxLayout không kéo dãn quá mức
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        return card;
    }

    // ==========================================================================
    //  MAIN – Chạy thử độc lập để xem trước giao diện
    // ==========================================================================

    /**
     * Phương thức main để chạy thử SettingsView độc lập.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Không thể khởi tạo FlatLaf: " + ex.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cài đặt - Hệ thống Quản lý Siêu thị");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new SettingsView());
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
