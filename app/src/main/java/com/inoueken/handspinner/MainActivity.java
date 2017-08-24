package com.inoueken.handspinner;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.inoueken.handspinner.models.MainActivityModel;

import rx.functions.Action1;


public class MainActivity extends AppCompatActivity {
    private Handler _handler;
    private Runnable _r;
    private ImageView _spinnerImageView;
    private MainActivityModel _model;

    private float _defaultPivotX = -1.0f;
    private float _defaultPivotY = -1.0f;

    public MainActivity(){
        super();
        this._model = new MainActivityModel();
        this._model.subscribeHandspinnerChanged(new Action1<Handspinner>() {
            @Override
            public void call(Handspinner handspinner) {
                changeHandspinner(handspinner);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (this._defaultPivotX < 0 && this._defaultPivotY < 0) {
            this._defaultPivotX = this._spinnerImageView.getPivotX();
            this._defaultPivotY = this._spinnerImageView.getPivotY();
            this._model.getReady();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this._spinnerImageView = (ImageView) findViewById(R.id.spinner);

        // 定期実行ハンドラを登録
        this._handler = new Handler();
        this._r = new Runnable() {
            private float _angle = 0;
            private static final long DelayMills = 4;

            @Override
            public void run() {
                // 少しずつ回す
                final float delta = 1.0f;
                _spinnerImageView.setRotation(_angle += delta);

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
        final FloatingActionButton btnBuyHandspinner = (FloatingActionButton) findViewById(R.id.btn_buy_handspinner);

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
                actionMenu.close(true);
            }
        });

        btnBuyHandspinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Amazonのハンドスピナーのページに移動する
                final String url = "https://www.amazon.co.jp/s/ref=nb_sb_noss_1?__mk_ja_JP=カタカナ&url=search-alias%3Daps&field-keywords=ハンドスピナー";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                actionMenu.close(true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 定期実行ハンドラを削除
        this._handler.removeCallbacks(this._r);
    }

    private void changeHandspinner(Handspinner spinner) {
        // 回転中心が合うようにピボット位置を補正する
        this._spinnerImageView.setPivotX(this._defaultPivotX * spinner.getMetadata().getPivotXCorrectionScale());
        this._spinnerImageView.setPivotY(this._defaultPivotY * spinner.getMetadata().getPivotYCorrectionScale());

        // ハンドスピナーの画像を差し替える
        this._spinnerImageView.setImageResource(spinner.getMetadata().getImageId());
    }
}
