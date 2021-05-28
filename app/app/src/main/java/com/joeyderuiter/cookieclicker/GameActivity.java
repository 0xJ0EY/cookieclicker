package com.joeyderuiter.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    public static final String ARGS_IP = "com.joeyderuiter.cookieclicker.ARGS_IP";

    private String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        ipAddress = intent.getStringExtra(ARGS_IP);

        System.out.println("ipAddress = " + ipAddress);
    }
}