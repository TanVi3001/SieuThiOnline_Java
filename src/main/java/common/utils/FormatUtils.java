package common.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {
    // Hàm biến 10000 -> 10.000 đ cho Quỳnh hiển thị lên bảng
    public static String formatMoney(BigDecimal amount) {
        if (amount == null) return "0 đ";
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vn = NumberFormat.getCurrencyInstance(localeVN);
        return vn.format(amount);
    }

    // Hàm check xem user có nhập "láo" (nhập chữ vào ô số lượng) không
    public static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}