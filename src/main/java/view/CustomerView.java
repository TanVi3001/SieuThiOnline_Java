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
import view.components.IconHelper;

public class CustomerView extends JPanel {

    // ==========================================
    // CẤU HÌNH MÀU SẮC CHUẨN MODERN UI
    // (Giống hệt ProductView để nhất quán toàn hệ thống)
    // ==========================================
    private final Color bgLight    = new Color(244, 246, 250);  // Nền xám nhạt toàn màn hình
    private final Color cardWhite  = Color.WHITE;                // Nền thẻ trắng
    private final Color primaryBlue = new Color(67, 97, 238);   // Màu chủ đạo xanh
    private final Color textDark   = new Color(43, 54, 116);    // Màu chữ đậm
    private final Color textGray   = new Color(163, 174, 208);  // Màu chữ phụ xám
    private final Color borderGray = new Color(230, 235, 241);  // Màu viền ô nhập liệu

    // ==========================================
    // KHAI BÁO CÁC THÀNH PHẦN GIAO DIỆN
    // ==========================================
    private JTextField txtId, txtName, txtPhone, txtEmail, txtAddress;
    private JComboBox<String> cbSearch; // Ô tìm kiếm dạng ComboBox có auto-complete
    private JTable tblCustomers;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;

    // Danh sách gợi ý tự động cho ô tìm kiếm (Tên - SĐT)
    private List<String> customerSearchList = new ArrayList<>();

    // ==========================================
    // KHỞI TẠO VIEW
    // ==========================================
    public CustomerView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(bgLight);
        setBorder(new EmptyBorder(20, 30, 20, 30)); // Padding tổng thể cho màn hình

