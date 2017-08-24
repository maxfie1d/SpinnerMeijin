package com.inoueken.handspinner.models;


import com.inoueken.handspinner.CountChangedEventArgs;
import com.inoueken.handspinner.Handspinner;

import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

public class GameManager {
    private Player _player;
    private HandspinnerShop _shop;
    private Subscription _oldRotationCountChangedSubscription;
    private Subscription _oldRotationAngleChangedSubscription;
    private BehaviorSubject<Float> _handspinnerRotationAngleChanged;

    public GameManager() {
        this._shop = new HandspinnerShop();
        this._player = new Player();
        this._handspinnerRotationAngleChanged = BehaviorSubject.create();
    }

    public void start() {
        this._player.subscribeHandspinnerChanged(new Action1<Handspinner>() {
            @Override
            public void call(Handspinner handspinner) {
                if (_oldRotationCountChangedSubscription != null) {
                    _oldRotationCountChangedSubscription.unsubscribe();
                }
                if (_oldRotationAngleChangedSubscription != null) {
                    _oldRotationAngleChangedSubscription.unsubscribe();
                }

                _oldRotationCountChangedSubscription = handspinner.subscribeRotationCountChanged(new Action1<CountChangedEventArgs>() {
                    @Override
                    public void call(CountChangedEventArgs countChangedEventArgs) {
                        final int delta = countChangedEventArgs.getNewCount() - countChangedEventArgs.getOldCount();
                        _player.earnCoins(delta);
                    }
                });

                _oldRotationAngleChangedSubscription = handspinner.subscribeRotationAngleChanged(new Action1<Float>() {
                    @Override
                    public void call(Float angle) {
                        _handspinnerRotationAngleChanged.onNext(angle);
                    }
                });
            }
        });

        //
        this._player.changeHandspinner("basic_spinner", this._shop);
        Handspinner spinner = this._player.getCurrentHandspinner();
        spinner.rotate();
        spinner.addForce(1.0f);
    }

    public Subscription subscribeHandspinnerRotationAngleChanged(Action1<Float> action) {
        return this._handspinnerRotationAngleChanged.subscribe(action);
    }

    public Player getPlayer() {
        return this._player;
    }
}
