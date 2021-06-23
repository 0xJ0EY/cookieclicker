package com.joeyderuiter.cookieclicker.services;

public class StoreServiceLocator {
    public static StoreService instance;

    private StoreServiceLocator() {}

    public synchronized static StoreService getInstance() {
        if (instance == null) {
            instance = new StoreService();
        }

        return instance;
    }
}