        loadAutoCompleteData(); // Nạp danh sách gợi ý tìm kiếm trước
        initUI();               // Xây dựng giao diện
        initEvents();           // Gắn sự kiện cho các nút
        loadCustomerData();     // Đổ dữ liệu ban đầu lên bảng
    }

    // ==========================================
    // NẠP DỮ LIỆU GỢI Ý CHO AUTO-COMPLETE
    // ==========================================
    private void loadAutoCompleteData() {
        customerSearchList.clear();
        try {
            java.util.List<model.order.Customer> list =
                    business.sql.sales_order.CustomersSql.getInstance().getCustomersWithOrders();
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

    // ==========================================
    // KHỞI TẠO TOÀN BỘ GIAO DIỆN
    // ==========================================
    private void initUI() {

        // ==========================================
        // PHẦN 1: HEADER (Tiêu đề + Thanh công cụ)
        // ==========================================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        // --- Tiêu đề bên trái ---
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

        // --- Thanh công cụ bên phải (Search + Nút) ---
        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        toolPanel.setOpaque(false);

        // ComboBox tìm kiếm (bỏ border mặc định để đặt vào khung riêng bên dưới)
        cbSearch = new JComboBox<>();
        styleSearchBox(cbSearch);
        setupAutoComplete(cbSearch, customerSearchList);

        // --- KHUNG TÌM KIẾM:  ---
        JPanel searchFieldWrapper = new JPanel(new BorderLayout(5, 0));
        searchFieldWrapper.setBackground(Color.WHITE);
        searchFieldWrapper.setPreferredSize(new Dimension(450, 45)); 
        searchFieldWrapper.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(new Color(220, 225, 235), 25),
            new EmptyBorder(0, 15, 0, 15)
        ));

        JLabel searchIconLabel = new JLabel(IconHelper.search(16));
        searchFieldWrapper.add(searchIconLabel, BorderLayout.WEST);
        searchFieldWrapper.add(cbSearch, BorderLayout.CENTER);

        btnSearch = createCustomButton("Tìm kiếm", primaryBlue, Color.WHITE, null);
        toolPanel.add(searchFieldWrapper);
        toolPanel.add(btnSearch);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);  
        headerPanel.add(toolPanel, BorderLayout.EAST);   
        add(headerPanel, BorderLayout.NORTH);
        
        // ==========================================
        // PHẦN 2: CENTER (Thẻ Form trái + Thẻ Bảng phải)
        // ==========================================
        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setOpaque(false);

        // ==========================================
        // FORM BÊN TRÁI (Thẻ trắng bo góc)
        // ==========================================
        RoundedPanel formCard = new RoundedPanel(20, cardWhite);
        formCard.setPreferredSize(new Dimension(350, 0));
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;

        // Khởi tạo các ô nhập liệu
        txtId      = createTextField("Mã tự động...");
        txtId.setEnabled(false); // Mã KH do hệ thống tự sinh, không cho sửa
        txtName    = createTextField("Nhập tên khách hàng...");
        txtPhone   = createTextField("Nhập số điện thoại...");
        txtEmail   = createTextField("Nhập email...");
        txtAddress = createTextField("Nhập địa chỉ...");

        // Thêm lần lượt Label rồi TextField vào form (insets 5 cho label, 15 cho field)
        int y = 0;

        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0);
        formCard.add(createLabel("Mã khách hàng"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 15, 0);
        formCard.add(txtId, gbc);

        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0);
        formCard.add(createLabel("Tên khách hàng (*)"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 15, 0);
        formCard.add(txtName, gbc);

        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0);
        formCard.add(createLabel("Số điện thoại (*)"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 15, 0);
        formCard.add(txtPhone, gbc);

        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0);
        formCard.add(createLabel("Email"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 15, 0);
        formCard.add(txtEmail, gbc);

        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0);
        formCard.add(createLabel("Địa chỉ"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 30, 0);
        formCard.add(txtAddress, gbc);

        // --- CÁC NÚT THAO TÁC CÓ ICON (giống hệt ProductView) ---
        btnAdd    = createCustomButton("Thêm",     primaryBlue,            Color.WHITE, IconHelper.add(20));
        btnUpdate = createCustomButton("Cập nhật", new Color(255, 153, 0), Color.BLACK, IconHelper.edit(20));
        btnDelete = createCustomButton("Xóa",      new Color(220, 53, 69), Color.WHITE, IconHelper.delete(20));
        btnClear  = createCustomButton("Làm mới",  new Color(165, 177, 194), Color.WHITE, IconHelper.refresh(20));

        // Lưới 2x2 chứa 4 nút (gap 12px giống ProductView)
        JPanel btnGrid = new JPanel(new GridLayout(2, 2, 12, 12));
        btnGrid.setOpaque(false);
        btnGrid.add(btnAdd);
        btnGrid.add(btnUpdate);
        btnGrid.add(btnDelete);
        btnGrid.add(btnClear);

        gbc.gridy = y++;
        gbc.insets = new Insets(0, 0, 0, 0);
        formCard.add(btnGrid, gbc);

        centerPanel.add(formCard, BorderLayout.WEST);

        // ==========================================
        // BẢNG BÊN PHẢI (Thẻ trắng bo góc chứa JTable)
        // ==========================================
        RoundedPanel tableCard = new RoundedPanel(20, cardWhite);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Model bảng — không cho chỉnh sửa trực tiếp trên ô
        tableModel = new DefaultTableModel(
                new Object[]{"Mã KH", "Tên khách hàng", "Số điện thoại", "Email", "Địa chỉ"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblCustomers = new JTable(tableModel);

        // Style bảng — nhất quán với ProductView
        tblCustomers.setRowHeight(35);
        tblCustomers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblCustomers.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblCustomers.getTableHeader().setBackground(bgLight);
        tblCustomers.getTableHeader().setReorderingAllowed(false); // Không cho kéo thả cột
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
    // SỰ KIỆN CHO CÁC NÚT VÀ BẢNG
    // ==========================================
    private void initEvents() {

        // Click vào hàng bảng → đổ dữ liệu lên form
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

        // Nút Làm mới: xóa form + ô search + load lại toàn bộ dữ liệu
        btnClear.addActionListener(e -> {
            txtId.setText("");
            txtName.setText("");
            txtPhone.setText("");
            txtEmail.setText("");
            txtAddress.setText("");
            ((JTextField) cbSearch.getEditor().getEditorComponent()).setText("");
            tblCustomers.clearSelection();
            loadCustomerData();
        });

        // Nút Tìm kiếm: lọc bảng theo từ khóa (tên hoặc SĐT)
        btnSearch.addActionListener(e -> {
            JTextField editor = (JTextField) cbSearch.getEditor().getEditorComponent();
            String keyword = editor.getText().trim().toLowerCase();

            // Nếu người dùng chọn từ gợi ý (dạng "Tên - SĐT") thì chỉ lấy phần tên
            if (keyword.contains(" - ")) {
                keyword = keyword.split(" - ")[0].trim();
            }

            searchAndFilterTable(keyword);
        });
    }

    // ==========================================
    // LỌC BẢNG THEO TỪ KHÓA (Tên hoặc SĐT)
    // ==========================================
    private void searchAndFilterTable(String keyword) {
        tableModel.setRowCount(0);
        try {
            java.util.List<model.order.Customer> list =
                    business.sql.sales_order.CustomersSql.getInstance().getCustomersWithOrders();
            for (model.order.Customer c : list) {
                String name  = c.getCustomerName() != null ? c.getCustomerName().toLowerCase() : "";
                String phone = c.getPhone() != null ? c.getPhone() : "";

                // Đưa vào bảng nếu từ khóa rỗng hoặc khớp tên/SĐT
                if (keyword.isEmpty() || name.contains(keyword) || phone.contains(keyword)) {
                    String id      = c.getCustomerId()   != null ? c.getCustomerId()   : "";
                    String cName   = c.getCustomerName() != null ? c.getCustomerName() : "";
                    String email   = c.getEmail()        != null ? c.getEmail()        : "";
                    String address = c.getAddress()      != null ? c.getAddress()      : "";
                    tableModel.addRow(new Object[]{id, cName, phone, email, address});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // NẠP TOÀN BỘ DỮ LIỆU KHÁCH HÀNG LÊN BẢNG
    // ==========================================
    private void loadCustomerData() {
        tableModel.setRowCount(0);
        try {
            java.util.List<model.order.Customer> list =
                    business.sql.sales_order.CustomersSql.getInstance().getCustomersWithOrders();
            for (model.order.Customer c : list) {
                String id      = c.getCustomerId()   != null ? c.getCustomerId()   : "";
                String name    = c.getCustomerName() != null ? c.getCustomerName() : "";
                String phone   = c.getPhone()        != null ? c.getPhone()        : "";
                String email   = c.getEmail()        != null ? c.getEmail()        : "";
                String address = c.getAddress()      != null ? c.getAddress()      : "";
                tableModel.addRow(new Object[]{id, name, phone, email, address});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // AUTO-COMPLETE: Style ô tìm kiếm dạng phẳng
    // (Bỏ viền + mũi tên dropdown — đặt trong khung riêng)
    // ==========================================
    private void styleSearchBox(JComboBox<String> cb) {
        cb.setEditable(true); cb.setBorder(null); cb.setBackground(Color.WHITE);
        
        ((JTextField)cb.getEditor().getEditorComponent()).setBorder(new EmptyBorder(0,5,0,5));
    }

    // ==========================================
    // AUTO-COMPLETE: Lọc gợi ý khi người dùng gõ
    // ==========================================
    private void setupAutoComplete(JComboBox<String> comboBox, List<String> originalItems) {
        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();

        // Nạp toàn bộ danh sách ban đầu vào ComboBox
        for (String item : originalItems) {
            comboBox.addItem(item);
        }
        comboBox.setSelectedItem("");

        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Bỏ qua các phím điều hướng và xác nhận
                if (e.getKeyCode() == KeyEvent.VK_UP   || e.getKeyCode() == KeyEvent.VK_DOWN
                 || e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    String text = editor.getText();
                    comboBox.removeAllItems();

                    if (text.isEmpty()) {
                        // Nếu ô rỗng, hiện lại toàn bộ danh sách và đóng popup
                        for (String item : originalItems) {
                            comboBox.addItem(item);
                        }
                        comboBox.hidePopup();
                    } else {
                        // Lọc các mục có chứa từ khóa (không phân biệt hoa/thường)
                        boolean hasSuggestion = false;
                        for (String item : originalItems) {
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
                    editor.setText(text); // Giữ lại chữ đang gõ sau khi rebuild list
                });
            }
        });
    }

    // ==========================================
    // UI HELPERS — CÁC HÀM TẠO COMPONENT
    // ==========================================

    /** Tạo JLabel chuẩn: font Segoe UI Bold 13, màu textDark */
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(textDark);
        return lbl;
    }

    /** Tạo JTextField chuẩn: placeholder, viền bo tròn 8px, font 14 */
    private JTextField createTextField(String placeholder) {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(200, 40));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.putClientProperty("JTextField.placeholderText", placeholder);
        txt.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(borderGray, 8),  // Viền bo tròn nhẹ
                new EmptyBorder(5, 10, 5, 10)    // Padding trong
        ));
        return txt;
    }

    /**
     * Tạo JButton chuẩn theo phong cách ProductView:
     * - Nền bo tròn 25px vẽ bằng BasicButtonUI (không dùng paintComponent override)
     * - Hỗ trợ icon bên trái, khoảng cách icon-text là 8px
     * - Kích thước mặc định 130x45, cursor tay, không border mặc định
     */
    private JButton createCustomButton(String text, Color bg, Color fg, ImageIcon icon) {
        JButton btn = new JButton(text);

        // Gắn icon nếu có, scale về 18x18 cho đồng đều
        if (icon != null) {
            btn.setIcon(new ImageIcon(icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
            btn.setIconTextGap(8);
        }

        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setPreferredSize(new Dimension(130, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false); // Tắt nền mặc định để tự vẽ

        // Vẽ nền bo tròn 25px bằng BasicButtonUI — cách dùng của ProductView
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 25, 25);
                super.paint(g2, c);
                g2.dispose();
            }
        });

        return btn;
    }

    // ==========================================
    // INNER CLASS: RoundedPanel
    // Panel tự vẽ nền bo góc (dùng cho formCard và tableCard)
    // ==========================================
    class RoundedPanel extends JPanel {

        private int radius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false); // Phải false để paintComponent hoạt động đúng
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g); // Vẽ các component con lên trên
        }
    }

    // ==========================================
    // INNER CLASS: RoundBorder
    // Border tự vẽ viền bo tròn (dùng cho TextField và SearchWrapper)
    // ==========================================
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
            g2.setStroke(new BasicStroke(1.2f)); // Nét vẽ mỏng, mịn
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
}