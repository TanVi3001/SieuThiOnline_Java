package view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.order.Customer;
import view.components.IconHelper; 

public class CustomerView extends JPanel {

    // --- BẢNG MÀU UI ---
    private final Color bgLight = new Color(244, 246, 250);
    private final Color cardWhite = Color.WHITE;
    private final Color primaryBlue = new Color(54, 92, 245);    
    private final Color textDark = new Color(43, 54, 116);
    private final Color textGray = new Color(163, 174, 208);
    private final Color borderGray = new Color(230, 235, 241);

    // --- KHAI BÁO UI COMPONENTS ---
    private JTextField txtId, txtName, txtPhone, txtEmail, txtAddress;
    private JComboBox<String> cbSearch;
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
            List<Customer> list = business.sql.sales_order.CustomersSql.getInstance().getCustomersWithOrders();
            for (Customer c : list) {
                if (c.getCustomerName() != null) {
                    customerSearchList.add(c.getCustomerName() + " - " + (c.getPhone() != null ? c.getPhone() : "N/A"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void initUI() {
        // ==========================================
        // 1. HEADER (Tiêu đề + Ô Tìm kiếm)
        // ==========================================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel lblTitle = new JLabel("Danh Mục Khách Hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(textDark);
        JLabel lblSub = new JLabel("Quản lý thông tin liên hệ và lịch sử mua hàng của khách");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(textGray);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);

        // --- Ô TÌM KIẾM DÀI ---
        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        toolPanel.setOpaque(false);

        cbSearch = new JComboBox<>();
        styleSearchBox(cbSearch);
        setupAutoComplete(cbSearch, customerSearchList);

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
        // 2. CENTER (Thẻ Form trái + Thẻ Bảng phải)
        // ==========================================
        JPanel centerPanel = new JPanel(new BorderLayout(25, 0));
        centerPanel.setOpaque(false);

        // FORM BÊN TRÁI
        RoundedPanel formCard = new RoundedPanel(20, cardWhite);
        formCard.setPreferredSize(new Dimension(360, 0));
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        txtId = createTextField("Mã tự động..."); txtId.setEnabled(false);
        txtName = createTextField("Họ và tên khách hàng...");
        txtPhone = createTextField("Số điện thoại...");
        txtEmail = createTextField("Địa chỉ Email...");
        txtAddress = createTextField("Địa chỉ thường trú...");

        int y = 0;
        formCard.add(createLabel("Mã khách hàng"), addGbc(gbc, y++, 5)); formCard.add(txtId, addGbc(gbc, y++, 15));
        formCard.add(createLabel("Tên khách hàng (*)"), addGbc(gbc, y++, 5)); formCard.add(txtName, addGbc(gbc, y++, 15));
        formCard.add(createLabel("Số điện thoại (*)"), addGbc(gbc, y++, 5)); formCard.add(txtPhone, addGbc(gbc, y++, 15));
        formCard.add(createLabel("Email"), addGbc(gbc, y++, 5)); formCard.add(txtEmail, addGbc(gbc, y++, 15));
        formCard.add(createLabel("Địa chỉ"), addGbc(gbc, y++, 5)); formCard.add(txtAddress, addGbc(gbc, y++, 25));

        JPanel btnGrid = new JPanel(new GridLayout(2, 2, 12, 12));
        btnGrid.setOpaque(false);
        // GRID CHỨA 4 NÚT CHỨC NĂNG BÊN DƯỚI FORM
        btnAdd = createCustomButton("Thêm mới", primaryBlue, Color.WHITE, IconHelper.add(20));
        btnUpdate = createCustomButton("Cập nhật", new Color(0, 168, 140), Color.WHITE, IconHelper.edit(20));
        btnDelete = createCustomButton("Xóa bỏ", new Color(220, 53, 69), Color.WHITE, IconHelper.delete(20));
        btnClear = createCustomButton("Làm mới", new Color(165, 177, 194), Color.WHITE, IconHelper.refresh(20));
        
        btnGrid.add(btnAdd); btnGrid.add(btnUpdate); btnGrid.add(btnDelete); btnGrid.add(btnClear);
        gbc.gridy = y++; formCard.add(btnGrid, gbc);

        // BẢNG BÊN PHẢI
        RoundedPanel tableCard = new RoundedPanel(20, cardWhite);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new Object[]{"Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Email", "Địa chỉ"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblCustomers = new JTable(tableModel);
        setupTableStyle();

        JScrollPane scrollPane = new JScrollPane(tblCustomers);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(formCard, BorderLayout.WEST);
        centerPanel.add(tableCard, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    // Chỉnh style bảng
    private void setupTableStyle() {
        tblCustomers.setRowHeight(40);
        tblCustomers.setShowVerticalLines(false);
        tblCustomers.setSelectionBackground(new Color(237, 242, 255));
        tblCustomers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(0);
        for (int i = 0; i < tblCustomers.getColumnCount(); i++) {
            tblCustomers.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JButton createCustomButton(String t, Color bg, Color fg, ImageIcon icon) {
        JButton btn = new JButton(t);
        if (icon != null) btn.setIcon(new ImageIcon(icon.getImage().getScaledInstance(18, 18, 1)));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg); btn.setBackground(bg);
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false); btn.setBorderPainted(false); btn.setContentAreaFilled(false);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override public void paint(Graphics g, JComponent c) {
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

    private void styleSearchBox(JComboBox<String> cb) {
        cb.setEditable(true); cb.setBorder(null); cb.setBackground(Color.WHITE);
        cb.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override protected JButton createArrowButton() { return new JButton() {{ setPreferredSize(new Dimension(0,0)); }}; }
        });
        JTextField editor = (JTextField) cb.getEditor().getEditorComponent();
        editor.setBorder(new EmptyBorder(0,5,0,5));
        editor.putClientProperty("JTextField.placeholderText", "Nhập tên hoặc SĐT khách hàng...");
    }

    private void setupAutoComplete(JComboBox<String> cb, List<String> items) {
        cb.removeAllItems();
        for (String i : items) cb.addItem(i);
        cb.setSelectedItem("");
        
        JTextField editor = (JTextField) cb.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) btnSearch.doClick();
            }
        });
    }

    private GridBagConstraints addGbc(GridBagConstraints gbc, int y, int b) {
        gbc.gridy = y; gbc.insets = new Insets(0, 0, b, 0); return gbc;
    }

    // ==========================================
    // UI HELPERS
    // ==========================================
    private JLabel createLabel(String t) {
        JLabel l = new JLabel(t); l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(textDark); return l;
    }

    private JTextField createTextField(String p) {
        JTextField t = new JTextField();
        t.setPreferredSize(new Dimension(200, 40));
        t.putClientProperty("JTextField.placeholderText", p);
        t.setBorder(BorderFactory.createCompoundBorder(new RoundBorder(borderGray, 10), new EmptyBorder(5, 12, 5, 12)));
        return t;
    }

    // ==========================================
    // LOGIC & SỰ KIỆN 
    // ==========================================
    private void initEvents() {
        tblCustomers.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent evt) {
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
            txtId.setText(""); txtName.setText(""); txtPhone.setText("");
            txtEmail.setText(""); txtAddress.setText("");
            ((JTextField) cbSearch.getEditor().getEditorComponent()).setText("");
            tblCustomers.clearSelection();
            loadCustomerData(); // Load lại toàn bộ dữ liệu
        });

        // Xử lý Tìm kiếm
        btnSearch.addActionListener(e -> {
            String keyword = ((JTextField) cbSearch.getEditor().getEditorComponent()).getText().trim().toLowerCase();
            
            // Xử lý chuỗi nếu người dùng chọn từ danh sách gợi ý (bỏ phần SĐT)
            if (keyword.contains(" - ")) keyword = keyword.split(" - ")[0].trim();
            updateTableByFilter(keyword);
        });
    }

    private void loadCustomerData() { updateTableByFilter(""); }

    private void updateTableByFilter(String keyword) {
        tableModel.setRowCount(0);
        try {
            List<Customer> list = business.sql.sales_order.CustomersSql.getInstance().getCustomersWithOrders();
            for (Customer c : list) {
                String name = (c.getCustomerName() != null) ? c.getCustomerName() : "";
                String phone = (c.getPhone() != null) ? c.getPhone() : "";
                if (keyword.isEmpty() || name.toLowerCase().contains(keyword) || phone.contains(keyword)) {
                    tableModel.addRow(new Object[]{c.getCustomerId(), name, phone, c.getEmail(), c.getAddress()});
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- INNER CLASSES FOR UI ---
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