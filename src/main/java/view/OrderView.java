/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import business.sql.sales_order.OrderDetailsSql;
import business.sql.sales_order.OrdersSql;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.order.Order;

/**
 *
 * @author Admin
 */
public class OrderView extends javax.swing.JPanel {
    private static final String STATUS_ALL = "Tat ca";
    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0.##");

    /**
     * Creates new form OrderView
     */
    public OrderView() {
        initComponents();
        
        // Khởi tạo bổ sung theo style thống nhất
        initTableModel();
        initStatusFilter();
        loadDataToTable();
        
        this.revalidate();
        this.repaint();
    }

    // ==========================================
    // Khởi tạo model bảng đúng cột theo DB
    // ==========================================
    private void initTableModel() {
        DefaultTableModel model = new DefaultTableModel(
            new Object [] { "Mã đơn", "Khách hàng", "Ngày", "Tổng tiền", "Trạng thái" }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp trên bảng
            }
        };
        jTable1.setModel(model);
        jTable1.setAutoCreateRowSorter(true);
    }

    private void initStatusFilter() {
        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{STATUS_ALL, "PROCESSING", "COMPLETED", "CANCELLED"}
        ));
        cbStatus.setSelectedItem(STATUS_ALL);
    }

    // ==========================================
    // Load toàn bộ dữ liệu đơn hàng từ DB lên bảng
    // ==========================================
    private void loadDataToTable() {
        try {
            // Sử dụng singleton OrdersSql đã viết
            List<Order> list = OrdersSql.getInstance().selectAll();
            fillTable(list);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách đơn hàng: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillTable(List<Order> list) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        for (Order o : list) {
            model.addRow(new Object[]{
                o.getOrderId(),
                o.getCustomerId(),
                o.getOrderDate(),
                o.getTotalAmount(),
                o.getStatus()
            });
        }
    }

    private int getSelectedModelRow() {
        int viewRow = jTable1.getSelectedRow();
        if (viewRow < 0) {
            return -1;
        }
        return jTable1.convertRowIndexToModel(viewRow);
    }

    private String getSelectedOrderId() {
        int modelRow = getSelectedModelRow();
        if (modelRow < 0) {
            return null;
        }
        return String.valueOf(jTable1.getModel().getValueAt(modelRow, 0));
    }

    private void showOrderDetailsDialog(String orderId) {
        List<Map<String, Object>> details = OrderDetailsSql.getInstance().selectDetailRowsByOrderId(orderId);
        DefaultTableModel detailModel = new DefaultTableModel(
                new Object[]{"Ma SP", "Ten san pham", "So luong", "Don gia", "Thanh tien"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Map<String, Object> detail : details) {
            detailModel.addRow(new Object[]{
                detail.get("product_id"),
                detail.get("product_name") != null ? detail.get("product_name") : detail.get("product_id"),
                detail.get("quantity"),
                moneyFormat.format(detail.get("unit_price")),
                moneyFormat.format(detail.get("line_total"))
            });
        }

        JTable detailTable = new JTable(detailModel);
        detailTable.setAutoCreateRowSorter(true);
        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(detailTable),
                "Chi tiet hoa don " + orderId,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void exportInvoice(String orderId) throws IOException {
        Order order = OrdersSql.getInstance().selectById(orderId);
        if (order == null) {
            throw new IOException("Khong tim thay hoa don: " + orderId);
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chon noi luu hoa don (PDF)");
        chooser.setSelectedFile(new java.io.File("HoaDon_" + orderId + ".pdf"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        List<Map<String, Object>> details = OrderDetailsSql.getInstance().selectDetailRowsByOrderId(orderId);
        
        try {
            com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(chooser.getSelectedFile().getAbsolutePath());
            com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);

            document.add(new com.itextpdf.layout.element.Paragraph("HOA DON BAN HANG")
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                    .setBold().setFontSize(20));

            document.add(new com.itextpdf.layout.element.Paragraph("Ma don: " + order.getOrderId()));
            document.add(new com.itextpdf.layout.element.Paragraph("Khach hang: " + order.getCustomerId()));
            document.add(new com.itextpdf.layout.element.Paragraph("Nhan vien: " + order.getEmployeeId()));
            document.add(new com.itextpdf.layout.element.Paragraph("Ngay: " + order.getOrderDate()));
            document.add(new com.itextpdf.layout.element.Paragraph("Trang thai: " + order.getStatus()));
            document.add(new com.itextpdf.layout.element.Paragraph("Tong tien: " + moneyFormat.format(order.getTotalAmount()) + " VND").setBold());
            document.add(new com.itextpdf.layout.element.Paragraph("\n"));

            com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(5);
            table.addHeaderCell("Ma SP");
            table.addHeaderCell("Ten san pham");
            table.addHeaderCell("SL");
            table.addHeaderCell("Don gia");
            table.addHeaderCell("Thanh tien");

            for (Map<String, Object> detail : details) {
                table.addCell(detail.get("product_id").toString());
                table.addCell(detail.get("product_name") != null ? detail.get("product_name").toString() : detail.get("product_id").toString());
                table.addCell(detail.get("quantity").toString());
                table.addCell(moneyFormat.format(detail.get("unit_price")));
                table.addCell(moneyFormat.format(detail.get("line_total")));
            }
            document.add(table);
            document.close();
            JOptionPane.showMessageDialog(this, "Da xuat hoa don PDF thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Loi khi tao PDF: " + e.getMessage());
        }
    }

    // ==========================================
    // Hàm điều hướng Panel (Thống nhất với Dashboard)
    // ==========================================
    private void showPanel(javax.swing.JPanel panel) {
        java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (win instanceof javax.swing.JFrame frame) {
            frame.setContentPane(panel);
            frame.revalidate();
            frame.repaint();
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnTop = new javax.swing.JPanel();
        Status = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox<>();
        tbOrder = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        pnButton = new javax.swing.JPanel();
        btnDetail = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnIssueAnInvoice = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        pnTop.setBackground(new java.awt.Color(236, 240, 241));
        pnTop.setPreferredSize(new java.awt.Dimension(342, 32));
        pnTop.setLayout(new java.awt.GridBagLayout());

        Status.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Status.setText("Trạng thái");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 184, 0, 0);
        pnTop.add(Status, gridBagConstraints);

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "PROCESSING", "COMPLETED", "CANCELLED" }));
        cbStatus.addActionListener(this::cbStatusActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 12, 5, 172);
        pnTop.add(cbStatus, gridBagConstraints);

        add(pnTop, java.awt.BorderLayout.PAGE_START);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã đơn", "Khách hàng", "Ngày", "Tổng tiền", "Trạng thái"
            }
        ));
        tbOrder.setViewportView(jTable1);

        add(tbOrder, java.awt.BorderLayout.CENTER);

        pnButton.setBackground(new java.awt.Color(236, 240, 241));
        pnButton.setPreferredSize(new java.awt.Dimension(358, 40));
        pnButton.setLayout(new java.awt.GridBagLayout());

        btnDetail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDetail.setText("Xem chi tiết");
        btnDetail.addActionListener(this::btnDetailActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 44, 12, 0);
        pnButton.add(btnDetail, gridBagConstraints);

        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdate.setText("Cập nhật trạng thái");
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 12, 0);
        pnButton.add(btnUpdate, gridBagConstraints);

        btnIssueAnInvoice.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnIssueAnInvoice.setForeground(new java.awt.Color(204, 0, 0));
        btnIssueAnInvoice.setText("Xuất hóa đơn");
        btnIssueAnInvoice.addActionListener(this::btnIssueAnInvoiceActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 12, 59);
        pnButton.add(btnIssueAnInvoice, gridBagConstraints);

        add(pnButton, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int row = getSelectedModelRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn hàng để cập nhật!");
            return;
        }
        
        String orderId = jTable1.getModel().getValueAt(row, 0).toString();
        String currentStatus = jTable1.getModel().getValueAt(row, 4).toString();
        
        // Tạo mảng lựa chọn khớp với DB
        String[] statuses = {"PROCESSING", "COMPLETED", "CANCELLED"};
        String newStatus = (String) JOptionPane.showInputDialog(this, 
                "Chọn trạng thái mới cho đơn hàng " + orderId,
                "Cập nhật trạng thái", JOptionPane.QUESTION_MESSAGE, null, 
                statuses, currentStatus);
        
        if (newStatus != null && !newStatus.equals(currentStatus)) {
            try {
                if (newStatus.equals("CANCELLED")) {
                    String reason = JOptionPane.showInputDialog(this, "Nhập lý do hủy:");
                    if (reason == null) reason = "Admin hủy đơn";
                    boolean success = business.service.PaymentService.cancelOrder(orderId, reason);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Hủy đơn hàng thành công!");
                        loadDataToTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi khi hủy đơn hàng!");
                    }
                } else {
                    int result = OrdersSql.getInstance().updateStatus(orderId, newStatus);
                    
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thành công!");
                        loadDataToTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Khong cap nhat duoc hoa don: " + orderId);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật trạng thái: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        String selected = cbStatus.getSelectedItem().toString();
        if (selected.equals(STATUS_ALL)) {
            try {
                fillTable(OrdersSql.getInstance().selectByCondition(selected));
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }//GEN-LAST:event_cbStatusActionPerformed

    private void btnDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetailActionPerformed
        String orderId = getSelectedOrderId();
        if (orderId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng muốn xem chi tiết!");
            return;
        }
        showOrderDetailsDialog(orderId);
    }//GEN-LAST:event_btnDetailActionPerformed

    private void btnIssueAnInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIssueAnInvoiceActionPerformed
        String orderId = getSelectedOrderId();
        if (orderId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng để xuất hóa đơn!");
            return;
        }
        
        try {
            exportInvoice(orderId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Loi xuat hoa don: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnIssueAnInvoiceActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Status;
    private javax.swing.JButton btnDetail;
    private javax.swing.JButton btnIssueAnInvoice;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel pnButton;
    private javax.swing.JPanel pnTop;
    private javax.swing.JScrollPane tbOrder;
    // End of variables declaration//GEN-END:variables
}
