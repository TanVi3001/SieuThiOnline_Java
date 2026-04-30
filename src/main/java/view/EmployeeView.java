package view;

import business.sql.hr_kpi.EmployeeSql;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.employee.Employee;

public class EmployeeView extends JPanel {

    // --- CẤU HÌNH MÀU SẮC CHUẨN MODERN UI ---
    private final Color bgLight = new Color(244, 246, 250);
    private final Color cardWhite = Color.WHITE;
    private final Color primaryBlue = new Color(67, 97, 238);
    private final Color textDark = new Color(43, 54, 116);
    private final Color textGray = new Color(163, 174, 208);
    private final Color borderGray = new Color(230, 235, 241);

    // --- KHAI BÁO UI COMPONENTS ---
    private JTextField txtId, txtName, txtPhone, txtEmail, txtRole, txtSearch;
    private JRadioButton rdoMale, rdoFemale;
    private ButtonGroup btngGender;
    private JTable tblEmployees;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    
    private final EmployeeSql employeeSql = new EmployeeSql();

    public EmployeeView() {
        // KIỂM TRA PHÂN QUYỀN ĐẦU TIÊN
        if (!business.service.AuthorizationService.canAccessEmployeeManagement()) {
            showAccessDenied();
            return;
        }

        setLayout(new BorderLayout(20, 20));
        setBackground(bgLight);
        setBorder(new EmptyBorder(20, 30, 20, 30)); // Padding cho toàn màn hình

        initUI();
        initEvents();
        loadDataToTable();
    }

    private void showAccessDenied() {
        setLayout(new BorderLayout());
        setBackground(bgLight);
        JLabel message = new JLabel("Bạn không có quyền truy cập chức năng quản lý nhân viên.", SwingConstants.CENTER);
        message.setFont(new Font("Segoe UI", Font.BOLD, 18));
        message.setForeground(new Color(220, 53, 69));
        add(message, BorderLayout.CENTER);
    }

    private void initUI() {
        // ==========================================
        // 1. HEADER (Tiêu đề + Ô Tìm kiếm)
        // ==========================================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel lblTitle = new JLabel("Quản Lý Nhân Viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(textDark);
        JLabel lblSub = new JLabel("Cập nhật thông tin, chức vụ và phân quyền");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(textGray);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);

        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        toolPanel.setOpaque(false);
        txtSearch = createTextField("Nhập tên nhân viên...");
        txtSearch.setPreferredSize(new Dimension(280, 40));
        
        btnSearch = createCustomButton("Tìm kiếm", primaryBlue, Color.WHITE);
        toolPanel.add(txtSearch);
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
        txtId.setEnabled(false); // Mã NV tự sinh
        txtName = createTextField("Nhập tên nhân viên...");
        txtPhone = createTextField("Nhập số điện thoại...");
        txtEmail = createTextField("Nhập email...");
        txtRole = createTextField("Nhập Role ID (VD: R001)...");

        // Cấu hình Giới tính
        rdoMale = new JRadioButton("Nam");
        rdoFemale = new JRadioButton("Nữ");
        rdoMale.setOpaque(false); rdoFemale.setOpaque(false);
        rdoMale.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rdoFemale.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btngGender = new ButtonGroup();
        btngGender.add(rdoMale);
        btngGender.add(rdoFemale);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        genderPanel.setOpaque(false);
        genderPanel.add(rdoMale);
        genderPanel.add(rdoFemale);

