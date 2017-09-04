package com.inoueken.handspinner.models;

import com.inoueken.handspinner.CountChangedEventArgs;
import com.inoueken.handspinner.Handspinner;

import rx.Subscription;
import rx.functions.Action1;

public class MainActivityModel {
    private GameManager _gameManager;

    public MainActivityModel() {
        this._gameManager = new GameManager();
    }

    public Subscription subscribeHandspinnerChanged(Action1<Handspinner> action) {
        return this._gameManager.getPlayer().subscribeHandspinnerChanged(action);
    }

    public Subscription subscribeCoinCountChanged(Action1<CountChangedEventArgs> action){
        return this._gameManager.getPlayer().subscribeCoinCountChanged(action);
    }

    public Subscription subscribeRotationAngleChanged(Action1<Float> action){
        return this._gameManager.subscribeHandspinnerRotationAngleChanged(action);
    }

    public void stopSimulation(){
        this._gameManager.getPlayer().getCurrentHandspinner().finishRotationTask();
    }

    public Handspinner getCurrentHandspinner(){
        return this._gameManager.getPlayer().getCurrentHandspinner();
    }

    /**
     * View側の準備ができたら呼び出す
     */
    public void getReady() {
        this._gameManager.start();
    }

    public void onStop(){
       this._gameManager.save();
    }
}
