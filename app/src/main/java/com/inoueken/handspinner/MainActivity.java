package com.inoueken.handspinner;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.inoueken.handspinner.models.HandspinnerShop;
import com.inoueken.handspinner.models.SelectSpinnerActivityModel;

import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    private Handler _handler;
    private Runnable _r;
    private ImageView _spinner;
    private Handspinner _handspinnerModel;

    private float _defaultPivotX = -1.0f;
    private float _defaultPivotY = -1.0f;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (this._defaultPivotX < 0) {
            this._defaultPivotX = this._spinner.getPivotX();
        }
        if (this._defaultPivotY < 0) {
            this._defaultPivotY = this._spinner.getPivotY();
        }

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
        HandspinnerShop shop = new HandspinnerShop();
        final AppData appData = new AppData();

        // 定期実行ハンドラを登録
        this._handler = new Handler();
        this._r = new Runnable() {
            private float _angle = 0;
            private static final long DelayMills = 4;

            @Override
            public void run() {
                // 少しずつ回す
                final float delta = 1.0f;
                _spinner.setRotation(_angle += delta);

                if (_angle >= 360) {
                    _angle -= 360;
                }
                _handler.postDelayed(this, DelayMills);
            }
        };

        this._handler.post(this._r);

        final FloatingActionMenu actionMenu = (FloatingActionMenu) findViewById(R.id.action_menu);
        final FloatingActionButton btnShowCredits = (FloatingActionButton) findViewById(R.id.btn_show_credits);
        final FloatingActionButton btnGotoShop = (FloatingActionButton) findViewById(R.id.btn_goto_shop);

        final MaterialDialog creditsDialog = new MaterialDialog.Builder(this)
                .title("クレジット")
                .customView(R.layout.credits_dialog, true)
                .positiveText("とじる").build();

        btnShowCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ダイアログを表示する
                creditsDialog.show();
                actionMenu.close(true);
            }
        });

        btnGotoShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ハンドスピナーショップに移動するやで");
                try {
                    Intent intent = new Intent(MainActivity.this, SelectSpinnerActivity.class);
                   //intent.putExtra("data", appData);
                    int requestCode = 1000;
                    startActivityForResult(intent, requestCode);
                }catch(Exception e){
                    System.out.println("遷移ミス");
                }
                actionMenu.close(true);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
       // AppData appData = (AppData)intent.getSerializableExtra("RESULT");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 定期実行ハンドラを削除
        this._handler.removeCallbacks(this._r);
    }

    private void changeSpinner(Handspinner spinner) {
        // 回転中心が合うようにピボット位置を補正する
        this._spinner.setPivotX(this._defaultPivotX * spinner.getMetadata().getPivotXCorrectionScale());
        this._spinner.setPivotY(this._defaultPivotY * spinner.getMetadata().getPivotYCorrectionScale());

        // ハンドスピナーの画像を差し替える
        this._spinner.setImageResource(spinner.getMetadata().getImageId());
    }
}
