package com.inoueken.handspinner;

import android.graphics.Matrix;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.inoueken.handspinner.models.HandspinnerShop;


public class MainActivity extends AppCompatActivity {
    private Handler _handler;
    private Runnable _r;
    private ImageView _spinner;
    private Handspinner _handspinnerModel;


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        HandspinnerShop shop = new HandspinnerShop();
        Handspinner spinner = shop.getSpinnerByName("ベーシックスピナー");
        this._handspinnerModel = spinner;

        this.changeSpinner(spinner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this._spinner = (ImageView) findViewById(R.id.spinner);

        // 定期実行ハンドラを登録
        this._handler = new Handler();
        this._r = new Runnable() {
            private float _angle = 0;
            private static final long DelayMills = 4;

            @Override
            public void run() {
                // 少しずつ回す
                final float delta = 23.0f;
                _spinner.setRotation(_angle += delta);

                if (_angle >= 360) {
                    _angle -= 360;
                }
                _handler.postDelayed(this, DelayMills);
            }
        };

        this._handler.post(this._r);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 定期実行ハンドラを削除
        this._handler.removeCallbacks(this._r);
    }

    private void changeSpinner(Handspinner spinner) {
        // 回転中心が合うようにピボット位置を補正する
        this._spinner.setPivotX(this._spinner.getPivotX() * spinner.getMetadata().getPivotXCorrectionScale());
        this._spinner.setPivotY(this._spinner.getPivotY() * spinner.getMetadata().getPivotYCorrectionScale());

        // ハンドスピナーの画像を差し替える
        this._spinner.setImageResource(spinner.getMetadata().getImageId());
    }
}
