package utils;

import java.util.*;

public abstract class CurrencyCodes {
    private static final Map<String, String> CURRENCY_CODES;

    static {
        CURRENCY_CODES = new LinkedHashMap<>();
        CURRENCY_CODES.put("United States Dollar", "USD");
        CURRENCY_CODES.put("Argentine Peso", "ARS");
        CURRENCY_CODES.put("Brazilian Real", "BRL");
        CURRENCY_CODES.put("Colombian Peso", "COP");
        CURRENCY_CODES.put("Euros", "EUR");
        CURRENCY_CODES.put("Pound sterling", "GBP");
        CURRENCY_CODES.put("Japanese yen", "JPY");
        CURRENCY_CODES.put("South Korean won", "KRW");
        CURRENCY_CODES.put("Indian rupees", "INR");
        CURRENCY_CODES.put("Canadian dollars", "CAD");
        CURRENCY_CODES.put("Australian dollars", "AUD");
    }

    public static String getCode(String currencyName) {
        return CURRENCY_CODES.get(currencyName);
    }

    public static List<String> getCurrencyNames() {
        return new ArrayList<>(CURRENCY_CODES.keySet());
    }
}
