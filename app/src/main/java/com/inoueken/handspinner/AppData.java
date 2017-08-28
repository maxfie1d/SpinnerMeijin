package com.inoueken.handspinner;

import com.inoueken.handspinner.models.HandspinnerShop;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Serializable;

/**
 * Created by n-isida on 2017/08/08.
 */

public class AppData implements Serializable{
    private int _coinCount;
    private Handspinner _currentSpinner;//現在選択中のスピナー
    private int[] _purchasedSpinners;
    private int SpinnerNum;
    private static final long serialVersionUID = 1L;//データ遷移用変数（消さないで）

    public AppData(HandspinnerShop shop) {
        this.load();
    }

    public void load() {
     /*
        BufferedReader br;
        String str;
        try {
            File file = new File("savedata.txt");
            br = new BufferedReader(new FileReader(file));
            int i = 0;
            while ((str = br.readLine()) != null) {
                if (str.equals("SpinnerNum")) {
                    SpinnerNum = Integer.parseInt(br.readLine());
                }
                if (str.equals("LastUsed")) {
                    _currentSpinner = shop.getSpinnerByName(br.readLine());
                }
                if (str.equals("CoinCount")) {
                    _coinCount = Integer.parseInt(br.readLine());
                }
                _purchasedSpinners[i++] = Integer.parseInt(br.readLine());
            }
            if (SpinnerNum != i) System.out.println("スピナー数が合いません");
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("ファイルが見つかりませんでした");
        } catch (IOException e) {
            System.out.println("読み込みエラー");
        }
        */

    }

    public void save() {

    }

    public int getCoinCount() {
        return _coinCount;
    }

    public Handspinner getLastUsedSpinner() {
        return null;
    }

    public Handspinner get_currentSpinner() {
        return this._currentSpinner;
    }

    public int[] get_purchasedSpinners() {
        return this._purchasedSpinners;
    }

}
