package com.inoueken.handspinner.models;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.inoueken.handspinner.AppData;
import com.inoueken.handspinner.Handspinner;

import java.util.Collection;
import java.util.Iterator;

import android.view.View;
import android.widget.ImageView;

import java.util.Arrays;

/**
 * Created by n-isida on 2017/08/08.
 */

public class SelectSpinnerActivityModel {
    private Handspinner _selectedSpinner;//画面表示中のスピナー
    private Handspinner _currentSpinner;//使用中のスピナー
    private Handspinner[] _handspinners;
    private int[] _spinnerPrice = new int[5];
    private int[] _purchasedSpinners;
    private int _selectedNum;
    private int _coinCount;

    public SelectSpinnerActivityModel(AppData appData) {
        Collection<Handspinner> spinnersCollection;
        HandspinnerShop shop = new HandspinnerShop();
        _selectedNum = 0;
        spinnersCollection = shop.getHandspinners();
        _handspinners = (Handspinner[]) spinnersCollection.toArray(new Handspinner[spinnersCollection.size()]);
        int i = 0;
        for (Handspinner spinner : _handspinners) {
            _spinnerPrice[i] = spinner.getMetadata().getPrice();
            if (i != 4) i++;
        }
        Arrays.sort(_spinnerPrice);
        i = 0;
        for (int spinner : _spinnerPrice) {
            _handspinners[i] = shop.getSpinnerByPrice(spinner);
            if(i!=4)i++;
        }
//        while (appData.get_currentSpinner().getMetadata().getDisplayName().equals(_handspinners[_selectedNum].getMetadata().getDisplayName()) ) _selectedNum++;
        _selectedSpinner = _handspinners[_selectedNum];
//        _coinCount = appData.getCoinCount();
        //左端右端時のボタンの非表示化
    }

    public void onLeftButtonPressed() {
        _selectedSpinner = _handspinners[--_selectedNum];
        setButtonVisible();
    }

    public void onRightButtonPressed() {

        _selectedSpinner = _handspinners[++_selectedNum];
        setButtonVisible();
    }

    public int setButtonVisible() {
        if (_selectedNum == 0) {
            return 1;
        } else if (_selectedNum == 4) {
            return 2;
        } else return 0;
    }


    public void onSelectButtonPressed() {
        _currentSpinner = _selectedSpinner;
    }
/*
    public void onBackToMainButtonPressed(){

    }
*/

    public void set_selectedSpinner(Handspinner spinner) {
        _selectedSpinner = spinner;
    }

    public void onPurchaseButtonPressed() {

    }


    public boolean judgeAccessRight() {
        if (true) {//購入権を持っていたら
            return true;
        } else {
            return false;
        }
    }

    public Handspinner get_selectedSpinner() {
        return _selectedSpinner;
    }
}
