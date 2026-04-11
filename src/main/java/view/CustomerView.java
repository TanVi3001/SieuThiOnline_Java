package view;

import business.sql.sales_order.CustomersSql;
import common.utils.Validator;
import java.awt.Window;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.order.Customer;

/**
 *
 * @author Admin
 */
public class CustomerView extends javax.swing.JPanel {

    /**
     * Creates new form CustomerView
     */
    public CustomerView() {
        initComponents();

        javax.swing.JTextField[] fields = {txtCustomerName, txtPhone, txtEmail, txtAddress};
        for (javax.swing.JTextField f : fields) {
            f.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 30));
            f.setMinimumSize(new java.awt.Dimension(100, 30));
            f.setPreferredSize(new java.awt.Dimension(250, 30));
        }
        txtAddress.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 30));
        txtAddress.setPreferredSize(new java.awt.Dimension(200, 30));

        initEvents();
        initTableModel();
        loadDataToTable();

        this.revalidate();
        this.repaint();
    }

    // =========================
    // Khởi tạo model bảng đúng cột (thêm cột địa chỉ)
    // =========================
    private void initTableModel() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Email", "Địa chỉ"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(model);
    }

    // =========================
    // Gắn event cho các nút và bảng
    // =========================
    private void initEvents() {
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
    }

    // =========================
    // Load toàn bộ dữ liệu từ DB lên bảng
    // =========================
    private void loadDataToTable() {
        try {
            List<Customer> list = CustomersSql.getInstance().selectAll();
            fillTable(list);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Hàm đổ list vào bảng (dùng chung cho load + search)
    private void fillTable(List<Customer> list) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        for (Customer c : list) {
            model.addRow(new Object[]{
                c.getCustomerID(),
                c.getCustomerName(),
                c.getPhone(),
                c.getEmail(),
                c.getAddress()
            });
        }
    }

    // =========================
    // Validate input
    // =========================
    private boolean validateInput(boolean checkCustomerId) {
        String customerId = txtCustomerID.getText().trim();
        String customerName = txtCustomerName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();

        if (checkCustomerId && (customerId.isEmpty() || !Validator.isNotEmpty(customerId))) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng không được để trống!");
            txtCustomerID.requestFocus();
            return false;
        }
        if (customerName.isEmpty() || !Validator.isNotEmpty(customerName)) {
            JOptionPane.showMessageDialog(this, "Tên khách hàng không được để trống!");
            txtCustomerName.requestFocus();
            return false;
        }
        if (!Validator.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!");
            txtPhone.requestFocus();
            return false;
        }
        if (!Validator.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!");
            txtEmail.requestFocus();
            return false;
        }
        return true;
    }

    // Build object Customer từ form
    private Customer getCustomerFromForm(String customerId) {
        Customer c = new Customer();
        c.setCustomerID(customerId);
        c.setCustomerName(txtCustomerName.getText().trim());
        c.setPhone(txtPhone.getText().trim());
        c.setEmail(txtEmail.getText().trim());
        c.setAddress(txtAddress.getText().trim());
        return c;
    }

    // =========================
    // MouseClicked jTable1
    // =========================
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        int row = jTable1.getSelectedRow();
        if (row < 0) {
            return;
        }

        txtCustomerID.setText(String.valueOf(jTable1.getValueAt(row, 0)));
        txtCustomerName.setText(String.valueOf(jTable1.getValueAt(row, 1)));
        txtPhone.setText(String.valueOf(jTable1.getValueAt(row, 2)));
        txtEmail.setText(String.valueOf(jTable1.getValueAt(row, 3)));

        // an toàn nếu model chưa có cột địa chỉ
        if (jTable1.getColumnCount() > 4) {
            txtAddress.setText(String.valueOf(jTable1.getValueAt(row, 4)));
        } else {
            txtAddress.setText("");
        }
    }

    private void showPanel(javax.swing.JPanel panel) {
        java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor(this);

        if (win instanceof javax.swing.JFrame frame) {
            frame.setContentPane(panel);
            frame.revalidate();
            frame.repaint();
        } else if (win instanceof javax.swing.JDialog dialog) {
            dialog.setContentPane(panel);
            dialog.revalidate();
            dialog.repaint();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnIn4Customer = new javax.swing.JPanel();
        CustomerID = new javax.swing.JLabel();
        txtCustomerID = new javax.swing.JTextField();
        CustomerName = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        Phone = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        Email = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        Address = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        tbCustomer = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        pnButton = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        pnTop = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        pnIn4Customer.setBackground(new java.awt.Color(236, 240, 241));
        pnIn4Customer.setPreferredSize(new java.awt.Dimension(280, 0));

        CustomerID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CustomerID.setText("Mã khách hàng");

        txtCustomerID.addActionListener(this::txtCustomerIDActionPerformed);

        CustomerName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CustomerName.setText("Tên khách hàng");

        txtCustomerName.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txtCustomerName.setPreferredSize(new java.awt.Dimension(250, 30));
        txtCustomerName.addActionListener(this::txtCustomerNameActionPerformed);

        Phone.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Phone.setText("Số điện thoại");

        txtPhone.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txtPhone.setPreferredSize(new java.awt.Dimension(250, 30));
        txtPhone.addActionListener(this::txtPhoneActionPerformed);

        Email.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Email.setText("Email");

        txtEmail.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txtEmail.setPreferredSize(new java.awt.Dimension(250, 30));
        txtEmail.addActionListener(this::txtEmailActionPerformed);

        Address.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Address.setText("Địa chỉ");

        txtAddress.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txtAddress.setPreferredSize(new java.awt.Dimension(200, 30));
        txtAddress.addActionListener(this::txtAddressActionPerformed);

        javax.swing.GroupLayout pnIn4CustomerLayout = new javax.swing.GroupLayout(pnIn4Customer);
        pnIn4Customer.setLayout(pnIn4CustomerLayout);
        pnIn4CustomerLayout.setHorizontalGroup(
            pnIn4CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnIn4CustomerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnIn4CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CustomerID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnIn4CustomerLayout.createSequentialGroup()
                        .addGroup(pnIn4CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCustomerID, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Address, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnIn4CustomerLayout.setVerticalGroup(
            pnIn4CustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnIn4CustomerLayout.createSequentialGroup()
                .addComponent(CustomerID, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(txtCustomerID, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(CustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Address, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        add(pnIn4Customer, java.awt.BorderLayout.LINE_START);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Email", "Địa chỉ"
            }
        ));
        tbCustomer.setViewportView(jTable1);

        add(tbCustomer, java.awt.BorderLayout.CENTER);

        pnButton.setBackground(new java.awt.Color(236, 240, 241));
        pnButton.setPreferredSize(new java.awt.Dimension(0, 60));

        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(this::btnThemActionPerformed);
        pnButton.add(btnThem);

        btnCapNhat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCapNhat.setText("Cập nhật");
        btnCapNhat.addActionListener(this::btnCapNhatActionPerformed);
        pnButton.add(btnCapNhat);

        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(this::btnXoaActionPerformed);
        pnButton.add(btnXoa);

        btnLamMoi.setBackground(new java.awt.Color(255, 255, 204));
        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(this::btnLamMoiActionPerformed);
        pnButton.add(btnLamMoi);

        add(pnButton, java.awt.BorderLayout.PAGE_END);

        pnTop.setBackground(new java.awt.Color(236, 240, 241));
        pnTop.setPreferredSize(new java.awt.Dimension(0, 40));

        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        btnSearch.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSearch.setText("Tìm kiếm");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        javax.swing.GroupLayout pnTopLayout = new javax.swing.GroupLayout(pnTop);
        pnTop.setLayout(pnTopLayout);
        pnTopLayout.setHorizontalGroup(
            pnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTopLayout.createSequentialGroup()
                .addContainerGap(280, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSearch)
                .addGap(16, 16, 16))
        );
        pnTopLayout.setVerticalGroup(
            pnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTopLayout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(pnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addContainerGap())
        );

        add(pnTop, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCustomerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerIDActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        try {
            if (!validateInput(true)) {
                return;
            }

            Customer customer = getCustomerFromForm(txtCustomerID.getText().trim());
            int result = CustomersSql.getInstance().insert(customer);

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                loadDataToTable();
                btnLamMoiActionPerformed(null);
            } else {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm khách hàng: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        // TODO add your handling code here:
        try {
            int row = jTable1.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng trên bảng để cập nhật!");
                return;
            }
            String customerId = String.valueOf(jTable1.getValueAt(row, 0));

            if (!validateInput(false)) {
                return;
            }

            Customer customer = getCustomerFromForm(customerId);
            int result = CustomersSql.getInstance().update(customer);

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!");
                loadDataToTable();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật khách hàng: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        try {
            int row = jTable1.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
                return;
            }

            String customerId = String.valueOf(jTable1.getValueAt(row, 0));
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn xóa khách hàng mã '" + customerId + "' không?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            int result = CustomersSql.getInstance().delete(customerId);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                loadDataToTable();
                btnXoaActionPerformed(null);
            } else {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa khách hàng: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
        txtCustomerID.setText("");
        txtCustomerName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        jTextField1.setText("");
        jTable1.clearSelection();
        txtCustomerID.requestFocus();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        try {
            String keyword = jTextField1.getText().trim();
            List<Customer> result = CustomersSql.getInstance().search(keyword);
            fillTable(result);
            JOptionPane.showMessageDialog(this, "Tìm thấy " + result.size() + " kết quả.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void txtCustomerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerNameActionPerformed

    private void txtPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Address;
    private javax.swing.JLabel CustomerID;
    private javax.swing.JLabel CustomerName;
    private javax.swing.JLabel Email;
    private javax.swing.JLabel Phone;
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel pnButton;
    private javax.swing.JPanel pnIn4Customer;
    private javax.swing.JPanel pnTop;
    private javax.swing.JScrollPane tbCustomer;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtCustomerID;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtPhone;
    // End of variables declaration//GEN-END:variables
}
