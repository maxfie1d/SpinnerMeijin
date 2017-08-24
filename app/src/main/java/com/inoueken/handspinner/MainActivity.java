package com.inoueken.handspinner;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.inoueken.handspinner.databinding.ActivityMainBinding;
import com.inoueken.handspinner.models.MainActivityModel;
import com.inoueken.handspinner.viewmodels.MainActivityViewModel;

import rx.functions.Action1;


public class MainActivity extends AppCompatActivity {
    private Handler _handler;
    private Runnable _r;
    private ImageView _spinnerImageView;
    private MainActivityModel _model;
    private MainActivityViewModel _vm;

    private float _defaultPivotX = -1.0f;
    private float _defaultPivotY = -1.0f;

    public MainActivity() {
        super();
        this._model = new MainActivityModel();
        this._model.subscribeHandspinnerChanged(new Action1<Handspinner>() {
            @Override
            public void call(Handspinner handspinner) {
                changeHandspinner(handspinner);
            }
        });

        this._model.subscribeRotationAngleChanged(new Action1<Float>() {
            @Override
            public void call(Float angle) {
                _spinnerImageView.setRotation(angle);
            }
        });

        this._model.subscribeCoinCountChanged(new Action1<CountChangedEventArgs>() {
            @Override
            public void call(CountChangedEventArgs countChangedEventArgs) {
                final int newCoinCount = countChangedEventArgs.getNewCount();
                // Viewにコインの数を反映する
                _vm.setCoinCount(newCoinCount);
            }
        });

        this._vm = new MainActivityViewModel();
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

        // ViewModelをバインディングする
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setVm(this._vm);

        this._spinnerImageView = (ImageView) findViewById(R.id.spinner);

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
