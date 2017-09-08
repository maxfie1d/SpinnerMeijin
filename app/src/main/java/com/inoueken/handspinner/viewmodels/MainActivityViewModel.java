package com.inoueken.handspinner.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.inoueken.handspinner.BR;

import java.text.NumberFormat;

public class MainActivityViewModel extends BaseObservable {
    @Bindable
    private String _coinCount;

    public String getCoinCount() {
        return this._coinCount;
    }

    private NumberFormat priceFormatter;

    public MainActivityViewModel() {
        this._coinCount = "--";
        this.priceFormatter = NumberFormat.getInstance();
    }

    public void setCoinCount(int coinCount) {
        // コンマ区切りにする
        final String formated = this.priceFormatter.format(coinCount);
        this._coinCount = formated;
        notifyPropertyChanged(BR.coinCount);
    }
}
