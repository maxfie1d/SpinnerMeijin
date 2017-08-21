package com.inoueken.handspinner;

/**
 * Created by n-isida on 2017/08/08.
 */

public class AppData {
    private int _coinCount;
    private Handspinner[] HandspinnerList;

    public AppData(){
        this.load();
    }

    public void load(){
        _coinCount = this.getCoinCount();
        HandspinnerList[0] = new Handspinner();

        /*
        設定情報ファイルから読み込み
         */
    }

    public void save(){

    }

    public int getCoinCount(){
        return 1000;
    }

    public Handspinner getLastUsedSpinner(){
        return null;
    }

}
