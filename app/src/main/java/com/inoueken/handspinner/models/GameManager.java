package com.inoueken.handspinner.models;


public class GameManager {
    private Player _player;
    private HandspinnerShop _shop;

    public GameManager() {
        this._shop = new HandspinnerShop();
        this._player = new Player();
    }

    public void start() {
        this._player.changeHandspinner("basic_spinner", this._shop);
    }

    public Player getPlayer(){
        return this._player;
    }
}
