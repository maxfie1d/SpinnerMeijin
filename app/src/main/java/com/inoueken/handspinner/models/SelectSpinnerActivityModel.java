package com.inoueken.handspinner.models;

import com.inoueken.handspinner.Handspinner;

/**
 * Created by n-isida on 2017/08/08.
 */

public class SelectSpinnerActivityModel {
    private Handspinner _currentSpinner;//使用中のスピナー
    private int _selectedSpinnerNum;//画面選択されているスピナー

    public void onLeftButtonPressed() {
        //左端確認
        _selectedSpinnerNum--;
    }

    public void onRightButtonPressed() {
        //右端確認
        _selectedSpinnerNum++;
    }

    public void onSpinnerPressed() {

    }

    public void onBuyButtonPressed() {
        /*
        現在表示されているスピナーが購入可能か？

        if (this._currentSpinner.buyFlag == 1) {
             //購入済み表示
        } else {
             if (AppData.getCoinCount() >= _currentSpinner.cost) {
                _currentSpinner.buyFlag == 1;
              } else //購入不可;
        }

        */
    }
}
