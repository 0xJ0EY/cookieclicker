package com.joeyderuiter.cookieclicker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.joeyderuiter.cookieclicker.models.messages.PlayerList;

public class GameViewModel extends ViewModel {
    private final MutableLiveData<PlayerList> players = new MutableLiveData<>();

    public LiveData<PlayerList> getPlayers() {
        return this.players;
    }

    public void setPlayers(PlayerList players) {
        this.players.postValue(players);
    }
}
