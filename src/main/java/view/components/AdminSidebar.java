package view.components;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdminSidebar extends JPanel {
    
    private final List<MenuItem> menuItems;
    private final JPanel menuPanel;
    private MenuClickListener listener;

    public AdminSidebar() {
        this.menuItems = new ArrayList<>();

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, Integer.MAX_VALUE));
        setBackground(Color.WHITE); 
        setBorder(new MatteBorder(0, 0, 0, 1, new Color(230, 230, 230))); 

        // 1. Phần tiêu đề (Branding)
        JPanel brandingPanel = new JPanel();
        brandingPanel.setLayout(new BoxLayout(brandingPanel, BoxLayout.Y_AXIS));
        brandingPanel.setBackground(Color.WHITE);
        brandingPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

        JLabel appName = new JLabel("Cổng Quản Trị");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        appName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Hệ thống điều khiển trung tâm");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(120, 120, 120)); 
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        brandingPanel.add(appName);
        brandingPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        brandingPanel.add(subtitle);

        // 2. Panel chứa các mục Menu
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // THÊM CÁC MỤC MENU TIẾNG VIỆT
        addMenuItem("Tổng quan hệ thống");
        addMenuItem("Quản lý tài khoản"); 
        addMenuItem("Quản lý phân quyền");
        addMenuItem("Nhật ký hệ thống");
        addMenuItem("Cài đặt");

        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // 3. Phần Logout
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        MenuItem logoutItem = new MenuItem("Đăng xuất", () -> {
            if (listener != null) {
                listener.onMenuClick("Đăng xuất");
            }
        });
        logoutItem.setFramed(true); 
        bottomPanel.add(logoutItem, BorderLayout.CENTER);

        add(brandingPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        if (!menuItems.isEmpty()) {
            menuItems.get(0).setActive(true);
        }
    }

    private void addMenuItem(final String title) {
        final MenuItem[] itemHolder = new MenuItem[1];
        MenuItem item = new MenuItem(title, () -> {
            for (MenuItem m : menuItems) {
                m.setActive(false);
            }
            itemHolder[0].setActive(true);
            if (listener != null) {
                listener.onMenuClick(title);
            }
        });
        itemHolder[0] = item;
        menuItems.add(item);
        menuPanel.add(item);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 8))); 
    }

    public void setMenuClickListener(MenuClickListener listener) {
        this.listener = listener;
    }

    public interface MenuClickListener {
        void onMenuClick(String title);
    }
}