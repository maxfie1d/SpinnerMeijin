package com.inoueken.handspinner.models;

import com.inoueken.handspinner.Handspinner;

import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

public class GameManager {
    private Handspinner _currentHandspinnerModel;
    private BehaviorSubject<Handspinner> _handspinnerChangedEvent;


    public GameManager() {
        this._handspinnerChangedEvent = BehaviorSubject.create();

        HandspinnerShop shop = new HandspinnerShop();
        Handspinner spinner = shop.getSpinnerByName("ベーシックスピナー");
        this._currentHandspinnerModel = spinner;
    }

    private void changeHandspinner(Handspinner handspinner) {
        this._handspinnerChangedEvent.onNext(handspinner);
    }

    public Subscription subscribeHandspinnerChanged(Action1<Handspinner> action) {
        return this._handspinnerChangedEvent.subscribe(action);
    }

    public void start() {
        this.changeHandspinner(this._currentHandspinnerModel);
    }
}
