/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common.utils;

/**
 *
 * @author Le Tan Vi
 */
public class Validator {
    // Check xem các ô có bị bỏ trống không
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Check xem có phải số nguyên dương không (cho ô Số lượng)
    public static boolean isPositiveInteger(String str) {
        try {
            int n = Integer.parseInt(str);
            return n >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}