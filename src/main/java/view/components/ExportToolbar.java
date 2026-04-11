package view.components;

import javax.swing.*;
import java.awt.*;

/**
 * Thanh công cụ Export PDF/Excel - dùng chung cho tất cả View
 */
public class ExportToolbar extends JPanel {
    private JButton btnExportExcel;
    private JButton btnExportPDF;

    public ExportToolbar() {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 15));

        // Nút Xuất Excel
        btnExportExcel = new JButton("📊 Xuất Excel");
        btnExportExcel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnExportExcel.setBackground(new Color(29, 111, 66)); // Xanh lá
        btnExportExcel.setForeground(Color.WHITE);
        btnExportExcel.setBorderPainted(false);
        btnExportExcel.setFocusPainted(false);
        btnExportExcel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExportExcel.setPreferredSize(new Dimension(120, 32));

        // Nút Xuất PDF
        btnExportPDF = new JButton("📄 Xuất PDF");
        btnExportPDF.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnExportPDF.setBackground(new Color(211, 47, 47)); // Đỏ
        btnExportPDF.setForeground(Color.WHITE);
        btnExportPDF.setBorderPainted(false);
        btnExportPDF.setFocusPainted(false);
        btnExportPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExportPDF.setPreferredSize(new Dimension(120, 32));

        add(btnExportExcel);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(btnExportPDF);
    }

    // Interface để lắng nghe sự kiện
    public interface ExportListener {
        void onExportExcel();
        void onExportPDF();
    }

    private ExportListener listener;

    public void setExportListener(ExportListener listener) {
        this.listener = listener;

        btnExportExcel.addActionListener(e -> {
            if (listener != null) listener.onExportExcel();
        });

        btnExportPDF.addActionListener(e -> {
            if (listener != null) listener.onExportPDF();
        });
    }

    // Getter nút (nếu cần customize thêm)
    public JButton getBtnExportExcel() {
        return btnExportExcel;
    }

    public JButton getBtnExportPDF() {
        return btnExportPDF;
    }
}