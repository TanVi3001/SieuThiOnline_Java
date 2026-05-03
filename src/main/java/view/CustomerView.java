package view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.order.Customer;
import view.components.IconHelper;

public class CustomerView extends JPanel {

    private final Color bgLight = new Color(244, 246, 250);
    private final Color cardWhite = Color.WHITE;
    private final Color primaryBlue = new Color(54, 92, 245);
    private final Color textDark = new Color(43, 54, 116);
    private final Color textGray = new Color(163, 174, 208);
    private final Color borderGray = new Color(230, 235, 241);

    private JTextField txtId, txtName, txtPhone, txtEmail, txtAddress;
    private JComboBox<String> cbSearch;
    private JTable tblCustomers;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;

    private List<String> customerSearchList = new ArrayList<>();

    public CustomerView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(bgLight);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        loadAutoCompleteData();
        initUI();
        initEvents();
        searchAndFilterTable("");
    }

    private void loadAutoCompleteData() {
        customerSearchList.clear();
        try {
            List<Customer> list = business.sql.sales_order.CustomersSql.getInstance().getCustomersWithOrders();
            for (Customer c : list) {
                if (c.getCustomerName() != null) {
                    customerSearchList.add(c.getCustomerName() + " - " +
                            (c.getPhone() != null ? c.getPhone() : "N/A"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Danh Mục Khách Hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(textDark);

        JLabel lblSub = new JLabel("Quản lý thông tin liên hệ và lịch sử mua hàng");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(textGray);

        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);

        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        toolPanel.setOpaque(false);

        cbSearch = new JComboBox<>();
        styleSearchBox(cbSearch, "Nhập tên hoặc SĐT...");
        setupAutoComplete(cbSearch, customerSearchList);

        JPanel searchWrapper = new JPanel(new BorderLayout(5, 0));
        searchWrapper.setBackground(Color.WHITE);
        searchWrapper.setPreferredSize(new Dimension(300, 45));
        searchWrapper.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(borderGray, 25),
                new EmptyBorder(0, 15, 0, 15)
        ));

        searchWrapper.add(new JLabel(IconHelper.search(16)), BorderLayout.WEST);
        searchWrapper.add(cbSearch, BorderLayout.CENTER);

        btnSearch = createButton("Tìm kiếm", primaryBlue, Color.WHITE, null);

        toolPanel.add(searchWrapper);
        toolPanel.add(btnSearch);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(toolPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(20, 0));
        center.setOpaque(false);

        RoundedPanel form = new RoundedPanel(20, cardWhite);
        form.setPreferredSize(new Dimension(360, 0));
        form.setLayout(new GridBagLayout());
        form.setBorder(new EmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        txtId = createTextField("Mã tự động...");
        txtId.setEnabled(false);
        txtName = createTextField("Tên khách hàng...");
        txtPhone = createTextField("SĐT...");
        txtEmail = createTextField("Email...");
        txtAddress = createTextField("Địa chỉ...");

        int y = 0;
        form.add(createLabel("Mã khách hàng"), addGbc(gbc, y++, 5));
        form.add(txtId, addGbc(gbc, y++, 15));
        form.add(createLabel("Tên (*)"), addGbc(gbc, y++, 5));
        form.add(txtName, addGbc(gbc, y++, 15));
        form.add(createLabel("SĐT (*)"), addGbc(gbc, y++, 5));
        form.add(txtPhone, addGbc(gbc, y++, 15));
        form.add(createLabel("Email"), addGbc(gbc, y++, 5));
        form.add(txtEmail, addGbc(gbc, y++, 15));
        form.add(createLabel("Địa chỉ"), addGbc(gbc, y++, 25));
        form.add(txtAddress, addGbc(gbc, y++, 20));

        JPanel btnGrid = new JPanel(new GridLayout(2, 2, 12, 12));
        btnGrid.setOpaque(false);

        btnAdd = createButton("Thêm", primaryBlue, Color.WHITE, IconHelper.add(20));
        btnUpdate = createButton("Cập nhật", new Color(255, 153, 0), Color.BLACK, IconHelper.edit(20));
        btnDelete = createButton("Xóa", new Color(220, 53, 69), Color.WHITE, IconHelper.delete(20));
        btnClear = createButton("Làm mới", new Color(165, 177, 194), Color.WHITE, IconHelper.refresh(20));

        btnGrid.add(btnAdd);
        btnGrid.add(btnUpdate);
        btnGrid.add(btnDelete);
        btnGrid.add(btnClear);

        form.add(btnGrid, addGbc(gbc, y++, 0));

        RoundedPanel tableCard = new RoundedPanel(20, cardWhite);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Tên", "SĐT", "Email", "Địa chỉ"}, 0
        );

        tblCustomers = new JTable(tableModel);
        tblCustomers.setRowHeight(35);

        JScrollPane scroll = new JScrollPane(tblCustomers);
        scroll.setBorder(null);

        tableCard.add(scroll);

        center.add(form, BorderLayout.WEST);
        center.add(tableCard, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    private void setupAutoComplete(JComboBox<String> cb, List<String> items) {
        JTextField editor = (JTextField) cb.getEditor().getEditorComponent();

        for (String s : items) cb.addItem(s);

        editor.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    String text = editor.getText();
                    cb.removeAllItems();

                    for (String s : items) {
                        if (s.toLowerCase().contains(text.toLowerCase())) {
                            cb.addItem(s);
                        }
                    }
                    editor.setText(text);
                    cb.showPopup();
                });
            }
        });
    }

    private void initEvents() {
        btnSearch.addActionListener(e -> {
            String keyword = ((JTextField) cbSearch.getEditor().getEditorComponent())
                    .getText().toLowerCase();
            searchAndFilterTable(keyword);
        });
    }

    private void searchAndFilterTable(String keyword) {
        tableModel.setRowCount(0);
        try {
            List<Customer> list = business.sql.sales_order.CustomersSql.getInstance().getCustomersWithOrders();
            for (Customer c : list) {
                String name = c.getCustomerName() != null ? c.getCustomerName().toLowerCase() : "";
                String phone = c.getPhone() != null ? c.getPhone() : "";

                if (keyword.isEmpty() || name.contains(keyword) || phone.contains(keyword)) {
                    tableModel.addRow(new Object[]{
                            c.getCustomerId(),
                            c.getCustomerName(),
                            c.getPhone(),
                            c.getEmail(),
                            c.getAddress()
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JLabel createLabel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(textDark);
        return l;
    }

    private JTextField createTextField(String p) {
        JTextField t = new JTextField();
        t.setPreferredSize(new Dimension(200, 40));
        t.putClientProperty("JTextField.placeholderText", p);
        t.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(borderGray, 10),
                new EmptyBorder(5, 12, 5, 12)
        ));
        return t;
    }

    private JButton createButton(String text, Color bg, Color fg, ImageIcon icon) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        return btn;
    }

    private void styleSearchBox(JComboBox<String> cb, String placeholder) {
        cb.setEditable(true);
        JTextField editor = (JTextField) cb.getEditor().getEditorComponent();
        editor.setBorder(new EmptyBorder(0, 5, 0, 5));
        editor.putClientProperty("JTextField.placeholderText", placeholder);
    }

    private GridBagConstraints addGbc(GridBagConstraints gbc, int y, int bottom) {
        gbc.gridy = y;
        gbc.insets = new Insets(0, 0, bottom, 0);
        return gbc;
    }

    class RoundedPanel extends JPanel {
        int r;
        Color bg;

        public RoundedPanel(int r, Color bg) {
            this.r = r;
            this.bg = bg;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
        }
    }

    class RoundBorder implements javax.swing.border.Border {
        Color c;
        int r;

        public RoundBorder(Color c, int r) {
            this.c = c;
            this.r = r;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            g.setColor(this.c);
            g.drawRoundRect(x, y, w - 1, h - 1, r, r);
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }

        public boolean isBorderOpaque() {
            return false;
        }
    }
}