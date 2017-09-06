package com.inoueken.handspinner.models;

import com.inoueken.handspinner.AppData;
import com.inoueken.handspinner.Handspinner;

import java.util.Collection;

import java.util.Arrays;


public class SelectSpinnerActivityModel {
    private Handspinner _selectedSpinner;//画面表示中のスピナー
    private Handspinner[] _handspinners;
    private int[] _spinnerPrice = new int[5];
    private int _selectedNum;
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
    }

    public void onLeftButtonPressed() {
        _selectedSpinner = _handspinners[--_selectedNum];
    }

    public void onRightButtonPressed() {
        _selectedSpinner = _handspinners[++_selectedNum];
    }

    public int setButtonVisible() {
        if (_selectedSpinner.getMetadata().getDisplayName().equals("ベーシックスピナー")) {
            return 1;
        } else if (_selectedSpinner.getMetadata().getDisplayName().equals("かき揚げスピナー")) {
            return 2;
        } else {
            return 0;
        }
    }

    /**
     * 「えらぶ」ボタンを押した時の処理
     */
    public void onSelectButtonPressed() {
        Player.getPlayer().setCurrentHandspinner(_selectedSpinner);
        Player.getPlayer().changeHandspinner(_selectedSpinner.getMetadata().getId(), new HandspinnerShop());

        // 設定を保存する
        this.saveData();
    }

    private void saveData() {
        AppData appData = new AppData();
        appData.savePlayerData(Player.getPlayer());
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

    public boolean judgeSpinnerSelected(Handspinner spinner) {
        return !spinner.getMetadata().getId().equals(_selectedSpinner.getMetadata().getId());
    }
}
