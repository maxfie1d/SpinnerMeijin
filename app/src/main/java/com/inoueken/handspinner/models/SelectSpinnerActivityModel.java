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
    // private Handspinner _currentSpinner;//使用中のスピナー
    private Handspinner[] _handspinners;
    private int[] _spinnerPrice = new int[5];
    private int[] _purchasedSpinners;
    private int _selectedNum;
    private int _coinCount;
    private HandspinnerShop _shop;

    public SelectSpinnerActivityModel() {
        Collection<Handspinner> spinnersCollection;
        this._shop = new HandspinnerShop();
        _selectedNum = 0;
        spinnersCollection = _shop.getHandspinners();
        _handspinners = (Handspinner[]) spinnersCollection.toArray(new Handspinner[spinnersCollection.size()]);
        int i = 0;
        for (Handspinner spinner : _handspinners) {
            _spinnerPrice[i] = spinner.getMetadata().getPrice();
            if (i != 4) i++;
        }
        Arrays.sort(_spinnerPrice);
        i = 0;
        for (int spinner : _spinnerPrice) {
            _handspinners[i] = _shop.getSpinnerByPrice(spinner);
            if (i != 4) i++;
        }
        _selectedSpinner = Player.getPlayer().getCurrentHandspinner();
        i = 0;
        for (Handspinner spinner : _handspinners) {
            if (spinner.getMetadata().getDisplayName().equals(_selectedSpinner.getMetadata().getDisplayName())) {
                _selectedNum = i;
            }
            i++;
        }
        _coinCount = Player.getPlayer().getCoinCount();
    }

    public void onLeftButtonPressed() {
        _selectedSpinner = _handspinners[--_selectedNum];
        // setButtonVisible();
    }

    public void onRightButtonPressed() {

        _selectedSpinner = _handspinners[++_selectedNum];
        //   setButtonVisible();
    }

    public int setButtonVisible() {
        if (_selectedSpinner.getMetadata().getDisplayName().equals("ベーシックスピナー")) {
            return 1;
        } else if (_selectedSpinner.getMetadata().getDisplayName().equals("かき揚げスピナー")) {
            return 2;
        } else return 0;
    }


    public void onSelectButtonPressed() {
        Player.getPlayer().setCurrentHandspinner(_selectedSpinner);
        Player.getPlayer().changeHandspinner(_selectedSpinner.getMetadata().getId(), new HandspinnerShop() );
    }

    public void set_selectedSpinner(Handspinner spinner) {
        _selectedSpinner = spinner;
    }

    /**
     * 購入ボタンを押したときの処理
     */
    public void onPurchaseButtonPressed() {
        Player.getPlayer().buyHandspinner(this._selectedSpinner.getMetadata().getId(), this._shop);
    }


    public boolean judgeAccessRight() {
        return Player.getPlayer().canHaveAccessToHandspinner(_selectedSpinner.getMetadata().getId());
    }

    public Handspinner get_selectedSpinner() {
        return _selectedSpinner;
    }

    public boolean judgeSpinnerSelected(Handspinner spinner){
        if(spinner.getMetadata().getId().equals(_selectedSpinner.getMetadata().getId())) return false;
        else return true;
    }
}
