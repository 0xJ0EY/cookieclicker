package com.joeyderuiter.cookieclicker.models.game;

import androidx.fragment.app.Fragment;

public abstract class CloneableFragment extends Fragment implements Cloneable {
    public CloneableFragment getClone() {
        try {
            return (CloneableFragment) this.clone();
        } catch (Exception ex) {
            return null;
        }
    }
}
