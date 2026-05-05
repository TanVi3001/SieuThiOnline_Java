package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SettingsAdminPanel extends JPanel {

    private final Color bgLight = new Color(244, 246, 250);
    private final Color cardWhite = Color.WHITE;
    private final Color textDark = new Color(43, 54, 116);
    private final Color textGray = new Color(163, 174, 208);
    private final Color borderGray = new Color(230, 235, 241);

    public SettingsAdminPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(bgLight);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        initUI();
    }

    private void initUI() {
        // ── 1. HEADER ────────────────────────────────────────────────────────
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel lblTitle = new JLabel("Cài Đặt Hệ Thống");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(textDark);
        JLabel lblSub = new JLabel("Tùy chỉnh thông số cốt lõi và bảo mật hệ thống");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(textGray);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // ── 2. CARDS BỐ CỤC DẠNG LƯỚI (GRID) ─────────────────────────────────
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        centerPanel.setOpaque(false);

        // CARD 1: Đổi Mật Khẩu Admin
        RoundedPanel cardSecurity = new RoundedPanel(20, cardWhite);
        cardSecurity.setLayout(new BoxLayout(cardSecurity, BoxLayout.Y_AXIS));
        cardSecurity.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JLabel lblSecTitle = new JLabel("Bảo mật tài khoản");
        lblSecTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSecTitle.setForeground(textDark);
        cardSecurity.add(lblSecTitle);
        cardSecurity.add(Box.createRigidArea(new Dimension(0, 25)));

        cardSecurity.add(createLabel("Mật khẩu hiện tại"));
        cardSecurity.add(Box.createRigidArea(new Dimension(0, 5)));
        cardSecurity.add(createPasswordField());
        cardSecurity.add(Box.createRigidArea(new Dimension(0, 15)));

        cardSecurity.add(createLabel("Mật khẩu mới"));
        cardSecurity.add(Box.createRigidArea(new Dimension(0, 5)));
        cardSecurity.add(createPasswordField());
        cardSecurity.add(Box.createRigidArea(new Dimension(0, 15)));

        cardSecurity.add(createLabel("Xác nhận mật khẩu mới"));
        cardSecurity.add(Box.createRigidArea(new Dimension(0, 5)));
        cardSecurity.add(createPasswordField());
        cardSecurity.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton btnChangePass = createCustomButton("Cập nhật mật khẩu", new Color(0, 168, 140), Color.WHITE);
        btnChangePass.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardSecurity.add(btnChangePass);

        // CARD 2: Cấu hình Email Service (SMTP)
        RoundedPanel cardEmail = new RoundedPanel(20, cardWhite);
        cardEmail.setLayout(new BoxLayout(cardEmail, BoxLayout.Y_AXIS));
        cardEmail.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblEmailTitle = new JLabel("Cấu hình Email Kích hoạt (SMTP)");
        lblEmailTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblEmailTitle.setForeground(textDark);
        cardEmail.add(lblEmailTitle);
        cardEmail.add(Box.createRigidArea(new Dimension(0, 25)));

        cardEmail.add(createLabel("Email gửi đi (Sender)"));
        cardEmail.add(Box.createRigidArea(new Dimension(0, 5)));
        JTextField txtEmail = createTextField();
        txtEmail.setText("admin.smartsupermarket@gmail.com");
        cardEmail.add(txtEmail);
        cardEmail.add(Box.createRigidArea(new Dimension(0, 15)));

        cardEmail.add(createLabel("App Password (Mật khẩu ứng dụng)"));
        cardEmail.add(Box.createRigidArea(new Dimension(0, 5)));
        JPasswordField txtAppPass = createPasswordField();
        txtAppPass.setText("1234567890abcdef");
        cardEmail.add(txtAppPass);
        cardEmail.add(Box.createRigidArea(new Dimension(0, 15)));

        cardEmail.add(createLabel("SMTP Server (Mặc định)"));
        cardEmail.add(Box.createRigidArea(new Dimension(0, 5)));
        JTextField txtHost = createTextField();
        txtHost.setText("smtp.gmail.com (Port: 587)");
        txtHost.setEnabled(false);
        cardEmail.add(txtHost);
        cardEmail.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton btnSaveEmail = createCustomButton("Lưu cấu hình", new Color(54, 92, 245), Color.WHITE);
        btnSaveEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardEmail.add(btnSaveEmail);

        // Ghép 2 Card vào lưới
        centerPanel.add(cardSecurity);
        centerPanel.add(cardEmail);

        // Đẩy lưới lên trên, phần dưới để trống
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(centerPanel, BorderLayout.NORTH);

        add(wrapper, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(textDark);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(new RoundBorder(borderGray, 8), new EmptyBorder(5, 10, 5, 10)));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        return txt;
    }

    private JPasswordField createPasswordField() {
        JPasswordField txt = new JPasswordField();
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(new RoundBorder(borderGray, 8), new EmptyBorder(5, 10, 5, 10)));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        return txt;
    }

    private JButton createCustomButton(String t, Color bg, Color fg) {
        JButton btn = new JButton(t);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg); btn.setBackground(bg);
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setCursor(new Cursor(12)); btn.setFocusPainted(false); btn.setBorderPainted(false); btn.setContentAreaFilled(false);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground()); g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);
                super.paint(g2, c); g2.dispose();
            }
        });
        return btn;
    }

    class RoundedPanel extends JPanel {
        private int r; private Color bg;
        public RoundedPanel(int r, Color bg) { this.r = r; this.bg = bg; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg); g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
            g2.dispose();
        }
    }

    class RoundBorder implements javax.swing.border.Border {
        private Color c; private int r;
        public RoundBorder(Color c, int r) { this.c = c; this.r = r; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(this.c); g2.drawRoundRect(x, y, w - 1, h - 1, r, r);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(1, 1, 1, 1); }
        @Override public boolean isBorderOpaque() { return false; }
    }
}