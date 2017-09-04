package com.inoueken.handspinner.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.inoueken.handspinner.BR;

public class MainActivityViewModel extends BaseObservable {
    @Bindable
    private String _coinCount;

    public String getCoinCount() {
        return this._coinCount;
    }


    public MainActivityViewModel() {
        this._coinCount = "--";
    }

    public void setCoinCount(int coinCount) {
        this._coinCount = String.valueOf(coinCount);
        notifyPropertyChanged(BR.coinCount);
    }
}
