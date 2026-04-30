package view;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CustomerView extends JPanel {

    // --- CẤU HÌNH MÀU SẮC CHUẨN MODERN UI ---
    private final Color bgLight = new Color(244, 246, 250);
    private final Color cardWhite = Color.WHITE;
    private final Color primaryBlue = new Color(67, 97, 238);
    private final Color textDark = new Color(43, 54, 116);
    private final Color textGray = new Color(163, 174, 208);
    private final Color borderGray = new Color(230, 235, 241);

    // --- KHAI BÁO UI COMPONENTS ---
    private JTextField txtId, txtName, txtPhone, txtEmail, txtAddress;
    private JComboBox<String> cbSearch; // Thay txtSearch bằng ComboBox
    private JTable tblCustomers;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;

    // Danh sách lưu dữ liệu gợi ý (Tên - SĐT)
    private List<String> customerSearchList = new ArrayList<>();

    public CustomerView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(bgLight);
        setBorder(new EmptyBorder(20, 30, 20, 30)); // Padding cho toàn màn hình

        loadAutoCompleteData(); // Load danh sách gợi ý trước
        initUI();
        initEvents();
        loadCustomerData(); // Load dữ liệu lên bảng
    }

    // Load dữ liệu Tên và SĐT khách hàng vào danh sách gợi ý
    private void loadAutoCompleteData() {
        customerSearchList.clear();
        try {
            java.util.List<model.order.Customer> list = business.sql.sales_order.CustomersSql.getInstance().getCustomersWithOrders();
            for (model.order.Customer c : list) {
                if (c.getCustomerName() != null && !c.getCustomerName().isEmpty()) {
                    String phone = c.getPhone() != null ? c.getPhone() : "N/A";
                    customerSearchList.add(c.getCustomerName() + " - " + phone);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        // ==========================================
        // 1. HEADER (Tiêu đề + Ô Tìm kiếm)
        // ==========================================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel lblTitle = new JLabel("Khách Hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(textDark);
        JLabel lblSub = new JLabel("Quản lý thông tin liên hệ và địa chỉ khách hàng");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(textGray);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);

        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        toolPanel.setOpaque(false);

        // --- CẤU HÌNH COMBOBOX TÌM KIẾM AUTO-COMPLETE ---
        cbSearch = new JComboBox<>();
        styleComboBox(cbSearch, "Nhập tên hoặc SĐT...");
        setupAutoComplete(cbSearch, customerSearchList);

        btnSearch = createCustomButton("Tìm kiếm", primaryBlue, Color.WHITE);
        toolPanel.add(cbSearch);
        toolPanel.add(btnSearch);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(toolPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // ==========================================
        // 2. CENTER (Thẻ Form trái + Thẻ Bảng phải)
        // ==========================================
        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setOpaque(false);

        // --- LEFT FORM (Thẻ Form trắng bo góc bên trái) ---
        RoundedPanel formCard = new RoundedPanel(20, cardWhite);
        formCard.setPreferredSize(new Dimension(350, 0));
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;

        txtId = createTextField("Mã tự động...");
        txtId.setEnabled(false); // Mã khách hàng thường không tự sửa
        txtName = createTextField("Nhập tên khách hàng...");
        txtPhone = createTextField("Nhập số điện thoại...");
        txtEmail = createTextField("Nhập email...");
        txtAddress = createTextField("Nhập địa chỉ...");

        int y = 0;
        gbc.gridy = y++;
        gbc.insets = new Insets(0, 0, 5, 0);
        formCard.add(createLabel("Mã khách hàng"), gbc);
        gbc.gridy = y++;
        gbc.insets = new Insets(0, 0, 15, 0);
        formCard.add(txtId, gbc);

        gbc.gridy = y++;
        gbc.insets = new Insets(0, 0, 5, 0);
        formCard.add(createLabel("Tên khách hàng (*)"), gbc);
        gbc.gridy = y++;
        gbc.insets = new Insets(0, 0, 15, 0);
        formCard.add(txtName, gbc);

        gbc.gridy = y++;
        gbc.insets = new Insets(0, 0, 5, 0);
        formCard.add(createLabel("Số điện thoại (*)"), gbc);
        gbc.gridy = y++;
        gbc.insets = new Insets(0, 0, 15, 0);
        formCard.add(txtPhone, gbc);

        gbc.gridy = y++;
        gbc.insets = new Insets(0, 0, 5, 0);
        formCard.add(createLabel("Email"), gbc);
        gbc.gridy = y++;
        gbc.insets = new Insets(0, 0, 15, 0);
        formCard.add(txtEmail, gbc);

        gbc.gridy = y++;
        gbc.insets = new Insets(0, 0, 5, 0);
        formCard.add(createLabel("Địa chỉ"), gbc);
        gbc.gridy = y++;
        gbc.insets = new Insets(0, 0, 30, 0);
        formCard.add(txtAddress, gbc);

        // GRID CHỨA 4 NÚT CHỨC NĂNG BÊN DƯỚI FORM
        btnAdd = createCustomButton("Thêm", primaryBlue, Color.WHITE);
        btnUpdate = createCustomButton("Cập nhật", new Color(255, 153, 0), Color.BLACK);
        btnDelete = createCustomButton("Xóa", new Color(220, 53, 69), Color.WHITE);
        btnClear = createCustomButton("Làm mới", new Color(230, 235, 241), textDark);

        JPanel btnGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        btnGrid.setOpaque(false);
        btnGrid.add(btnAdd);
        btnGrid.add(btnUpdate);
        btnGrid.add(btnDelete);
        btnGrid.add(btnClear);

        gbc.gridy = y++;
        formCard.add(btnGrid, gbc);
        centerPanel.add(formCard, BorderLayout.WEST);

        // --- RIGHT TABLE (Thẻ chứa JTable bo góc bên phải) ---
        RoundedPanel tableCard = new RoundedPanel(20, cardWhite);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new Object[]{"Mã KH", "Tên khách hàng", "Số điện thoại", "Email", "Địa chỉ"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblCustomers = new JTable(tableModel);

        // Chỉnh style bảng
        tblCustomers.setRowHeight(35);
        tblCustomers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblCustomers.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblCustomers.getTableHeader().setBackground(bgLight);
        tblCustomers.getTableHeader().setReorderingAllowed(false);
        tblCustomers.setShowVerticalLines(false);
        tblCustomers.setSelectionBackground(new Color(237, 242, 255));
        tblCustomers.setSelectionForeground(textDark);

        JScrollPane scrollPane = new JScrollPane(tblCustomers);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(tableCard, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    // ==========================================
    // LOGIC AUTO-COMPLETE COMBOBOX
    // ==========================================
    private void styleComboBox(JComboBox<String> cb, String placeholder) {
        cb.setPreferredSize(new Dimension(280, 40));
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        cb.setEditable(true);

        JTextField editor = (JTextField) cb.getEditor().getEditorComponent();
        editor.putClientProperty("JTextField.placeholderText", placeholder);
        editor.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(borderGray, 8), new EmptyBorder(5, 5, 5, 5)
        ));
    }

    private void setupAutoComplete(JComboBox<String> comboBox, List<String> originalItems) {
        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();

        for (String item : originalItems) {
            comboBox.addItem(item);
        }
        comboBox.setSelectedItem("");

        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN
                        || e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    String text = editor.getText();
                    comboBox.removeAllItems();

                    if (text.isEmpty()) {
                        for (String item : originalItems) {
                            comboBox.addItem(item);
                        }
                        comboBox.hidePopup();
                    } else {
                        boolean hasSuggestion = false;
                        for (String item : originalItems) {
                            // Tìm kiếm không phân biệt hoa thường
                            if (item.toLowerCase().contains(text.toLowerCase())) {
                                comboBox.addItem(item);
                                hasSuggestion = true;
                            }
                        }
                        if (hasSuggestion) {
                            comboBox.showPopup();
                        } else {
                            comboBox.hidePopup();
                        }
                    }
                    editor.setText(text); // Giữ lại chữ đang gõ
                });
            }
        });
    }

    // ==========================================
    // UI HELPERS
    // ==========================================
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(textDark);
        return lbl;
    }

    private JTextField createTextField(String placeholder) {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(200, 40));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.putClientProperty("JTextField.placeholderText", placeholder);
        txt.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(borderGray, 8), new EmptyBorder(5, 10, 5, 10)
        ));
        return txt;
    }

    private JButton createCustomButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setPreferredSize(new Dimension(100, 38));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setOpaque(false);
            }
        });
        return btn;
    }

    class RoundedPanel extends JPanel {

        private int radius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class RoundBorder implements javax.swing.border.Border {

        private Color color;
        private int radius;

        public RoundBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    // ==========================================
    // LOGIC & SỰ KIỆN 
    // ==========================================
    private void initEvents() {
        // Đổ dữ liệu từ Bảng lên Form khi click
        tblCustomers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = tblCustomers.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tblCustomers.getValueAt(row, 0).toString());
                    txtName.setText(tblCustomers.getValueAt(row, 1).toString());
                    txtPhone.setText(tblCustomers.getValueAt(row, 2).toString());
                    txtEmail.setText(tblCustomers.getValueAt(row, 3).toString());
                    txtAddress.setText(tblCustomers.getValueAt(row, 4).toString());
                }
            }
        });

        // Xóa form và reset bảng
        btnClear.addActionListener(e -> {
            txtId.setText("");
            txtName.setText("");
            txtPhone.setText("");
            txtEmail.setText("");
            txtAddress.setText("");
            ((JTextField) cbSearch.getEditor().getEditorComponent()).setText(""); // Xóa trắng ô tìm kiếm
            tblCustomers.clearSelection();
            loadCustomerData(); // Load lại toàn bộ dữ liệu
        });

        // Xử lý Tìm kiếm
        btnSearch.addActionListener(e -> {
            JTextField editor = (JTextField) cbSearch.getEditor().getEditorComponent();
            String keyword = editor.getText().trim().toLowerCase();

            // Xử lý chuỗi nếu người dùng chọn từ danh sách gợi ý (bỏ phần SĐT)
            if (keyword.contains(" - ")) {
                keyword = keyword.split(" - ")[0].trim();
            }

            searchAndFilterTable(keyword);
        });
    }

    // Hàm lọc dữ liệu trên bảng theo Tên hoặc SĐT
    private void searchAndFilterTable(String keyword) {
        tableModel.setRowCount(0);
        try {
            java.util.List<model.order.Customer> list = business.sql.sales_order.CustomersSql.getInstance().getCustomersWithOrders();
            for (model.order.Customer c : list) {
                String name = c.getCustomerName() != null ? c.getCustomerName().toLowerCase() : "";
                String phone = c.getPhone() != null ? c.getPhone() : "";

                // Nếu từ khóa rỗng hoặc khớp tên hoặc khớp SĐT thì đưa vào bảng
                if (keyword.isEmpty() || name.contains(keyword) || phone.contains(keyword)) {
                    String id = c.getCustomerId() != null ? c.getCustomerId() : "";
                    String cName = c.getCustomerName() != null ? c.getCustomerName() : "";
                    String email = c.getEmail() != null ? c.getEmail() : "";
                    String address = c.getAddress() != null ? c.getAddress() : "";

                    tableModel.addRow(new Object[]{id, cName, phone, email, address});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCustomerData() {
        tableModel.setRowCount(0);
        try {
            java.util.List<model.order.Customer> list = business.sql.sales_order.CustomersSql.getInstance().getCustomersWithOrders();

            for (model.order.Customer c : list) {
                String id = c.getCustomerId() != null ? c.getCustomerId() : "";
                String name = c.getCustomerName() != null ? c.getCustomerName() : "";
                String phone = c.getPhone() != null ? c.getPhone() : "";
                String email = c.getEmail() != null ? c.getEmail() : "";
                String address = c.getAddress() != null ? c.getAddress() : "";

                tableModel.addRow(new Object[]{id, name, phone, email, address});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
