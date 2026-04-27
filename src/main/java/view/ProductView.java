/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

/**
 *
 * @author Admin
 */
import business.sql.prod_inventory.ProductsSql;
import common.utils.Validator;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.product.Product;
import view.components.ExportToolbar;

public class ProductView extends javax.swing.JPanel {

    /**
     * Creates new form ProductView
     */
    private ExportToolbar toolbar; // Khai bÃ¡o toolbar á»Ÿ má»©c class Ä‘á»ƒ dá»… truy cáº­p

    public ProductView() {
        initComponents();

        initTableModel();
        initEvents();
        loadDataToTable();

        // 1. Khá»Ÿi táº¡o ExportToolbar má»›i (chá»©a Auto-Complete ComboBox)
        toolbar = new ExportToolbar(this);
        add(toolbar, BorderLayout.PAGE_START);

        // 2. Gáº®N NÃƒO CHO NÃšT TÃŒM KIáº¾M Tá»ª TOOLBAR
        toolbar.getBtnSearch().addActionListener(e -> {
            String keyword = toolbar.getSearchText();
            // Gá»i xuá»‘ng DB Ä‘á»ƒ tÃ¬m danh sÃ¡ch sáº£n pháº©m khá»›p vá»›i tá»« khÃ³a
            List<Product> filteredList = ProductsSql.getInstance().searchByName(keyword);
            // Äá»• láº¡i dá»¯ liá»‡u lÃªn báº£ng
            fillTable(filteredList);
        });

        this.revalidate();
        this.repaint();
    }

