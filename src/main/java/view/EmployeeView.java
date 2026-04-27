/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

// import cần có
import business.sql.hr_kpi.EmployeeSql;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.employee.Employee;

/**
 *
 * @author Admin
 */

public class EmployeeView extends javax.swing.JPanel {

    /**
     * Creates new form EmployeeView
     */
    private DefaultTableModel tableModel;
    private final EmployeeSql employeeSql = new EmployeeSql();

    public EmployeeView() {
        initComponents();
        initTable();
        initEvents();
        loadDataToTable();
        setupModernUI();
    }
    
    private void setupModernUI() {
        java.awt.Color bgLight    = new java.awt.Color(244, 246, 250);
        java.awt.Color navy       = new java.awt.Color(43, 54, 116);
        java.awt.Color textGray   = new java.awt.Color(163, 174, 208);
        java.awt.Color borderGray = new java.awt.Color(230, 235, 241);
        java.awt.Color bgField    = new java.awt.Color(248, 249, 252);
        java.awt.Color teal       = new java.awt.Color(0, 168, 140);
        java.awt.Color red        = new java.awt.Color(220, 53, 69);
        java.awt.Color yellow     = new java.awt.Color(230, 180, 50);

        // ── Nền tổng ────────────────────────────────────────────────────────────
        this.setBackground(bgLight);
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── jPanel1 – thanh tìm kiếm ────────────────────────────────────────────
        jPanel1.removeAll();
        jPanel1.setLayout(new java.awt.BorderLayout(10, 0));
        jPanel1.setBackground(bgLight);
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 56));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 12, 0));

        txtSearch.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        txtSearch.setBackground(java.awt.Color.WHITE);
        txtSearch.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(borderGray, 1, true),
            javax.swing.BorderFactory.createEmptyBorder(0, 14, 0, 14)
        ));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm nhân viên...");
        jPanel1.add(txtSearch, java.awt.BorderLayout.CENTER);

        styleBtn(btnSearch, teal, java.awt.Color.WHITE);
        btnSearch.setPreferredSize(new java.awt.Dimension(110, 40));
        jPanel1.add(btnSearch, java.awt.BorderLayout.EAST);

        // ── pnForm – card form trái ─────────────────────────────────────────────
        pnForm.removeAll();
        pnForm.setLayout(new java.awt.BorderLayout());
        pnForm.setBackground(bgLight);
        pnForm.setPreferredSize(new java.awt.Dimension(260, 0));
        pnForm.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 12));

        javax.swing.JPanel formCard = new javax.swing.JPanel() {
            @Override protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(java.awt.Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose(); super.paintComponent(g);
            }
        };
        formCard.setOpaque(false);
        formCard.setLayout(new javax.swing.BoxLayout(formCard, javax.swing.BoxLayout.Y_AXIS));
        formCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        javax.swing.JLabel lblCardTitle = new javax.swing.JLabel("Thông tin nhân viên");
        lblCardTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        lblCardTitle.setForeground(navy);
        lblCardTitle.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        lblCardTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 16, 0));
        formCard.add(lblCardTitle);

        addRow(formCard, "Tên nhân viên", txtEmployeeName, navy, borderGray, bgField);
        addRow(formCard, "Số điện thoại", txtEmployeePhone, navy, borderGray, bgField);
        addRow(formCard, "Email", txtEmployeeEmail, navy, borderGray, bgField);
        addRow(formCard, "Chức vụ", txtEmployeeRole, navy, borderGray, bgField);

        // Giới tính
        javax.swing.JLabel lblGender = new javax.swing.JLabel("Giới tính");
        lblGender.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        lblGender.setForeground(navy);
        lblGender.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        lblGender.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 6, 0));
        formCard.add(lblGender);

        javax.swing.JPanel genderPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        genderPanel.setOpaque(false);
        genderPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        genderPanel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 30));

        rdoMale.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        rdoMale.setForeground(navy); rdoMale.setOpaque(false);
        rdoFemale.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        rdoFemale.setForeground(navy); rdoFemale.setOpaque(false);
        genderPanel.add(rdoMale);
        genderPanel.add(javax.swing.Box.createHorizontalStrut(16));
        genderPanel.add(rdoFemale);
        formCard.add(genderPanel);

        pnForm.add(formCard, java.awt.BorderLayout.CENTER);

        // ── pnButton – nút bấm ──────────────────────────────────────────────────
        pnButton.setBackground(bgLight);
        pnButton.setPreferredSize(new java.awt.Dimension(0, 62));
        pnButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 0, 0, 0));
        pnButton.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));

        styleBtn(btnAdd,    navy,   java.awt.Color.WHITE);
        styleBtn(btnUpdate, teal,   java.awt.Color.WHITE);
        styleBtn(btnDelete, red,    java.awt.Color.WHITE);
        styleBtn(btnClear,  yellow, navy);

        for (javax.swing.JButton b : new javax.swing.JButton[]{btnAdd, btnUpdate, btnDelete, btnClear})
            b.setPreferredSize(new java.awt.Dimension(110, 40));

        // ── Bảng ────────────────────────────────────────────────────────────────
        tbEmployee.setBorder(null);
        tbEmployee.getViewport().setBackground(java.awt.Color.WHITE);

        jTable1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        jTable1.setRowHeight(36);
        jTable1.setGridColor(borderGray);
        jTable1.setSelectionBackground(new java.awt.Color(210, 220, 235));
        jTable1.setSelectionForeground(navy);
        jTable1.setShowHorizontalLines(true);
        jTable1.setShowVerticalLines(false);
        jTable1.setIntercellSpacing(new java.awt.Dimension(0, 1));
        jTable1.setFillsViewportHeight(true);

        javax.swing.table.JTableHeader header = jTable1.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40));
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                javax.swing.JLabel l = (javax.swing.JLabel)
                    super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                l.setBackground(navy); l.setForeground(java.awt.Color.WHITE);
                l.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
                l.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 14, 0, 14));
                l.setOpaque(true); return l;
            }
        });

        jTable1.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 14, 0, 14));
                setBackground(sel ? new java.awt.Color(210, 220, 235)
                        : row % 2 == 0 ? java.awt.Color.WHITE : new java.awt.Color(248, 249, 252));
                setForeground(sel ? navy : new java.awt.Color(44, 62, 80));
                return this;
            }
        });

        this.revalidate();
        this.repaint();
    }

    private void addRow(javax.swing.JPanel parent, String labelText,
            javax.swing.JTextField field,
            java.awt.Color navy, java.awt.Color borderGray, java.awt.Color bgField) {
        javax.swing.JLabel lbl = new javax.swing.JLabel(labelText);
        lbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        lbl.setForeground(navy);
        lbl.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        lbl.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 4, 0));
        parent.add(lbl);

        field.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        field.setBackground(bgField);
        field.setForeground(new java.awt.Color(44, 62, 80));
        field.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 36));
        field.setPreferredSize(new java.awt.Dimension(220, 36));
        field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(borderGray, 1, true),
            javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        parent.add(field);
    }

    private void styleBtn(javax.swing.JButton btn, java.awt.Color bg, java.awt.Color fg) {
        btn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btn.setForeground(fg); btn.setFocusPainted(false);
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setOpaque(false); btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(btn.getModel().isRollover() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);
                g2.dispose(); super.paint(g, c);
            }
        });
    }

    private void initTable() {
        tableModel = (DefaultTableModel) jTable1.getModel();
        jTable1.setModel(new DefaultTableModel(
                new Object[]{"Mã nhân viên", "Tên nhân viên", "Số điện thoại", "Email", "Chức vụ", "Giới tính"}, 0
        ));
    }

    private void initEvents() {
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
    }

    // ====== THÊM CÁC HÀM HỖ TRỢ ======
    private void loadDataToTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        ArrayList<Employee> list = employeeSql.selectAll();
        for (Employee e : list) {
            model.addRow(new Object[]{
                e.getEmployeeId(),
                e.getEmployeeName(),
                e.getPhone(),
                e.getEmail(),
                e.getRole(), // nếu model dùng getPosition() thì đổi lại
                e.getGender()
            });
        }
    }

    private Employee getEmployeeFromForm() {
        String name = txtEmployeeName.getText().trim();
        String phone = txtEmployeePhone.getText().trim();
        String email = txtEmployeeEmail.getText().trim();
        String role = txtEmployeeRole.getText().trim();
        String gender = rdoMale.isSelected() ? "Nam" : (rdoFemale.isSelected() ? "Nữ" : "");

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được để trống!");
            return null;
        }
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!");
            return null;
        }
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email không được để trống!");
            return null;
        }
        if (role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Role ID không được để trống! (ví dụ: R001)");
            return null;
        }
        if (gender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giới tính!");
            return null;
        }

        Employee e = new Employee();
        e.setEmployeeName(name);
        e.setPhone(phone);
        e.setEmail(email);
        e.setRole(role);
        e.setRoleId(role);
        e.setGender(gender);
        return e;
    }

    private String generateEmployeeId() {
        return "EMP" + System.currentTimeMillis();
    }

    private void clearForm() {
        txtEmployeeName.setText("");
        txtEmployeePhone.setText("");
        txtEmployeeEmail.setText("");
        txtEmployeeRole.setText("");
        btngGender.clearSelection();
        jTable1.clearSelection();
    }

    private String getSelectedEmployeeId() {
        int row = jTable1.getSelectedRow();
        if (row < 0) {
            return null;
        }
        Object v = jTable1.getValueAt(row, 0);
        return v == null ? null : v.toString();
    }

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        int row = jTable1.getSelectedRow();
        if (row < 0) {
            return;
        }

        txtEmployeeName.setText(String.valueOf(jTable1.getValueAt(row, 1)));
        txtEmployeePhone.setText(String.valueOf(jTable1.getValueAt(row, 2)));
        txtEmployeeEmail.setText(String.valueOf(jTable1.getValueAt(row, 3)));
        txtEmployeeRole.setText(String.valueOf(jTable1.getValueAt(row, 4)));

        String gender = String.valueOf(jTable1.getValueAt(row, 5));
        rdoMale.setSelected("Nam".equalsIgnoreCase(gender));
        rdoFemale.setSelected("Nữ".equalsIgnoreCase(gender));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btngGender = new javax.swing.ButtonGroup();
        pnForm = new javax.swing.JPanel();
        EmloyeeName = new javax.swing.JLabel();
        txtEmployeeName = new javax.swing.JTextField();
        EmployeePhone = new javax.swing.JLabel();
        txtEmployeePhone = new javax.swing.JTextField();
        EmployeeEmail = new javax.swing.JLabel();
        txtEmployeeEmail = new javax.swing.JTextField();
        EmployeeRole = new javax.swing.JLabel();
        txtEmployeeRole = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        rdoMale = new javax.swing.JRadioButton();
        rdoFemale = new javax.swing.JRadioButton();
        tbEmployee = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        pnButton = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();

        setBackground(new java.awt.Color(236, 240, 241));
        setLayout(new java.awt.BorderLayout());

        pnForm.setBackground(new java.awt.Color(236, 240, 241));
        pnForm.setPreferredSize(new java.awt.Dimension(300, 0));

        EmloyeeName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        EmloyeeName.setText("Tên nhân viên");
        EmloyeeName.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txtEmployeeName.addActionListener(this::txtEmployeeNameActionPerformed);

        EmployeePhone.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        EmployeePhone.setText("Số điện thoại");
        EmployeePhone.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txtEmployeePhone.addActionListener(this::txtEmployeePhoneActionPerformed);

        EmployeeEmail.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        EmployeeEmail.setText("Email");
        EmployeeEmail.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txtEmployeeEmail.addActionListener(this::txtEmployeeEmailActionPerformed);

        EmployeeRole.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        EmployeeRole.setText("Chức vụ");
        EmployeeRole.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        txtEmployeeRole.addActionListener(this::txtEmployeeRoleActionPerformed);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Giới tính");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        btngGender.add(rdoMale);
        rdoMale.setText("Nam");
        rdoMale.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        rdoMale.addActionListener(this::rdoMaleActionPerformed);

        btngGender.add(rdoFemale);
        rdoFemale.setText("Nữ");
        rdoFemale.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        rdoFemale.addActionListener(this::rdoFemaleActionPerformed);

        javax.swing.GroupLayout pnFormLayout = new javax.swing.GroupLayout(pnForm);
        pnForm.setLayout(pnFormLayout);
        pnFormLayout.setHorizontalGroup(
            pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EmloyeeName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnFormLayout.createSequentialGroup()
                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EmployeePhone, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmployeePhone, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EmployeeEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmployeeEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EmployeeRole, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(pnFormLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(rdoFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(pnFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEmployeeRole, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdoMale, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnFormLayout.setVerticalGroup(
            pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFormLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(EmloyeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(EmployeePhone, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(txtEmployeePhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(EmployeeEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(txtEmployeeEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(EmployeeRole, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmployeeRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdoMale, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdoFemale, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        add(pnForm, java.awt.BorderLayout.LINE_START);

        jTable1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Tên nhân viên", "Số điện thoại", "Email", "Chức vụ", "Giới tính"
            }
        ));
        tbEmployee.setViewportView(jTable1);

        add(tbEmployee, java.awt.BorderLayout.CENTER);

        pnButton.setBackground(new java.awt.Color(236, 240, 241));
        pnButton.setPreferredSize(new java.awt.Dimension(0, 40));

        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAdd.setText("Thêm");
        btnAdd.addActionListener(this::btnAddActionPerformed);
        pnButton.add(btnAdd);

        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdate.setText("Cập nhật");
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);
        pnButton.add(btnUpdate);

        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDelete.setText("Xóa");
        btnDelete.addActionListener(this::btnDeleteActionPerformed);
        pnButton.add(btnDelete);

        btnClear.setBackground(new java.awt.Color(255, 255, 204));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClear.setText("Làm mới");
        btnClear.addActionListener(this::btnClearActionPerformed);
        pnButton.add(btnClear);

        add(pnButton, java.awt.BorderLayout.PAGE_END);

        jPanel1.setBackground(new java.awt.Color(236, 240, 241));
        jPanel1.setPreferredSize(new java.awt.Dimension(1051, 40));

        txtSearch.addActionListener(this::txtSearchActionPerformed);

        btnSearch.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSearch.setText("Tìm kiếm");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(300, 300, 300)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addContainerGap())
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        Employee e = getEmployeeFromForm();
        if (e == null) {
            return;
        }

        // form không có ô mã => tự sinh
        e.setEmployeeId(generateEmployeeId());

        int rs = employeeSql.insert(e);
        if (rs > 0) {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            loadDataToTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }

    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        String id = getSelectedEmployeeId();
        if (id == null || id.isBlank()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên trong bảng để cập nhật!");
            return;
        }

        Employee e = getEmployeeFromForm();
        if (e == null) {
            return;
        }
        e.setEmployeeId(id);

        int rs = employeeSql.update(e);
        JOptionPane.showMessageDialog(this, rs > 0 ? "Cập nhật thành công!" : "Cập nhật thất bại!");
        loadDataToTable();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        String id = getSelectedEmployeeId();
        if (id == null || id.isBlank()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên trong bảng để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa nhân viên này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int rs = employeeSql.delete(id);
            JOptionPane.showMessageDialog(this, rs > 0 ? "Xóa thành công!" : "Xóa thất bại!");
            loadDataToTable();
            clearForm();
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearForm();
        loadDataToTable();
    }//GEN-LAST:event_btnClearActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        String keyword = txtSearch.getText().trim();

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        java.util.List<Employee> list = employeeSql.search(keyword); // cần có hàm search trong DAO
        for (Employee e : list) {
            model.addRow(new Object[]{
                e.getEmployeeId(),
                e.getEmployeeName(),
                e.getPhone(),
                e.getEmail(),
                e.getRole(), // hoặc getPosition()
                e.getGender()
            });
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtEmployeeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeeNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmployeeNameActionPerformed

    private void txtEmployeePhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeePhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmployeePhoneActionPerformed

    private void txtEmployeeEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeeEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmployeeEmailActionPerformed

    private void txtEmployeeRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeeRoleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmployeeRoleActionPerformed

    private void rdoMaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoMaleActionPerformed

    private void rdoFemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoFemaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoFemaleActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel EmloyeeName;
    private javax.swing.JLabel EmployeeEmail;
    private javax.swing.JLabel EmployeePhone;
    private javax.swing.JLabel EmployeeRole;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup btngGender;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel pnButton;
    private javax.swing.JPanel pnForm;
    private javax.swing.JRadioButton rdoFemale;
    private javax.swing.JRadioButton rdoMale;
    private javax.swing.JScrollPane tbEmployee;
    private javax.swing.JTextField txtEmployeeEmail;
    private javax.swing.JTextField txtEmployeeName;
    private javax.swing.JTextField txtEmployeePhone;
    private javax.swing.JTextField txtEmployeeRole;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
