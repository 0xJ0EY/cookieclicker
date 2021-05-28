package com.joeyderuiter.cookieclicker.services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LobbyService {

    final static String DATABASE_TABLE = "servers";

    private final DatabaseReference database;

    public LobbyService() {
        this.database = FirebaseDatabase
                .getInstance()
                .getReference(DATABASE_TABLE);
    }

    public DatabaseReference getLobbyList() {
        return this.database.getRef();
    }
}