    private void initTableModel() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"MÃ£ sáº£n pháº©m", "TÃªn sáº£n pháº©m", "GiÃ¡", "Sá»‘ lÆ°á»£ng", "Loáº¡i sáº£n pháº©m"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProducts.setModel(model);
    }

    private void initEvents() {
        tblProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ProductView.this.tblProductsMouseClicked(evt); // gá»i hÃ m cá»§a class ngoÃ i
            }
        });
    }

    private void fillTable(List<Product> list) {
        DefaultTableModel model = (DefaultTableModel) tblProducts.getModel();
        model.setRowCount(0);
        for (Product p : list) {
            model.addRow(new Object[]{
                p.getProductId(),
                p.getProductName(),
                p.getBasePrice(),
                p.getQuantity(),
                p.getCategoryId()
            });
        }
    }

    private boolean validateInput() {
        if (Validator.isEmpty(txtName.getText())) {
            JOptionPane.showMessageDialog(this, "TÃªn sáº£n pháº©m khÃ´ng Ä‘Æ°á»£c rá»—ng!");
            return false;
        }
        if (!Validator.isPositiveInteger(txtQuantity.getText())) {
            JOptionPane.showMessageDialog(this, "Sá»‘ lÆ°á»£ng pháº£i lÃ  sá»‘ nguyÃªn dÆ°Æ¡ng!");
            return false;
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(txtPrice.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "GiÃ¡ khÃ´ng há»£p lá»‡!");
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

        if (name.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "TÃªn sáº£n pháº©m khÃ´ng Ä‘Æ°á»£c rá»—ng!");
            return null;
        }
        if (priceText.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "GiÃ¡ khÃ´ng Ä‘Æ°á»£c rá»—ng!");
            return null;
        }
        if (qtyText.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Sá»‘ lÆ°á»£ng khÃ´ng Ä‘Æ°á»£c rá»—ng!");
            return null;
        }
        if (categoryId.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Loáº¡i sáº£n pháº©m (category_id) khÃ´ng Ä‘Æ°á»£c rá»—ng!");
            return null;
        }

        try {
            java.math.BigDecimal price = new java.math.BigDecimal(priceText);
            int qty = Integer.parseInt(qtyText);

            // product_id: tá»± sinh táº¡m thá»i
            String productId = "PROD_" + System.currentTimeMillis();

            p.setProductId(productId);
            p.setProductName(name);
            p.setBasePrice(price);
            p.setQuantity(qty);
            p.setCategoryId(categoryId);

            // táº¡m hard-code Ä‘á»ƒ qua FK (pháº£i tá»“n táº¡i trong DB)
            p.setSupplierId("SUP001");
            p.setStoreId("ST001");
            p.setUnit("CÃ¡i");

            return p;
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "GiÃ¡/Sá»‘ lÆ°á»£ng khÃ´ng há»£p lá»‡!");
            return null;
        }
    }

    private void showPanel(javax.swing.JPanel panel) {
        java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (win instanceof javax.swing.JFrame frame) {
            frame.setContentPane(panel);
            frame.revalidate();
            frame.repaint();
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnForm = new javax.swing.JPanel();
        ProductName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        price = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        Quantity = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        Category = new javax.swing.JLabel();
        txtCategory = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProducts = new javax.swing.JTable();
        pnlButton = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnExportPDF = new javax.swing.JButton();

        setBackground(new java.awt.Color(236, 240, 241));
        setLayout(new java.awt.BorderLayout());

        pnForm.setBackground(new java.awt.Color(236, 240, 241));
        pnForm.setPreferredSize(new java.awt.Dimension(280, 0));
        pnForm.setLayout(new java.awt.GridBagLayout());

        ProductName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ProductName.setText("TÃªn sáº£n pháº©m");
        ProductName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 182;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        pnForm.add(ProductName, gridBagConstraints);

        txtName.addActionListener(this::txtNameActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 210;
        gridBagConstraints.ipady = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        pnForm.add(txtName, gridBagConstraints);

        price.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        price.setText("GiÃ¡");
        price.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 252;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        pnForm.add(price, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 210;
        gridBagConstraints.ipady = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        pnForm.add(txtPrice, gridBagConstraints);

        Quantity.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Quantity.setText("Sá»‘ lÆ°á»£ng");
        Quantity.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 214;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        pnForm.add(Quantity, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 210;
        gridBagConstraints.ipady = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        pnForm.add(txtQuantity, gridBagConstraints);

        Category.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Category.setText("Loáº¡i sáº£n pháº©m");
        Category.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 179;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        pnForm.add(Category, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 210;
        gridBagConstraints.ipady = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 2, 0);
        pnForm.add(txtCategory, gridBagConstraints);

        add(pnForm, java.awt.BorderLayout.LINE_START);

        jScrollPane1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        tblProducts.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tblProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "MÃ£ sáº£n pháº©m", "TÃªn sáº£n pháº©m", "GiÃ¡", "Sá»‘ lÆ°á»£ng", "Loáº¡i sáº£n pháº©m"
            }
        ));
        jScrollPane1.setViewportView(tblProducts);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pnlButton.setBackground(new java.awt.Color(236, 240, 241));
        pnlButton.setPreferredSize(new java.awt.Dimension(0, 60));

        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAdd.setText("ThÃªm");
        btnAdd.addActionListener(this::btnAddActionPerformed);
        pnlButton.add(btnAdd);

        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUpdate.setText("Cáº­p nháº­t");
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);
        pnlButton.add(btnUpdate);

        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDelete.setText("XÃ³a");
        btnDelete.addActionListener(this::btnDeleteActionPerformed);
        pnlButton.add(btnDelete);

        btnClear.setBackground(new java.awt.Color(255, 255, 204));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnClear.setText("LÃ m má»›i");
        btnClear.addActionListener(this::btnClearActionPerformed);
        pnlButton.add(btnClear);

        add(pnlButton, java.awt.BorderLayout.PAGE_END);

        jPanel1.setBackground(new java.awt.Color(236, 240, 241));
        jPanel1.setPreferredSize(new java.awt.Dimension(877, 35));

        txtSearch.addActionListener(this::txtSearchActionPerformed);

        btnSearch.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSearch.setText("TÃ¬m kiáº¿m");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        btnExportPDF.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnExportPDF.setText("Xuáº¥t Execl");
        btnExportPDF.addActionListener(this::btnExportPDFActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(280, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExportPDF)
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(btnExportPDF))
                .addGap(3, 3, 3))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (!validateInput()) {
            return;
        }

        Product p = getProductFromForm();
        if (p == null) {
            return;
        }

        // Náº¿u chÆ°a cÃ³ productId thÃ¬ tá»± sinh
        if (p.getProductId() == null || p.getProductId().trim().isEmpty()) {
            p.setProductId("PROD" + (System.currentTimeMillis() % 1000000));
        }

        // Báº¯t buá»™c cho FK (Ä‘áº£m báº£o cÃ¡c mÃ£ nÃ y tá»“n táº¡i trong DB)
        if (p.getCategoryId() == null || p.getCategoryId().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Loáº¡i sáº£n pháº©m (category_id) khÃ´ng Ä‘Æ°á»£c rá»—ng!");
            return;
        }

        if (p.getSupplierId() == null || p.getSupplierId().trim().isEmpty()) {
            p.setSupplierId("SUP001"); // táº¡m máº·c Ä‘á»‹nh
        }

        if (p.getStoreId() == null || p.getStoreId().trim().isEmpty()) {
            p.setStoreId("ST001"); // táº¡m máº·c Ä‘á»‹nh
        }

        if (p.getUnit() == null || p.getUnit().trim().isEmpty()) {
            p.setUnit("CÃ¡i");
        }

        boolean ok = ProductsSql.getInstance().insert(p);

        if (ok) {
            JOptionPane.showMessageDialog(this, "ThÃªm thÃ nh cÃ´ng!");
            loadDataToTable();
            btnClearActionPerformed(null);
        } else {
            JOptionPane.showMessageDialog(this,
                    "ThÃªm tháº¥t báº¡i! Kiá»ƒm tra category_id / supplier_id / store_id cÃ³ tá»“n táº¡i trong DB.",
                    "Lá»—i", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int row = tblProducts.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chá»n dÃ²ng cáº§n sá»­a!");
            return;
        }

        // Láº¥y ID gá»‘c tá»« cá»™t 0 Ä‘á»ƒ lÃ m Ä‘iá»u kiá»‡n WHERE
        String idOld = tblProducts.getValueAt(row, 0).toString().trim();
        Product p = getProductFromForm();
        if (p == null) {
            return;
        }

        p.setProductId(idOld); // Ã‰p ID cÅ© vÃ o Ä‘á»ƒ SQL tÃ¬m Ä‘Ãºng dÃ²ng

        if (ProductsSql.getInstance().update(p)) {
            JOptionPane.showMessageDialog(this, "Cáº­p nháº­t thÃ nh cÃ´ng!");
            loadDataToTable(); // Äá»’NG Bá»˜ Láº I UI
        } else {
            JOptionPane.showMessageDialog(this, "Tháº¥t báº¡i! Check mÃ£ Loáº¡i/GiÃ¡.");
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int row = tblProducts.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chon dong can xoa!");
            return;
        }

        String id = tblProducts.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xoa san pham " + id + "?",
                "Xac nhan",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean usedInOrders = ProductsSql.getInstance().isUsedInOrders(id);
            if (ProductsSql.getInstance().delete(id)) {
                if (usedInOrders) {
                    JOptionPane.showMessageDialog(this,
                            "San pham da co trong hoa don nen he thong chi an san pham khoi kho/danh sach ban.",
                            "Da an san pham",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Xoa mem san pham thanh cong!");
                }
                loadDataToTable();
                btnClearActionPerformed(null);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Khong the xoa san pham. Co the san pham da bi an hoac khong con ton tai.",
                        "Loi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        txtName.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
        txtCategory.setText("");
        tblProducts.clearSelection();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String keyword = txtSearch.getText().trim();
        List<Product> list = ProductsSql.getInstance().searchByName(keyword);
        fillTable(list);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnExportPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportPDFActionPerformed
        // TODO add your handling code here:
        try {
            // 1. Láº¥y dá»¯ liá»‡u tá»« báº£ng
            List<Map<String, Object>> productList = getAllProductsFromTable();

            if (productList.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "KhÃ´ng cÃ³ dá»¯ liá»‡u Ä‘á»ƒ xuáº¥t!",
                        "ThÃ´ng bÃ¡o",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 2. Chá»n nÆ¡i lÆ°u file
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("LÆ°u file Excel");
            fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
            fileChooser.setSelectedFile(new java.io.File("SanPham_" + System.currentTimeMillis() + ".xlsx"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                // 3. Gá»i service xuáº¥t Excel
                common.report.ExcelExporter.exportInventoryFromMap(productList, filePath);

                // 4. Hiá»ƒn thá»‹ thÃ nh cÃ´ng
                javax.swing.JOptionPane.showMessageDialog(this,
                        "âœ… Xuáº¥t Excel thÃ nh cÃ´ng!\nFile: " + filePath,
                        "ThÃ nh cÃ´ng",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "âŒ Lá»—i xuáº¥t Excel: " + ex.getMessage(),
                    "Lá»—i",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnExportPDFActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void tblProductsMouseClicked(java.awt.event.MouseEvent evt) {
        int row = tblProducts.getSelectedRow();
        if (row < 0) {
            return;
        }

        Object nameObj = tblProducts.getValueAt(row, 1);
        Object priceObj = tblProducts.getValueAt(row, 2);
        Object qtyObj = tblProducts.getValueAt(row, 3);
        Object categoryObj = tblProducts.getValueAt(row, 4);

        txtName.setText(nameObj == null ? "" : nameObj.toString());
        txtPrice.setText(priceObj == null ? "" : priceObj.toString());
        txtQuantity.setText(qtyObj == null ? "" : qtyObj.toString());
        txtCategory.setText(categoryObj == null ? "" : categoryObj.toString());
    }
// Helper: láº¥y táº¥t cáº£ dá»¯ liá»‡u tá»« JTable

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
        DefaultTableModel model = (DefaultTableModel) tblProducts.getModel();
        model.setRowCount(0);

        try {
            List<model.product.Product> list
                    = business.sql.prod_inventory.ProductsSql.getInstance().selectAll();

            for (model.product.Product p : list) {
                Object[] row = {
                    p.getProductId(),
                    p.getProductName(),
                    p.getBasePrice(),
                    p.getQuantity(),
                    p.getCategoryId() // thÃªm cá»™t Loáº¡i SP
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            System.out.println("Lá»—i load báº£ng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        // 1. Sá»­a tblSanPham -> tblProducts
        DefaultTableModel model = (DefaultTableModel) tblProducts.getModel();
        model.setRowCount(0);

        try {
            // 2. Gá»i Ä‘Ãºng Instance cá»§a ProductsSql Ä‘á»ƒ láº¥y dá»¯ liá»‡u
            List<model.product.Product> list = business.sql.prod_inventory.ProductsSql.getInstance().selectAll();

            for (model.product.Product p : list) {
                model.addRow(new Object[]{
                    p.getProductId(),
                    p.getProductName(),
                    p.getBasePrice(),
                    p.getQuantity(),
                    p.getCategoryId() // Sá»­a getCategoryName -> getCategoryId
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Category;
    private javax.swing.JLabel ProductName;
    private javax.swing.JLabel Quantity;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnExportPDF;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnForm;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JLabel price;
    private javax.swing.JTable tblProducts;
    private javax.swing.JTextField txtCategory;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
