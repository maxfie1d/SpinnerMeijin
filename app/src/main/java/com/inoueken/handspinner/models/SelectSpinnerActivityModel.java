package com.inoueken.handspinner.models;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.inoueken.handspinner.AppData;
import com.inoueken.handspinner.Handspinner;

import java.util.Collection;
import java.util.Iterator;

import android.view.View;
import android.widget.ImageView;

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
    public int setButtonVisible(){
        if(_selectedNum==0){
            return 1;
        }else if(_selectedNum==4){
            return 2;
        }else return 0;
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


    public boolean judgeAccessRight(){
        if(true){//購入権を持っていたら
            return true;
        }else{
            return false;
        }

    }
}
