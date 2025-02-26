package net.darkness.ozonparser.services;

import java.util.regex.Pattern;

public class OzonTextParser {
    public static String extractOrderNumber(String text) {
        var orderNumberPattern = Pattern.compile("Заказ №\\s*(\\d{10}-\\d{4})");
        var orderNumberMatcher = orderNumberPattern.matcher(text);

        if (orderNumberMatcher.find()) {
            return orderNumberMatcher.group(1);
        } else {
            return "Order number not found!";
        }
    }

    public static String extractBarcode(String text) {
        var barcodePattern = Pattern.compile("(\\d{4}\\s\\d{4}\\s\\d\\s\\*\\s\\d{4})");
        var barcodeMatcher = barcodePattern.matcher(text);

        if (barcodeMatcher.find()) {
            return barcodeMatcher.group(1);
        } else {
            return "Barcode not found!";
        }
    }

    public static String extractDate(String text) {
        var datePattern = Pattern.compile("от (\\d+) (\\p{L}+)");
        var dateMatcher = datePattern.matcher(text);

        if (dateMatcher.find()) {
            var day = dateMatcher.group(1);
            var month = dateMatcher.group(2);

            return day + " " + month;
        } else {
            return "Date not found!";
        }
    }

    public static float extractPrice(String text) {
        var pricePattern = Pattern.compile("^([\\d\\s]+[.,\\s]\\d*)\\s*[Р₽®©]$");

        for (var line : text.split("\n")) {
            var priceMatcher = pricePattern.matcher(line);

            if (priceMatcher.find()) {
                var priceString = priceMatcher.group(1)
                        .replace(",", ".")
                        .replace(" ", "");

                int len = priceString.length();
                if (len > 2 && priceString.charAt(len - 3) != '.') {
                    priceString = priceString.substring(0, len - 2) + "." + priceString.substring(len - 2);
                }

                return Float.parseFloat(priceString);
            }
        }

        return -1;
    }
}
