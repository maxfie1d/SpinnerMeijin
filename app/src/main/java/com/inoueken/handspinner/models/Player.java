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
    private Set<String> _handspinnerAccessRights;
    private BehaviorSubject<Handspinner> _handspinnerChangedEvent;
    private int _coinCount;
    private BehaviorSubject<CountChangedEventArgs> _coinCountChangedEvent;
    private static Player player;

    private Player() {
        this._handspinnerChangedEvent = BehaviorSubject.create();
        this._coinCountChangedEvent = BehaviorSubject.create();
    }

    public static Player getPlayer() {
        if (Player.player == null) {
            Player.player = new Player();
        }
        return player;
    }

    public int getCoinCount() {
        return this._coinCount;
    }


    /**
     * ハンドスピナーのアクセス権限があるかどうかを返す
     *
     * @param handspinnerId 　ハンドスピナーのID
     * @return アクセス権があればtrue, 無ければfalse
     */
    public boolean canHaveAccessToHandspinner(String handspinnerId) {
        return this._handspinnerAccessRights.contains(handspinnerId);
    }

    public Handspinner getCurrentHandspinner() {
        return this._currentHandspinner;
    }

    public void setCurrentHandspinner(Handspinner spinner) {
        this._currentHandspinner = spinner;
    }

    /**
     * メイン画面のスピナーを変更する,アクセス権がなければ例外
     *
     * @param handspinnerId 　ハンドスピナーのID
     * @param shop          　ショップ
     * @return 画像を変更する
     */
    public void changeHandspinner(String handspinnerId, HandspinnerShop shop) {
        if (this._handspinnerAccessRights.contains(handspinnerId)) {
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

    public Subscription subscribeCoinCountChanged(Action1<CountChangedEventArgs> action) {
        return this._coinCountChangedEvent.subscribe(action);
    }

    public void earnCoins(int coinCount) {
        int newCoinCount = this._coinCount + coinCount;
        this.setCoins(newCoinCount);
    }

    public boolean judgeCanBuySpinner(int Price) {
        if (player.getCoinCount() < Price) return false;
        else return true;
    }

    public void buyHandspinner(String handspinnerId, HandspinnerShop shop) {
        final Handspinner spinner = shop.getSpinnerById(handspinnerId);
        if (this.getCoinCount() < spinner.getMetadata().getPrice()) {
            // 例外
        } else {
            // お金を減らす
            final int newCoinCount = this.getCoinCount() - spinner.getMetadata().getPrice();
            this.setCoins(newCoinCount);
            // 権限を与える
            this.giveHandspinnerAccessRights(handspinnerId);
        }
    }

    private void setCoins(int coinCount) {
        int oldCoinCount = this._coinCount;
        this._coinCount = coinCount;
        this._coinCountChangedEvent.onNext(new CountChangedEventArgs(oldCoinCount, this._coinCount));
    }

    public Set<String> getHandspinnerAccessRights() {
        return this._handspinnerAccessRights;
    }

    public void removeHanspinnerAccessRights(String handspinnerId) {
        _handspinnerAccessRights.remove(handspinnerId);
    }

    public void giveHandspinnerAccessRights(String handspinnerId) {
        _handspinnerAccessRights.add(handspinnerId);
    }

    /**
     * プレイヤーデータを読み込む
     */
    public void restoreData(PlayerData playerData, HandspinnerShop shop) {
        // コインの数
        this._coinCount = playerData.coinCount;
        this._coinCountChangedEvent.onNext(new CountChangedEventArgs(-1, this._coinCount));

        // ハンドスピナーの使用権
        Set<String> accessRights = new HashSet<>();
        if (playerData.accessRights.basic_spinner) accessRights.add("basic_spinner");
        if (playerData.accessRights.rare_spinner) accessRights.add("rare_spinner");
        if (playerData.accessRights.legendary_spinner) accessRights.add("legendary_spinner");
        if (playerData.accessRights.ultra_spinner) accessRights.add("ultra_spinner");
        if (playerData.accessRights.kakiage_spinner) accessRights.add("kakiage_spinner");
        this._handspinnerAccessRights = accessRights;

        // 選択中のハンドスピナー
        this.changeHandspinner(playerData.currentSpinner, shop);
    }
}
