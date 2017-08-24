package com.inoueken.handspinner.models;

import com.inoueken.handspinner.CountChangedEventArgs;
import com.inoueken.handspinner.Handspinner;

import java.util.HashSet;
import java.util.Set;

import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

public class Player {
    // ハンドスピナーの所有権を格納する
    private Handspinner _currentHandspinner;
    private Set<String> _handspinnerAccesses;
    private BehaviorSubject<Handspinner> _handspinnerChangedEvent;
    private int _coinCount;
    private BehaviorSubject<CountChangedEventArgs> _coinCountChangedEvent;

    public Player() {
        this._handspinnerAccesses = new HashSet<>();
        this._handspinnerAccesses.add("basic_spinner");

        this._handspinnerChangedEvent = BehaviorSubject.create();
        this._coinCountChangedEvent = BehaviorSubject.create();
        this._coinCount = 0;
    }

    public int getCoinCount() {
        return this._coinCount;
    }

    public boolean canHaveAccessToHandspinner(String handspinnerId) {
        return this._handspinnerAccesses.contains(handspinnerId);
    }

    public Handspinner getCurrentHandspinner() {
        return this._currentHandspinner;
    }

    public void changeHandspinner(String handspinnerId, HandspinnerShop shop) {
        if (this._handspinnerAccesses.contains(handspinnerId)) {
            final Handspinner spinner = shop.getSpinnerById(handspinnerId);
            this._currentHandspinner = spinner;
            this._handspinnerChangedEvent.onNext(spinner);
        } else {
            // ハンドスピナーの使用権限が無い場合は例外を投げる
            throw new Error("You don't have access to handspinner: " + handspinnerId);
        }
    }

    public Subscription subscribeHandspinnerChanged(Action1<Handspinner> action) {
        return this._handspinnerChangedEvent.subscribe(action);
    }

    public Subscription subscribeCoinCountChanged(Action1<CountChangedEventArgs> action){
        return this._coinCountChangedEvent.subscribe(action);
    }

    public void earnCoins(int coinCount) {
        final int oldCoinCount = this._coinCount;
        this._coinCount += coinCount;
        this._coinCountChangedEvent.onNext(new CountChangedEventArgs(oldCoinCount, this._coinCount));
    }
}
