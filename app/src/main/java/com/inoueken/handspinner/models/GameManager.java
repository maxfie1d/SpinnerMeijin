package com.inoueken.handspinner.models;


import com.inoueken.handspinner.AppData;
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
        this._player = getPlayer();
        this._handspinnerRotationAngleChanged = BehaviorSubject.create();
    }

    /**
     * アプリ起動時に呼び出す
     */
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

        // プレイヤーデータを読み込む
        AppData appData = new AppData();
        PlayerData playerData = appData.loadPlayerData();
        this._player.restoreData(playerData, this._shop);

        Handspinner spinner = this._player.getCurrentHandspinner();
        spinner.addForce(1.0f);
    }

    /**
     * アプリデータを保存する
     */
    public void save() {
        AppData appData = new AppData();
        appData.savePlayerData(this._player);
    }

    public Subscription subscribeHandspinnerRotationAngleChanged(Action1<Float> action) {
        return this._handspinnerRotationAngleChanged.subscribe(action);
    }

    public Player getPlayer() {
        return Player.getPlayer();
    }
}
