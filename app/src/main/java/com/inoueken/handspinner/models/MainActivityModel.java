package com.inoueken.handspinner.models;

import com.inoueken.handspinner.Handspinner;

import rx.Subscription;
import rx.functions.Action1;

public class MainActivityModel {
    private GameManager _gameManager;

    public MainActivityModel() {
        this._gameManager = new GameManager();
    }

    public Subscription subscribeHandspinnerChanged(Action1<Handspinner> action) {
        return this._gameManager.subscribeHandspinnerChanged(action);
    }

    /**
     * View側の準備ができたら呼び出す
     */
    public void getReady() {
        this._gameManager.start();
    }
}