        int y = 0;
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0); formCard.add(createLabel("Mã nhân viên"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 15, 0); formCard.add(txtId, gbc);

        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0); formCard.add(createLabel("Tên nhân viên (*)"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 15, 0); formCard.add(txtName, gbc);

        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0); formCard.add(createLabel("Số điện thoại (*)"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 15, 0); formCard.add(txtPhone, gbc);

        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0); formCard.add(createLabel("Email (*)"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 15, 0); formCard.add(txtEmail, gbc);

        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0); formCard.add(createLabel("Chức vụ (Role ID) (*)"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 15, 0); formCard.add(txtRole, gbc);

        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0); formCard.add(createLabel("Giới tính (*)"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 30, 0); formCard.add(genderPanel, gbc);

        // GRID CHỨA 4 NÚT CHỨC NĂNG NGAY DƯỚI FORM
        btnAdd = createCustomButton("Thêm", primaryBlue, Color.WHITE);
        btnUpdate = createCustomButton("Cập nhật", new Color(0, 168, 140), Color.WHITE); // Màu Teal
        btnDelete = createCustomButton("Xóa", new Color(220, 53, 69), Color.WHITE); // Đỏ
        btnClear = createCustomButton("Làm mới", new Color(230, 180, 50), textDark); // Vàng

        JPanel btnGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        btnGrid.setOpaque(false);
        btnGrid.add(btnAdd);
        btnGrid.add(btnUpdate);
        btnGrid.add(btnDelete);
        btnGrid.add(btnClear);

        gbc.gridy = y++; formCard.add(btnGrid, gbc);
        centerPanel.add(formCard, BorderLayout.WEST);

        // --- RIGHT TABLE (Thẻ chứa JTable bo góc bên phải) ---
        RoundedPanel tableCard = new RoundedPanel(20, cardWhite);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new Object[]{"Mã NV", "Tên nhân viên", "Số điện thoại", "Email", "Chức vụ", "Giới tính"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblEmployees = new JTable(tableModel);
        setupTableStyle();

        JScrollPane scrollPane = new JScrollPane(tblEmployees);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(tableCard, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
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
        txt.setPreferredSize(new Dimension(200, 38));
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
        return btn;
    }

    private void setupTableStyle() {
        tblEmployees.setRowHeight(35);
        tblEmployees.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblEmployees.setShowVerticalLines(false);
        tblEmployees.setSelectionBackground(new Color(237, 242, 255));
        tblEmployees.setSelectionForeground(textDark);
        tblEmployees.getTableHeader().setReorderingAllowed(false);

        // Header Style đồng bộ
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(textDark);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        for (int i = 0; i < tblEmployees.getColumnModel().getColumnCount(); i++) {
            tblEmployees.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Căn giữa dữ liệu các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblEmployees.getColumnCount(); i++) {
            tblEmployees.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
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
            super.paintComponent(g);
            g2.dispose();
        }
    }

    class RoundBorder implements javax.swing.border.Border {
        private Color color;
        private int radius;

        public RoundBorder(Color color, int radius) { this.color = color; this.radius = radius; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(1, 1, 1, 1); }
        @Override public boolean isBorderOpaque() { return false; }
    }

    // ==========================================
    // LOGIC & SỰ KIỆN 
    // ==========================================
    private void initEvents() {
        tblEmployees.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = tblEmployees.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(String.valueOf(tblEmployees.getValueAt(row, 0)));
                    txtName.setText(String.valueOf(tblEmployees.getValueAt(row, 1)));
                    txtPhone.setText(String.valueOf(tblEmployees.getValueAt(row, 2)));
                    txtEmail.setText(String.valueOf(tblEmployees.getValueAt(row, 3)));
                    txtRole.setText(String.valueOf(tblEmployees.getValueAt(row, 4)));

                    String gender = String.valueOf(tblEmployees.getValueAt(row, 5));
                    rdoMale.setSelected("Nam".equalsIgnoreCase(gender));
                    rdoFemale.setSelected("Nữ".equalsIgnoreCase(gender));
                }
            }
        });

        btnAdd.addActionListener(e -> {
            Employee emp = getEmployeeFromForm();
            if (emp == null) return;
            emp.setEmployeeId("EMP" + System.currentTimeMillis());

            if (employeeSql.insert(emp) > 0) {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                loadDataToTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            String id = txtId.getText();
            if (id.isEmpty() || id.startsWith("Mã")) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên trong bảng để cập nhật!");
                return;
            }
            Employee emp = getEmployeeFromForm();
            if (emp == null) return;
            emp.setEmployeeId(id);

            if (employeeSql.update(emp) > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadDataToTable();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            String id = txtId.getText();
            if (id.isEmpty() || id.startsWith("Mã")) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên trong bảng để xóa!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (employeeSql.delete(id) > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadDataToTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(e -> clearForm());

        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            tableModel.setRowCount(0);
            java.util.List<Employee> list = employeeSql.search(keyword);
            for (Employee emp : list) {
                tableModel.addRow(new Object[]{
                    emp.getEmployeeId(), emp.getEmployeeName(), emp.getPhone(), emp.getEmail(), emp.getRole(), emp.getGender()
                });
            }
        });
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        ArrayList<Employee> list = employeeSql.selectAll();
        for (Employee e : list) {
            tableModel.addRow(new Object[]{
                e.getEmployeeId(), e.getEmployeeName(), e.getPhone(), e.getEmail(), e.getRole(), e.getGender()
            });
        }
    }

    private Employee getEmployeeFromForm() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String role = txtRole.getText().trim();
        String gender = rdoMale.isSelected() ? "Nam" : (rdoFemale.isSelected() ? "Nữ" : "");

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || role.isEmpty() || gender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ các trường bắt buộc (*)");
            return null;
        }

        Employee e = new Employee();
        e.setEmployeeName(name);
        e.setPhone(phone);
        e.setEmail(email);
        e.setRole(role);
        e.setRoleId(role); // Set Role ID theo logic cũ
        e.setGender(gender);
        return e;
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtRole.setText("");
        btngGender.clearSelection();
        tblEmployees.clearSelection();
    }
}