package com.inoueken.handspinner.models;

import android.support.annotation.NonNull;

import com.inoueken.handspinner.AppData;
import com.inoueken.handspinner.Handspinner;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by n-isida on 2017/08/08.
 */

public class SelectSpinnerActivityModel {
    private Handspinner _selectedSpinner;//画面表示中のスピナー
    private Handspinner _currentSpinner;//使用中のスピナー
    private Handspinner[] _handspinners;
    private int[] _purchasedSpinners;
    private int _selectedNum;
    private int _coinCount;

    public SelectSpinnerActivityModel(AppData appData) {
        Collection<Handspinner> spinnersCollection;
        HandspinnerShop shop = new HandspinnerShop();
        _selectedNum = 0;
        spinnersCollection = shop.getHandspinners();
        _handspinners = (Handspinner[]) spinnersCollection.toArray(new Handspinner[spinnersCollection.size()]);
        while (appData.get_currentSpinner() != _handspinners[_selectedNum]) _selectedNum++;
        _selectedSpinner = _handspinners[_selectedNum];
        _purchasedSpinners = appData.get_purchasedSpinners();
        _coinCount = appData.getCoinCount();
        //左端右端時のボタンの非表示化
    }

    public void onLeftButtonPressed() {
        _selectedSpinner = _handspinners[--_selectedNum];
    }

    public void onRightButtonPressed() {
        _selectedSpinner = _handspinners[++_selectedNum];
    }

    public void onSelectButtonPressed(){
        _currentSpinner = _selectedSpinner;
    }

    public void onBackToMainButtonPressed(){

    }


    public void set_selectedSpinner(Handspinner spinner) {
        _selectedSpinner = spinner;
    }

    public void onPurchaseButtonPressed() {

        //現在表示されているスピナーが購入可能か？

        if (this._purchasedSpinners[_selectedNum] == 1) {
             //購入済み表示
        } else {
             if (_coinCount >= _selectedSpinner.getMetadata().getPrice()) {
                _purchasedSpinners[_selectedNum] = 1;
              } else {
                 //購入不可
             }
        }


    }
}
