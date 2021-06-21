package com.joeyderuiter.cookieclicker.models.game;

import androidx.fragment.app.Fragment;

import lombok.Getter;

public class TabItem {
    @Getter
    private final String name;

    private final CloneableFragment fragment;

    public TabItem(String name, CloneableFragment fragment) {
        this.name = name;
        this.fragment = fragment;
    }

    public Fragment getFragmentClone() {
        return (Fragment) fragment.getClone();
    }
}
