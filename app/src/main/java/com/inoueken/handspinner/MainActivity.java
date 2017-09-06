package com.inoueken.handspinner;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.inoueken.handspinner.databinding.ActivityMainBinding;
import com.inoueken.handspinner.models.MainActivityModel;
import com.inoueken.handspinner.viewmodels.MainActivityViewModel;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

import com.inoueken.handspinner.models.HandspinnerShop;
import com.inoueken.handspinner.models.SelectSpinnerActivityModel;

import android.content.Intent;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private ImageView _spinnerImageView;
    private MainActivityModel _model;
    private MainActivityViewModel _vm;
    private DisplayMetrics displayMetrics;
    private GestureDetector ges;
    private double beforePositionX = 0;
    private double beforePositionY = 0;
    private double centerX = 0;
    private double centerY = 0;
    private RelativeLayout r1 = null;
    private CompositeSubscription _subscriptions;

    private float _defaultPivotX = -1.0f;
    private float _defaultPivotY = -1.0f;

    private void subscribeEvents() {
        Log.d("debug", "イベント購読を開始します");
        final Subscription s1 = this._model.subscribeHandspinnerChanged(new Action1<Handspinner>() {
            @Override
            public void call(Handspinner handspinner) {
                changeHandspinner(handspinner);
            }
        });

        final Subscription s2 = this._model.subscribeRotationAngleChanged(new Action1<Float>() {
            @Override
            public void call(final Float angle) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _spinnerImageView.setRotation(angle);
                    }
                });
            }
        });

        final Subscription s3 = this._model.subscribeCoinCountChanged(new Action1<CountChangedEventArgs>() {
            @Override
            public void call(CountChangedEventArgs countChangedEventArgs) {
                final int newCoinCount = countChangedEventArgs.getNewCount();
                // Viewにコインの数を反映する
                _vm.setCoinCount(newCoinCount);
            }
        });

        final CompositeSubscription subscriptions = Subscriptions.from(s1, s2, s3);
        this._subscriptions = subscriptions;
    }

    private void unsubscribeEvents() {
        if (this._subscriptions != null) {
            Log.d("debug", "イベント購読を解除します");
            this._subscriptions.unsubscribe();
        }
    }

    public MainActivity() {
        super();
        Log.d("debug", "MainActivityのインスタンスを作るやで");
        this._model = new MainActivityModel();
        this.subscribeEvents();

        this._vm = new MainActivityViewModel();
        AppData.init(this);
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
        this.r1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        this.displayMetrics = new DisplayMetrics();
        display.getMetrics(this.displayMetrics);

        ges = new GestureDetector(this, this);

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
                unsubscribeEvents();
                _model.stopSimulation();
                try {
                    Intent intent = new Intent(MainActivity.this, SelectSpinnerActivity.class);
                    //intent.putExtra("data", appData);
                    int requestCode = 1000;
                    startActivityForResult(intent, requestCode);
                } catch (Exception e) {
                    System.out.println("遷移ミス");
                }
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

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("debug", "ショップから戻ってきたやで");
        super.onActivityResult(requestCode, resultCode, intent);

        // 回転シミュレーションを再開する
        this._model.getCurrentHandspinner().rotate();
        this.subscribeEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void changeHandspinner(Handspinner spinner) {
        // 回転中心が合うようにピボット位置を補正する
        this._spinnerImageView.setPivotX(this._defaultPivotX * spinner.getMetadata().getPivotXCorrectionScale());
        this._spinnerImageView.setPivotY(this._defaultPivotY * spinner.getMetadata().getPivotYCorrectionScale());

        // ハンドスピナーの画像を差し替える
        this._spinnerImageView.setImageResource(spinner.getMetadata().getImageId());
        this._spinnerImageView.setImageResource(spinner.getMetadata().getImageId());

        // ハンドスピナーの回転軸の座標を計算しておく
        centerX = _spinnerImageView.getLeft() + _spinnerImageView.getPivotX();
        centerY = displayMetrics.heightPixels - r1.getHeight() + _spinnerImageView.getTop() + _spinnerImageView.getPivotY();

    }

    @Override
    protected void onStop() {
        super.onStop();
        this._model.onStop();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        this._model.getCurrentHandspinner().setAngularVelocity(0f);
        beforePositionX = e.getX() - centerX;
        beforePositionY = e.getY() - centerY;
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ges.onTouchEvent(event);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        double e1X = e1.getX() - centerX;
        double e1Y = e1.getY() - centerY;
        double e2X = e2.getX() - centerX;
        double e2Y = e2.getY() - centerY;

        if (beforePositionX == 0) {
            beforePositionX = e1X;
            beforePositionY = e1Y;
        }

        double theta = Math.atan2(beforePositionX * e2Y - beforePositionY * e2X, beforePositionX * e2X + beforePositionY * e2Y);

        Handspinner s = this._model.getCurrentHandspinner();

        s.setAngularVelocity(0f);
        s.addAngle((float) (theta / Math.PI * 180));

        beforePositionX = e2X;
        beforePositionY = e2Y;

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        double vX = velocityX / displayMetrics.widthPixels * 25.4;
        double vY = velocityY / displayMetrics.heightPixels * 25.4;
        double touchX = e2.getX();
        double touchY = e2.getY();
        double vectorX = touchX - centerX;
        double vectorY = touchY - centerY;
        double distanceFromCenter = Math.sqrt(Math.pow(vectorX, 2) + Math.pow(vectorY, 2));
        double validVelocitySize = Math.abs(vectorX * vX + vectorY * vY) / Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        double arm = distanceFromCenter + Math.sqrt(vX * vX + vY * vY - validVelocitySize * validVelocitySize);
        double theta = Math.atan2(vectorX * vY - vectorY * vX, vectorX * vX + vectorY * vY);

        Handspinner s = this._model.getCurrentHandspinner();
        double speed = s.getMetadata().getSpeed();

        if (theta < 0) {
            s.addForce(-(float) (validVelocitySize * arm / (220000 - 50000 * speed)));
        } else if (theta != 0 && theta != Math.PI) {
            s.addForce((float) (validVelocitySize * arm / (220000 - 50000 * speed)));
        }
        return true;
    }
}
