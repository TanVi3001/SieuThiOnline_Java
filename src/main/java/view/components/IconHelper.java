package view.components;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * IconHelper — Tiện ích load icon PNG dùng chung toàn project.
 *
 * Cách dùng ở bất kỳ panel nào:
 *   ImageIcon icon = IconHelper.load("money-Photoroom.png", 22);
 *
 * File PNG phải nằm trong: src/main/resources/view/image/
 */
public class IconHelper {

    // Thư mục chứa icon trong resources
    private static final String RESOURCE_PATH = "view/image/";
    private static final String FILE_PATH     = "src/main/resources/view/image/";

    // Không cho khởi tạo
    private IconHelper() {}

    /**
     * Load icon PNG và scale về kích thước mong muốn.
     *
     * @param fileName  Tên file, ví dụ: "money-Photoroom.png"
     * @param size      Kích thước pixel (vuông), ví dụ: 22
     * @return ImageIcon đã scale, hoặc null nếu không tìm thấy file
     */
    public static ImageIcon load(String fileName, int size) {
        try {
            // Cách 1: load qua classpath (khi build JAR)
            URL url = IconHelper.class.getClassLoader()
                    .getResource(RESOURCE_PATH + fileName);

            // Cách 2: load trực tiếp từ file khi chạy trong NetBeans/IDE
            if (url == null) {
                File f = new File(FILE_PATH + fileName);
                if (f.exists()) url = f.toURI().toURL();
            }

            if (url == null) {
                System.err.println("[IconHelper] Không tìm thấy icon: " + fileName);
                return null;
            }

            Image scaled = new ImageIcon(url).getImage()
                    .getScaledInstance(size, size, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);

        } catch (Exception e) {
            System.err.println("[IconHelper] Lỗi load icon '" + fileName + "': " + e.getMessage());
            return null;
        }
    }

    // ------------------------------------------------------------------
    // Các icon dùng trong toàn project — đổi tên file tại đây nếu cần
    // ------------------------------------------------------------------
    public static ImageIcon revenue(int size)  { return load("money.png", size); }
    public static ImageIcon customer(int size) { return load("customer.png", size); }
    public static ImageIcon order(int size)    { return load("shopping-cart.png", size); }
    public static ImageIcon barChart(int size) { return load("graph.png", size); }
    public static ImageIcon lineChart(int size){ return load("chart.png", size); }
    public static ImageIcon pieChart(int size) { return load("public-service.png", size); }
    public static ImageIcon stock(int size)    { return load("in-stock.png", size); }
    public static ImageIcon bill(int size)     { return load("bill.png", size); }
}