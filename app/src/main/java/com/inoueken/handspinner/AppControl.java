package com.inoueken.handspinner;

import android.app.Application;

public class AppControl extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("アプリを開始するよ！");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        System.out.println("アプリを終了するよ！");
    }
}
