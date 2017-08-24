package com.inoueken.handspinner.models;

import com.inoueken.handspinner.Handspinner;

import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

public class MainActivityModel {
    private Handspinner _currentHandspinnerModel;
    private BehaviorSubject<Handspinner> _handspinnerChangedEvent;


    public MainActivityModel() {
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

    /**
     * View側の準備ができたら呼び出す
     */
    public void getReady(){
       this.changeHandspinner(this._currentHandspinnerModel);
    }
}
