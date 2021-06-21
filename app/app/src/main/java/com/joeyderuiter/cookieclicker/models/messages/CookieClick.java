package com.joeyderuiter.cookieclicker.models.messages;

import lombok.Getter;

public class CookieClick {

    @Getter
    private final boolean clicked;

    public CookieClick(boolean clicked) {
        this.clicked = clicked;
    }
}
