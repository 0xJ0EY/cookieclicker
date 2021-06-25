package com.joeyderuiter.cookieclicker.services;

import android.content.Context;

public class StoreServiceLocator {
    public static StoreService instance;

    private StoreServiceLocator() {}

    public synchronized static StoreService getInstance(Context context) {
        if (instance == null) {
            instance = new StoreService(context);
        }

        return instance;
    }
}
