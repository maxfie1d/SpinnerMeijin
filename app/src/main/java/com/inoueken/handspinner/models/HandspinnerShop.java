package com.inoueken.handspinner.models;

import com.inoueken.handspinner.Handspinner;
import com.inoueken.handspinner.R;

import java.util.Collection;
import java.util.HashSet;


/**
 * ハンドスピナーのお店を表すクラスやで
 */
public class HandspinnerShop {
    private HashSet<Handspinner> _handspinners;

    public HandspinnerShop() {
        this._handspinners = new HashSet<>();

        // ベーシックスピナー
        Handspinner basicSpinner = new Handspinner(
                new HandspinnerMetadata(
                        "ベーシックスピナー",
                        1,
                        "何の変哲もない普通のハンドスピナー。初心者はまずこれを使う。",
                        0,
                        1.00f,
                        1.175f,
                        "basic_spinner.png",
                        R.drawable.basic_spinner));

        // レアスピナー
        Handspinner rareSpinner = new Handspinner(
                new HandspinnerMetadata(
                        "レアスピナー",
                        2,
                        "普通のお店には売っていないレアなハンドスピナー。気持ちベーシックスピナーよりよく回る気がする。",
                        1 * 1000,
                        1.00f,
                        1.175f,
                        "rare_spinner.png",
                        R.drawable.rare_spinner));

        // 伝説のスピナー
        Handspinner legendarySpinner = new Handspinner(
                new HandspinnerMetadata(
                        "伝説のスピナー",
                        3,
                        "世界に1つしかないハンドスピナー。岩に刺さっていたところを抜いてきた。回す人間の能力が試される。",
                        10 * 1000,
                        1.00f,
                        1.215f,
                        "legendary_spinner.png",
                        R.drawable.legendary_spinner));

        // ウルトラスーパーハイスペックスピナー(長いのでウルトラスピナーと省略)
        Handspinner ultraSpinner = new Handspinner(
                new HandspinnerMetadata(
                        "ウルトラスーパーハイスペックスピナー",
                        4,
                        "どこか宇宙を感じる壮大なスピナー。このスピナーが止まっているところを見た者はいない。",
                        100 * 1000,
                        1.041f,
                        1.242f,
                        "ultra_spinner.png",
                        R.drawable.ultra_spinner));

        // かき揚げスピナー
        Handspinner kakiageSpinner = new Handspinner(
                new HandspinnerMetadata(
                        "かき揚げスピナー",
                        0,
                        "何の変哲もない普通のハンドスピナー。ついにかき揚げがハンドスピナーになった。全体的に油まみれでベトベト。もうﾏｼﾞむり。。",
                        1 * 1000 * 1000,
                        1.00f,
                        1.00f,
                        "kakiage_spinner.png",
                        R.drawable.kakiage_spinner));

        final Handspinner[] handspinners = {basicSpinner, rareSpinner, legendarySpinner, ultraSpinner, kakiageSpinner};
        for (Handspinner handspinner : handspinners) {
            this._handspinners.add(handspinner);
        }
    }

    public Collection<Handspinner> getHandspinners() {
        return this._handspinners;
    }

    public Handspinner getSpinnerByName(String name) {
        for (Handspinner spinner : this._handspinners) {
            if(spinner.getMetadata().getDisplayName().equals(name)){
                return spinner;
            }
        }
        return null;
    }
}
