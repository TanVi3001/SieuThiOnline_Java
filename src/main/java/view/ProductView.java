package view;

import business.sql.prod_inventory.ProductsSql;
import business.sql.prod_inventory.ProductUnitsSql;
import business.service.UnitOfMeasureService;
import business.service.AuthorizationService;
import common.utils.Validator;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.product.Product;
import model.product.ProductUnit;

// THÊM THƯ VIỆN ĐỂ ĐỌC FILE VÀ MỞ CỬA SỔ CHỌN FILE
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ProductView extends JPanel {

    // --- CẤU HÌNH MÀU SẮC CHUẨN MODERN UI ---
    private final Color bgLight = new Color(244, 246, 250);
    private final Color cardWhite = Color.WHITE;
    private final Color primaryBlue = new Color(67, 97, 238);
    private final Color textDark = new Color(43, 54, 116);
    private final Color textGray = new Color(163, 174, 208);
    private final Color borderGray = new Color(230, 235, 241);

    // --- KHAI BÁO UI COMPONENTS ---
    private JTextField txtName, txtPrice, txtQuantity, txtCategory, txtSearch;
    private JTable tblProducts;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch, btnExportPDF, btnUnitConfig, btnImport;

    public ProductView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(bgLight);
        setBorder(new EmptyBorder(20, 30, 20, 30)); // Padding cho toàn màn hình

        initUI();
        initEvents();
        loadDataToTable();

        // ---------------------------------------------------------
        // KIỂM TRA QUYỀN (AUTHORIZATION) BẢO VỆ DỮ LIỆU
        // ---------------------------------------------------------
        if (AuthorizationService.isCashier()) {
            // Nếu là Thu ngân -> Tàng hình các nút chỉnh sửa, chỉ cho xem
            btnAdd.setVisible(false);
            btnUpdate.setVisible(false);
            btnDelete.setVisible(false);
            btnUnitConfig.setVisible(false);
            btnImport.setVisible(false); // Thu ngân cũng không được import file
        }
    }

    private void initUI() {
        // --- HEADER (Tiêu đề + Ô Tìm kiếm + Nút Xuất Excel + Nút Nhập CSV) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel lblTitle = new JLabel("Danh Mục Sản Phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(textDark);
        JLabel lblSub = new JLabel("Quản lý thông tin, giá bán và số lượng tồn kho");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(textGray);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);

        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        toolPanel.setOpaque(false);
        txtSearch = createTextField("Nhập tên sản phẩm...");
        txtSearch.setPreferredSize(new Dimension(280, 40));
        
        btnSearch = createCustomButton("Tìm kiếm", primaryBlue, Color.WHITE);
        btnExportPDF = createCustomButton("Xuất Excel", new Color(0, 163, 108), Color.WHITE);
        
        // NÚT NHẬP CSV MÀU TÍM
        btnImport = createCustomButton("Nhập CSV", new Color(103, 58, 183), Color.WHITE);
        
        toolPanel.add(txtSearch);
        toolPanel.add(btnSearch);
        toolPanel.add(btnExportPDF);
        toolPanel.add(btnImport); // Gắn nút vào thanh công cụ

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(toolPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- CENTER (Form Nhập liệu + Bảng Dữ liệu) ---
        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setOpaque(false);

        // 1. LEFT FORM (Thẻ Form trắng bo góc bên trái)
        RoundedPanel formCard = new RoundedPanel(20, cardWhite);
        formCard.setPreferredSize(new Dimension(350, 0));
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;

        txtName = createTextField("Nhập tên...");
        txtPrice = createTextField("Nhập giá (VNĐ)...");
        txtQuantity = createTextField("Nhập số lượng...");
        txtCategory = createTextField("Nhập mã loại...");

        int y = 0;
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0); formCard.add(createLabel("Tên sản phẩm (*)"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 15, 0); formCard.add(txtName, gbc);

        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0); formCard.add(createLabel("Giá bán (*)"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 15, 0); formCard.add(txtPrice, gbc);

        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0); formCard.add(createLabel("Số lượng (*)"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 15, 0); formCard.add(txtQuantity, gbc);

        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 5, 0); formCard.add(createLabel("Loại sản phẩm (*)"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 30, 0); formCard.add(txtCategory, gbc);

        // Grid chứa 5 nút chức năng
        btnAdd = createCustomButton("Thêm", primaryBlue, Color.WHITE);
        btnUpdate = createCustomButton("Cập nhật", new Color(255, 153, 0), Color.BLACK);
        btnDelete = createCustomButton("Xóa", new Color(220, 53, 69), Color.WHITE);
        btnClear = createCustomButton("Làm mới", new Color(230, 235, 241), textDark);
        btnUnitConfig = createCustomButton("Đơn vị", textGray, Color.WHITE);

        JPanel btnGrid = new JPanel(new GridLayout(3, 2, 10, 10));
        btnGrid.setOpaque(false);
        btnGrid.add(btnAdd);
        btnGrid.add(btnUpdate);
        btnGrid.add(btnDelete);
        btnGrid.add(btnClear);
        btnGrid.add(btnUnitConfig);

        gbc.gridy = y++; formCard.add(btnGrid, gbc);
        centerPanel.add(formCard, BorderLayout.WEST);

        // 2. RIGHT TABLE (Thẻ chứa JTable bo góc bên phải)
        RoundedPanel tableCard = new RoundedPanel(20, cardWhite);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new Object[]{"Mã SP", "Tên sản phẩm", "Giá", "Số lượng", "Loại SP"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblProducts = new JTable(tableModel);
        tblProducts.setRowHeight(35); // Bảng cao ráo, dễ bấm
        tblProducts.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblProducts.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblProducts.getTableHeader().setBackground(bgLight);
        tblProducts.getTableHeader().setReorderingAllowed(false);
        tblProducts.setShowVerticalLines(false);
        tblProducts.setSelectionBackground(new Color(237, 242, 255));
        tblProducts.setSelectionForeground(textDark);

        JScrollPane scrollPane = new JScrollPane(tblProducts);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(tableCard, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    // ==========================================
    // CÁC HÀM UI HELPERS
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
            @Override public void mouseEntered(MouseEvent e) { btn.setOpaque(false); }
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
        tblProducts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                tblProductsMouseClicked(evt);
            }
        });
        btnAdd.addActionListener(e -> btnAddActionPerformed());
        btnUpdate.addActionListener(e -> btnUpdateActionPerformed());
        btnDelete.addActionListener(e -> btnDeleteActionPerformed());
        btnClear.addActionListener(e -> btnClearActionPerformed());
        btnSearch.addActionListener(e -> btnSearchActionPerformed());
        btnExportPDF.addActionListener(e -> btnExportPDFActionPerformed());
        btnUnitConfig.addActionListener(e -> showUnitConfigDialog());
        btnImport.addActionListener(e -> handleImportCSV());
    }

    private void btnAddActionPerformed() {                                         
        if (!validateInput()) return;
        Product p = getProductFromForm();
        if (p == null) return;

        business.sql.prod_inventory.ProductsSql dao = business.sql.prod_inventory.ProductsSql.getInstance();

        // --- LOGIC 1: KIỂM TRA TRÙNG TÊN SẢN PHẨM ---
        Product existingProduct = dao.findByExactName(p.getProductName());

        if (existingProduct != null) {
            // NẾU ĐÃ TỒN TẠI -> Hiện bảng hỏi xem có muốn cộng dồn không
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Món '" + p.getProductName() + "' đã có trong kho.\nBạn có muốn cộng dồn thêm " + p.getQuantity() + " vào số lượng hiện tại không?",
                    "Phát hiện trùng lặp", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Gọi hàm cộng dồn
                if (dao.addQuantity(existingProduct.getProductId(), p.getQuantity(), existingProduct.getStoreId())) {
                    JOptionPane.showMessageDialog(this, "✅ Đã cộng dồn số lượng thành công!");
                    loadDataToTable();
                    btnClearActionPerformed();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Lỗi khi cộng dồn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            // --- LOGIC 2: SẢN PHẨM HOÀN TOÀN MỚI ---
            // Gọi hàm sinh mã tự động SP000...
            p.setProductId(dao.generateNextProductId());

            // Bắt buộc cho FK (tạm mặc định nếu trống)
            if (p.getSupplierId() == null || p.getSupplierId().trim().isEmpty()) {
                p.setSupplierId("SUP001");
            }
            if (p.getStoreId() == null || p.getStoreId().trim().isEmpty()) {
                p.setStoreId("ST001");
            }
            if (p.getUnit() == null || p.getUnit().trim().isEmpty()) {
                p.setUnit("Cái");
            }

            if (dao.insert(p)) {
                JOptionPane.showMessageDialog(this, "✅ Thêm sản phẩm mới thành công!\nMã tự cấp: " + p.getProductId());
                loadDataToTable();
                btnClearActionPerformed();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Thêm thất bại! Kiểm tra category_id có tồn tại không.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }                                        

    private void btnUpdateActionPerformed() {                                            
        int row = tblProducts.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn dòng cần sửa!");
            return;
        }

        String idOld = tblProducts.getValueAt(row, 0).toString().trim();
        Product p = getProductFromForm();
        if (p == null) return;

        p.setProductId(idOld); 

        if (ProductsSql.getInstance().update(p)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDataToTable(); 
        } else {
            JOptionPane.showMessageDialog(this, "Thất bại! Check mã Loại/Giá.");
        }
    }                                           

    private void btnDeleteActionPerformed() {                                            
        int row = tblProducts.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn dòng cần xóa!");
            return;
        }

        String id = tblProducts.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa sản phẩm " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean usedInOrders = ProductsSql.getInstance().isUsedInOrders(id);
            if (ProductsSql.getInstance().delete(id)) {
                if (usedInOrders) {
                    JOptionPane.showMessageDialog(this, "Sản phẩm đã có trong hóa đơn nên hệ thống chỉ ẩn sản phẩm khỏi kho/danh sách bán.", "Đã ẩn sản phẩm", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa mềm sản phẩm thành công!");
                }
                loadDataToTable();
                btnClearActionPerformed();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa sản phẩm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }                                           

    private void btnClearActionPerformed() {                                          
        txtName.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
        txtCategory.setText("");
        tblProducts.clearSelection();
    }                                         

    private void btnSearchActionPerformed() {                                            
        String keyword = txtSearch.getText().trim();
        List<Product> list = ProductsSql.getInstance().searchByName(keyword);
        fillTable(list);
    }                                           

    private void btnExportPDFActionPerformed() {                                               
        try {
            List<Map<String, Object>> productList = getAllProductsFromTable();

            if (productList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Lưu file Excel");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setSelectedFile(new java.io.File("SanPham_" + System.currentTimeMillis() + ".xlsx"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                common.report.ExcelExporter.exportInventoryFromMap(productList, filePath);
                JOptionPane.showMessageDialog(this, "✅ Xuất Excel thành công!\nFile: " + filePath, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi xuất Excel: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }                                              

    private void tblProductsMouseClicked(MouseEvent evt) {
        int row = tblProducts.getSelectedRow();
        if (row < 0) return;

        Object nameObj = tblProducts.getValueAt(row, 1);
        Object priceObj = tblProducts.getValueAt(row, 2);
        Object qtyObj = tblProducts.getValueAt(row, 3);
        Object categoryObj = tblProducts.getValueAt(row, 4);

        txtName.setText(nameObj == null ? "" : nameObj.toString());
        txtPrice.setText(priceObj == null ? "" : priceObj.toString());
        txtQuantity.setText(qtyObj == null ? "" : qtyObj.toString());
        txtCategory.setText(categoryObj == null ? "" : categoryObj.toString());
    }

    private List<Map<String, Object>> getAllProductsFromTable() {
        List<Map<String, Object>> list = new ArrayList<>();
        int rowCount = tblProducts.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Map<String, Object> row = new HashMap<>();
            row.put("productId", tblProducts.getValueAt(i, 0));
            row.put("productName", tblProducts.getValueAt(i, 1));
            row.put("price", tblProducts.getValueAt(i, 2));
            row.put("quantity", tblProducts.getValueAt(i, 3));
            list.add(row);
        }
        return list;
    }

    public void loadDataToTable() {
        tableModel.setRowCount(0);
        try {
            List<Product> list = ProductsSql.getInstance().selectAll();
            for (Product p : list) {
                Object[] row = { p.getProductId(), p.getProductName(), p.getBasePrice(), p.getQuantity(), p.getCategoryId() };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        loadDataToTable();
    }

    private void fillTable(List<Product> list) {
        tableModel.setRowCount(0);
        for (Product p : list) {
            tableModel.addRow(new Object[]{ p.getProductId(), p.getProductName(), p.getBasePrice(), p.getQuantity(), p.getCategoryId() });
        }
    }

    private boolean validateInput() {
        if (Validator.isEmpty(txtName.getText())) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được rỗng!");
            return false;
        }
        if (!Validator.isPositiveInteger(txtQuantity.getText())) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương!");
            return false;
        }
        try {
            new BigDecimal(txtPrice.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Giá không hợp lệ!");
            return false;
        }
        return true;
    }

    private Product getProductFromForm() {
        Product p = new Product();
        String name = txtName.getText().trim();
        String priceText = txtPrice.getText().trim();
        String qtyText = txtQuantity.getText().trim();
        String categoryId = txtCategory.getText().trim();

        if (name.isEmpty() || priceText.isEmpty() || qtyText.isEmpty() || categoryId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ Tên, Giá, Số lượng và Loại SP!");
            return null;
        }

        try {
            p.setProductName(name);
            p.setBasePrice(new BigDecimal(priceText));
            p.setQuantity(Integer.parseInt(qtyText));
            p.setCategoryId(categoryId);
            return p;
        } catch (Exception e) {
            return null;
        }
    }

    private String getSelectedProductId() {
        int viewRow = tblProducts.getSelectedRow();
        if (viewRow < 0) return null;
        int modelRow = tblProducts.convertRowIndexToModel(viewRow);
        Object value = tblProducts.getModel().getValueAt(modelRow, 0);
        return value == null ? null : value.toString().trim();
    }

    private void showUnitConfigDialog() {
        String productId = getSelectedProductId();
        if (productId == null || productId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm cần cấu hình đơn vị!");
            return;
        }

        DefaultTableModel unitModel = new DefaultTableModel(new Object[]{"Đơn vị", "Tỷ lệ về ĐV gốc", "ĐV gốc"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        loadProductUnits(productId, unitModel);

        JTable unitTable = new JTable(unitModel);
        JTextField txtUnitName = new JTextField();
        JTextField txtRate = new JTextField("1");
        JCheckBox chkBase = new JCheckBox("Đặt làm đơn vị gốc");

        JPanel form = new JPanel(new java.awt.GridLayout(0, 1, 0, 6));
        form.add(new JLabel("Các đơn vị hiện có"));
        form.add(new JScrollPane(unitTable));
        form.add(new JLabel("Tên đơn vị"));
        form.add(txtUnitName);
        form.add(new JLabel("Tỷ lệ quy đổi"));
        form.add(txtRate);
        form.add(chkBase);

        int result = JOptionPane.showConfirmDialog(this, form, "Cấu hình đơn vị cho " + productId, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                boolean ok = new UnitOfMeasureService().configureProductUnit(productId, txtUnitName.getText().trim(), new BigDecimal(txtRate.getText().trim()), chkBase.isSelected());
                if (ok) JOptionPane.showMessageDialog(this, "Đã cập nhật đơn vị tính!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật tỷ lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadProductUnits(String productId, DefaultTableModel model) {
        model.setRowCount(0);
        List<ProductUnit> units = ProductUnitsSql.getInstance().selectByProductId(productId);
        for (ProductUnit unit : units) {
            model.addRow(new Object[]{ unit.getUnitId(), unit.getConversionRateToBase(), unit.getIsBaseUnit() == 1 ? "Có" : "" });
        }
    }

    // ==========================================
    // HÀM XỬ LÝ NHẬP FILE CSV VÀO HỆ THỐNG
    // ==========================================
    private void handleImportCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file CSV sản phẩm để nhập vào hệ thống");
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("File dữ liệu CSV (*.csv)", "csv");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToImport = fileChooser.getSelectedFile();
            int successCount = 0;
            int failCount = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(fileToImport, StandardCharsets.UTF_8))) {
                String line;
                boolean isHeader = true;

                while ((line = br.readLine()) != null) {
                    if (isHeader) {
                        isHeader = false; // Bỏ qua dòng tiêu đề
                        continue;
                    }

                    String[] data = line.split(",");
                    
                    // Mã SP, Tên SP, Giá, Số lượng, Mã Danh Mục
                    if (data.length >= 5) {
                        try {
                            Product p = new Product();
                            p.setProductId(data[0].trim());
                            p.setProductName(data[1].trim());
                            p.setBasePrice(new BigDecimal(data[2].trim()));
                            p.setQuantity(Integer.parseInt(data[3].trim()));
                            p.setCategoryId(data[4].trim()); 
                            
                            p.setSupplierId("SUP001"); 
                            p.setStoreId("ST001");    
                            p.setUnit("Cái");

                            if (ProductsSql.getInstance().insert(p)) {
                                successCount++;
                            } else {
                                failCount++;
                            }
                        } catch (Exception ex) {
                            failCount++;
                            System.err.println("Lỗi dòng dữ liệu: " + line);
                        }
                    }
                }

                JOptionPane.showMessageDialog(this, 
                    "Quá trình nhập dữ liệu kết thúc!\n" +
                    "Thành công: " + successCount + " sản phẩm\n" +
                    "Thất bại: " + failCount + " (Có thể trùng mã hoặc sai định dạng)",
                    "Thông báo Import", JOptionPane.INFORMATION_MESSAGE);
                
                loadDataToTable(); // Tự động load lại bảng

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Không thể đọc file: " + e.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    } 
}