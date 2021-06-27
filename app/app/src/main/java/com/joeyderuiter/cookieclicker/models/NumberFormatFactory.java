package com.joeyderuiter.cookieclicker.models;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatFactory {
    public static NumberFormat getInstance() {
        return NumberFormat.getInstance(new Locale("nl", "nl"));
    }
}
