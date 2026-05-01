package view;

import business.sql.hr_kpi.EmployeeSql;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
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
    private JTextField txtId, txtName, txtPhone, txtEmail;
    private JComboBox<String> cbSearch;

    private JRadioButton rdoMale, rdoFemale;
    private ButtonGroup btngGender;
    private JTable tblEmployees;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;

    private final EmployeeSql employeeSql = new EmployeeSql();
    private List<String> employeeNameList = new ArrayList<>();

    public EmployeeView() {
        if (!business.service.AuthorizationService.canAccessEmployeeManagement()) {
            showAccessDenied();
            return;
        }

        setLayout(new BorderLayout(20, 20));
        setBackground(bgLight);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        loadAutoCompleteData();
        initUI();
        initEvents();
        loadDataToTable();
    }

    private void loadAutoCompleteData() {
        employeeNameList.clear();
        try {
            List<Employee> list = employeeSql.selectAll();
            for (Employee e : list) {
                if (e.getEmployeeName() != null && !e.getEmployeeName().isEmpty()) {
                    employeeNameList.add(e.getEmployeeName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel lblTitle = new JLabel("Hồ Sơ Nhân Viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(textDark);
        JLabel lblSub = new JLabel("Cập nhật thông tin lý lịch cá nhân (Việc phân quyền do Admin quản lý)");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(textGray);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);

        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        toolPanel.setOpaque(false);

        cbSearch = new JComboBox<>();
        styleComboBox(cbSearch, "Nhập tên nhân viên...");
        setupAutoComplete(cbSearch, employeeNameList);

        btnSearch = createCustomButton("Tìm kiếm", primaryBlue, Color.WHITE);
        toolPanel.add(cbSearch);
        toolPanel.add(btnSearch);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(toolPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setOpaque(false);

        // --- LEFT FORM ---
        RoundedPanel formCard = new RoundedPanel(20, cardWhite);
        formCard.setPreferredSize(new Dimension(350, 0));
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;

        txtId = createTextField("Mã tự động...");
        txtId.setEnabled(false);
        txtName = createTextField("Nhập tên nhân viên...");
        txtPhone = createTextField("Nhập số điện thoại...");
        txtEmail = createTextField("Nhập email...");

        rdoMale = new JRadioButton("Nam");
        rdoFemale = new JRadioButton("Nữ");
        rdoMale.setOpaque(false);
        rdoFemale.setOpaque(false);
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
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0); formCard.add(createLabel("Giới tính (*)"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 30, 0); formCard.add(genderPanel, gbc);

        btnAdd = createCustomButton("Thêm HS", primaryBlue, Color.WHITE);
        btnUpdate = createCustomButton("Cập nhật", new Color(0, 168, 140), Color.WHITE);
        btnDelete = createCustomButton("Xóa HS", new Color(220, 53, 69), Color.WHITE);
        btnClear = createCustomButton("Làm mới", new Color(230, 180, 50), textDark);

        JPanel btnGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        btnGrid.setOpaque(false);
        btnGrid.add(btnAdd);
        btnGrid.add(btnUpdate);
        btnGrid.add(btnDelete);
        btnGrid.add(btnClear);

        gbc.gridy = y++;
        formCard.add(btnGrid, gbc);
        centerPanel.add(formCard, BorderLayout.WEST);

        // --- RIGHT TABLE CẬP NHẬT 7 CỘT ---
        RoundedPanel tableCard = new RoundedPanel(20, cardWhite);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Thêm cột "Cấp tài khoản" vào vị trí số 4 (Index 4)
        tableModel = new DefaultTableModel(new Object[]{"Mã NV", "Tên nhân viên", "Số ĐT", "Email", "Cấp tài khoản", "Chức vụ", "Giới tính"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
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
                        for (String item : originalItems) comboBox.addItem(item);
                        comboBox.hidePopup();
                    } else {
                        boolean hasSuggestion = false;
                        for (String item : originalItems) {
                            if (item.toLowerCase().contains(text.toLowerCase())) {
                                comboBox.addItem(item);
                                hasSuggestion = true;
                            }
                        }
                        if (hasSuggestion) comboBox.showPopup();
                        else comboBox.hidePopup();
                    }
                    editor.setText(text);
                });
            }
        });
    }

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

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(textDark);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        for (int i = 0; i < tblEmployees.getColumnModel().getColumnCount(); i++) {
            tblEmployees.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);

                // Lấy Cấp tài khoản ở Index 4, Chức vụ ở Index 5
                String accStatus = String.valueOf(table.getModel().getValueAt(table.convertRowIndexToModel(row), 4));
                String role = String.valueOf(table.getModel().getValueAt(table.convertRowIndexToModel(row), 5));

                // 1. TÔ MÀU NỀN THEO QUYỀN LỰC
                if ("R_ADMIN_ALL".equals(role) || "Quản trị viên".equals(role)) {
                    setBackground(isSelected ? new Color(248, 215, 218) : new Color(255, 235, 238));
                    setForeground(new Color(220, 53, 69));
                    setFont(new Font("Segoe UI", Font.BOLD, 14));
                } else if ("R_STORE_MNG".equals(role) || "Quản lý cửa hàng".equals(role)) {
                    setBackground(isSelected ? new Color(212, 237, 218) : new Color(230, 245, 233));
                    setForeground(new Color(25, 135, 84)); 
                    setFont(new Font("Segoe UI", Font.BOLD, 14));
                } else {
                    setBackground(isSelected ? new Color(237, 242, 255) : Color.WHITE);
                    setForeground(isSelected ? textDark : Color.BLACK);
                    setFont(new Font("Segoe UI", Font.PLAIN, 14));
                }

                // 2. TÔ MÀU CHỮ RIÊNG CHO CỘT "CẤP TÀI KHOẢN" (Index 4)
                if (column == 4) {
                    if ("Chưa cấp".equals(accStatus)) {
                        setForeground(new Color(220, 53, 69)); // Đỏ cảnh báo
                        setFont(new Font("Segoe UI", Font.BOLD, 14));
                    } else if ("Đã cấp".equals(accStatus)) {
                        setForeground(new Color(25, 135, 84)); // Xanh lá oki
                        setFont(new Font("Segoe UI", Font.BOLD, 14));
                    }
                }

                return c;
            }
        };

        for (int i = 0; i < tblEmployees.getColumnCount(); i++) {
            tblEmployees.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
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
        public Insets getBorderInsets(Component c) { return new Insets(1, 1, 1, 1); }
        @Override
        public boolean isBorderOpaque() { return false; }
    }

    private int getRoleRank(String role) {
        if ("R_ADMIN_ALL".equals(role) || "Quản trị viên".equals(role)) return 1;
        if ("R_STORE_MNG".equals(role) || "Quản lý cửa hàng".equals(role)) return 2;
        return 3;
    }

    private void initEvents() {
        tblEmployees.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = tblEmployees.getSelectedRow();
                if (row >= 0) {
                    // Index 5 giờ là Chức vụ
                    String role = String.valueOf(tblEmployees.getValueAt(row, 5));

                    if ("R_ADMIN_ALL".equals(role) || "Quản trị viên".equals(role)) {
                        JOptionPane.showMessageDialog(EmployeeView.this,
                                "⚠️ Đây là tài khoản Quản trị viên cấp cao (Admin).\nBạn không có quyền xem hay thao tác trên hồ sơ này!",
                                "Cảnh báo bảo mật", JOptionPane.WARNING_MESSAGE);
                        tblEmployees.clearSelection();
                        clearForm();
                        return;
                    } else if ("R_STORE_MNG".equals(role) || "Quản lý cửa hàng".equals(role)) {
                        JOptionPane.showMessageDialog(EmployeeView.this,
                                "⚠️ Đây là hồ sơ Cửa hàng trưởng (Manager).\nBạn không thể can thiệp vào hồ sơ đồng cấp!",
                                "Cảnh báo bảo mật", JOptionPane.WARNING_MESSAGE);
                        tblEmployees.clearSelection();
                        clearForm();
                        return;
                    }

                    txtId.setText(String.valueOf(tblEmployees.getValueAt(row, 0)));
                    txtName.setText(String.valueOf(tblEmployees.getValueAt(row, 1)));
                    txtPhone.setText(String.valueOf(tblEmployees.getValueAt(row, 2)));
                    txtEmail.setText(String.valueOf(tblEmployees.getValueAt(row, 3)));

                    // Giới tính lùi xuống Index 6
                    String gender = String.valueOf(tblEmployees.getValueAt(row, 6));
                    rdoMale.setSelected("Nam".equalsIgnoreCase(gender));
                    rdoFemale.setSelected("Nữ".equalsIgnoreCase(gender));
                }
            }
        });

        btnAdd.addActionListener(e -> {
            Employee emp = getEmployeeFromForm();
            if (emp == null) return;
            
            emp.setEmployeeId("USR" + System.currentTimeMillis());

            if (employeeSql.insert(emp) > 0) {
                JOptionPane.showMessageDialog(this, "Tạo hồ sơ nhân viên thành công!\nVui lòng liên hệ Admin để cấp tài khoản đăng nhập.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                if (!employeeNameList.contains(emp.getEmployeeName())) {
                    employeeNameList.add(emp.getEmployeeName());
                }
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
                JOptionPane.showMessageDialog(this, "Cập nhật hồ sơ thành công!");
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
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa hồ sơ nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (employeeSql.delete(id) > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa hồ sơ thành công!");
                    loadDataToTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(e -> clearForm());

        btnSearch.addActionListener(e -> {
            JTextField searchEditor = (JTextField) cbSearch.getEditor().getEditorComponent();
            String keyword = searchEditor.getText().trim();

            tableModel.setRowCount(0);
            java.util.List<Employee> list = employeeSql.search(keyword);

            list.sort((e1, e2) -> {
                int rank1 = getRoleRank(e1.getRole());
                int rank2 = getRoleRank(e2.getRole());
                return Integer.compare(rank1, rank2);
            });

            for (Employee emp : list) {
                tableModel.addRow(new Object[]{
                    emp.getEmployeeId(), emp.getEmployeeName(), emp.getPhone(), 
                    emp.getEmail(), emp.getAccountStatus(), emp.getRole(), emp.getGender()
                });
            }
        });
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        ArrayList<Employee> list = employeeSql.selectAll();

        list.sort((e1, e2) -> {
            int rank1 = getRoleRank(e1.getRole());
            int rank2 = getRoleRank(e2.getRole());
            return Integer.compare(rank1, rank2);
        });

        for (Employee emp : list) {
            tableModel.addRow(new Object[]{
                emp.getEmployeeId(), emp.getEmployeeName(), emp.getPhone(), 
                emp.getEmail(), emp.getAccountStatus(), emp.getRole(), emp.getGender()
            });
        }
    }

    private Employee getEmployeeFromForm() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String gender = rdoMale.isSelected() ? "Nam" : (rdoFemale.isSelected() ? "Nữ" : "");

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || gender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ các thông tin cá nhân (*)");
            return null;
        }

        Employee e = new Employee();
        e.setEmployeeName(name);
        e.setPhone(phone);
        e.setEmail(email);
        e.setGender(gender);
        return e;
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");

        ((JTextField) cbSearch.getEditor().getEditorComponent()).setText("");

        btngGender.clearSelection();
        tblEmployees.clearSelection();

        loadDataToTable();
    }
}