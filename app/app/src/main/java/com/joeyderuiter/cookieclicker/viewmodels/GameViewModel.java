package com.joeyderuiter.cookieclicker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.joeyderuiter.cookieclicker.models.messages.PlayerList;
import com.joeyderuiter.cookieclicker.models.messages.ServerTime;

public class GameViewModel extends ViewModel {
    private final MutableLiveData<PlayerList> players = new MutableLiveData<>();
    private final MutableLiveData<ServerTime> serverTime = new MutableLiveData<>();

    public LiveData<PlayerList> getPlayers() {
        return this.players;
    }

    public void setPlayers(PlayerList players) {
        this.players.postValue(players);
    }

    public MutableLiveData<ServerTime> getServerTime() {
        return serverTime;
    }

    public void setServerTime(ServerTime serverTime) {
        this.serverTime.postValue(serverTime);
    }

}
