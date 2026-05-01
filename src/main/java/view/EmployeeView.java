package view;

import business.sql.hr_kpi.EmployeeSql;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.employee.Employee;
import view.components.IconHelper;

public class EmployeeView extends JPanel {

    // --- BẢNG MÀU & THÔNG SỐ UI ---
    private final Color bgLight = new Color(244, 246, 250);
    private final Color cardWhite = Color.WHITE;
    private final Color primaryBlue = new Color(54, 92, 245);    
    private final Color textDark = new Color(43, 54, 116);
    private final Color textGray = new Color(163, 174, 208);
    private final Color borderGray = new Color(230, 235, 241);

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
        // Kiểm tra quyền truy cập 
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
        } catch (Exception e) { e.printStackTrace(); }
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
        // --- HEADER ---
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

        // TOOLBAR TÌM KIẾM 
        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        toolPanel.setOpaque(false);

        cbSearch = new JComboBox<>();
        styleSearchBox(cbSearch);
        setupAutoComplete(cbSearch, employeeNameList);

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

        // --- BỐ CỤC CHÍNH ---
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
        txtName = createTextField("Nhập tên...");
        txtPhone = createTextField("Nhập số điện thoại...");
        txtEmail = createTextField("Nhập email...");

        rdoMale = new JRadioButton("Nam"); rdoFemale = new JRadioButton("Nữ");
        rdoMale.setOpaque(false); rdoFemale.setOpaque(false);
        btngGender = new ButtonGroup(); btngGender.add(rdoMale); btngGender.add(rdoFemale);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        genderPanel.setOpaque(false); genderPanel.add(rdoMale); genderPanel.add(rdoFemale);

        int y = 0;
        formCard.add(createLabel("Mã nhân viên"), addGbc(gbc, y++, 5)); formCard.add(txtId, addGbc(gbc, y++, 15));
        formCard.add(createLabel("Tên nhân viên (*)"), addGbc(gbc, y++, 5)); formCard.add(txtName, addGbc(gbc, y++, 15));
        formCard.add(createLabel("Số điện thoại (*)"), addGbc(gbc, y++, 5)); formCard.add(txtPhone, addGbc(gbc, y++, 15));
        formCard.add(createLabel("Email (*)"), addGbc(gbc, y++, 5)); formCard.add(txtEmail, addGbc(gbc, y++, 15));
        formCard.add(createLabel("Giới tính (*)"), addGbc(gbc, y++, 5)); formCard.add(genderPanel, addGbc(gbc, y++, 25));

        JPanel btnGrid = new JPanel(new GridLayout(2, 2, 12, 12));
        btnGrid.setOpaque(false);
        btnAdd = createCustomButton("Thêm hồ sơ", primaryBlue, Color.WHITE, IconHelper.add(20));
        btnUpdate = createCustomButton("Cập nhật", new Color(0, 168, 140), Color.WHITE, IconHelper.edit(20));
        btnDelete = createCustomButton("Xóa hồ sơ", new Color(220, 53, 69), Color.WHITE, IconHelper.delete(20));
        btnClear = createCustomButton("Làm mới", new Color(165, 177, 194), Color.WHITE, IconHelper.refresh(20));
        
        btnGrid.add(btnAdd); btnGrid.add(btnUpdate); btnGrid.add(btnDelete); btnGrid.add(btnClear);
        gbc.gridy = y++; formCard.add(btnGrid, gbc);

        // BẢNG BÊN PHẢI 
        RoundedPanel tableCard = new RoundedPanel(20, cardWhite);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new Object[]{"Mã nhân viên", "Tên nhân viên", "Số điện thoại", "Email", "Cấp tài khoản", "Chức vụ", "Giới tính"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblEmployees = new JTable(tableModel);
        setupTableStyle();

        JScrollPane scrollPane = new JScrollPane(tblEmployees);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(formCard, BorderLayout.WEST);
        centerPanel.add(tableCard, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void initEvents() {
        tblEmployees.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = tblEmployees.getSelectedRow();
                if (row >= 0) {
                    String role = String.valueOf(tblEmployees.getValueAt(row, 5));
                    // Chặn thao tác trên Admin/Manager
                    if (role.contains("ADMIN") || role.contains("Quản trị viên") || role.contains("MNG")) {
                        JOptionPane.showMessageDialog(null, "⚠️ Đây là hồ sơ cấp cao. Bạn không có quyền thao tác!", "Bảo mật", JOptionPane.WARNING_MESSAGE);
                        clearForm(); return;
                    }
                    txtId.setText(String.valueOf(tblEmployees.getValueAt(row, 0)));
                    txtName.setText(String.valueOf(tblEmployees.getValueAt(row, 1)));
                    txtPhone.setText(String.valueOf(tblEmployees.getValueAt(row, 2)));
                    txtEmail.setText(String.valueOf(tblEmployees.getValueAt(row, 3)));
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
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadDataToTable(); clearForm();
            }
        });

        btnUpdate.addActionListener(e -> {
            if (txtId.getText().isEmpty()) return;
            Employee emp = getEmployeeFromForm();
            if (emp == null) return;
            emp.setEmployeeId(txtId.getText());
            if (employeeSql.update(emp) > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadDataToTable();
            }
        });

        btnDelete.addActionListener(e -> {
            if (txtId.getText().isEmpty()) return;
            if (JOptionPane.showConfirmDialog(this, "Xóa hồ sơ này?", "Xác nhận", 0) == 0) {
                if (employeeSql.delete(txtId.getText()) > 0) {
                    loadDataToTable(); clearForm();
                }
            }
        });

        btnClear.addActionListener(e -> clearForm());

        btnSearch.addActionListener(e -> {
            String keyword = ((JTextField) cbSearch.getEditor().getEditorComponent()).getText().trim();
            updateTable(employeeSql.search(keyword));
        });
    }

    private void loadDataToTable() {
        updateTable(employeeSql.selectAll());
    }

    private void updateTable(List<Employee> list) {
        tableModel.setRowCount(0);
        list.sort((e1, e2) -> Integer.compare(getRoleRank(e1.getRole()), getRoleRank(e2.getRole())));
        for (Employee emp : list) {
            tableModel.addRow(new Object[]{
                emp.getEmployeeId(), emp.getEmployeeName(), emp.getPhone(), 
                emp.getEmail(), emp.getAccountStatus(), emp.getRole(), emp.getGender()
            });
        }
    }

    private int getRoleRank(String role) {
        if (role == null) return 3;
        if (role.contains("ADMIN")) return 1;
        if (role.contains("MNG")) return 2;
        return 3;
    }

    private Employee getEmployeeFromForm() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String gender = rdoMale.isSelected() ? "Nam" : (rdoFemale.isSelected() ? "Nữ" : "");
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || gender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đủ thông tin (*)");
            return null;
        }
        Employee e = new Employee();
        e.setEmployeeName(name); e.setPhone(phone); e.setEmail(email); e.setGender(gender);
        return e;
    }

    private void clearForm() {
        txtId.setText(""); txtName.setText(""); txtPhone.setText(""); txtEmail.setText("");
        btngGender.clearSelection(); tblEmployees.clearSelection();
        ((JTextField) cbSearch.getEditor().getEditorComponent()).setText("");
    }

    private void setupTableStyle() {
        tblEmployees.setRowHeight(40);
        tblEmployees.setShowVerticalLines(false);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
                setHorizontalAlignment(0);
                String role = String.valueOf(t.getValueAt(r, 5));
                if (role.contains("ADMIN")) setForeground(Color.RED);
                else if (role.contains("MNG")) setForeground(new Color(25, 135, 84));
                else setForeground(Color.BLACK);
                return comp;
            }
        };
        for (int i = 0; i < tblEmployees.getColumnCount(); i++) tblEmployees.getColumnModel().getColumn(i).setCellRenderer(renderer);
    }

    private GridBagConstraints addGbc(GridBagConstraints gbc, int y, int b) {
        gbc.gridy = y; gbc.insets = new Insets(0, 0, b, 0); return gbc;
    }

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

    private JButton createCustomButton(String t, Color bg, Color fg, ImageIcon icon) {
        JButton btn = new JButton(t);
        if (icon != null) btn.setIcon(new ImageIcon(icon.getImage().getScaledInstance(18, 18, 1)));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg); btn.setBackground(bg);
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setCursor(new Cursor(12)); btn.setFocusPainted(false); btn.setBorderPainted(false); btn.setContentAreaFilled(false);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground()); g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 25, 25);
                super.paint(g2, c); g2.dispose();
            }
        });
        return btn;
    }

    private void styleSearchBox(JComboBox<String> cb) {
        cb.setEditable(true); cb.setBorder(null); cb.setBackground(Color.WHITE);
        cb.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override protected JButton createArrowButton() { return new JButton() {{ setPreferredSize(new Dimension(0,0)); }}; }
        });
        ((JTextField)cb.getEditor().getEditorComponent()).setBorder(new EmptyBorder(0,5,0,5));
    }

    private void setupAutoComplete(JComboBox<String> cb, List<String> items) {
        for (String i : items) cb.addItem(i);
        cb.setSelectedItem("");
        cb.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) btnSearch.doClick();
            }
        });
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