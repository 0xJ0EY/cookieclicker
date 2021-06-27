package com.joeyderuiter.cookieclicker.services;

import android.content.Context;

public class EndServiceLocator {
    public static EndService instance;

    private EndServiceLocator() {}

    public synchronized static EndService getInstance(Context context) {
        if (instance == null) {
            instance = new EndService(context);
        }

        return instance;
    }
}
