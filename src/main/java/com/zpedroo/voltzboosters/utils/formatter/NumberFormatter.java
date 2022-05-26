package com.zpedroo.voltzboosters.utils.formatter;

public class NumberFormatter {

    public static String formatDecimal(double number) {
        String[] multiplierSplit = String.valueOf(number).split("\\.");
        double decimal = Double.parseDouble(multiplierSplit[1]);

        return decimal > 0 ? String.format("%.1f", number) : String.format("%.0f", number);
    }
}