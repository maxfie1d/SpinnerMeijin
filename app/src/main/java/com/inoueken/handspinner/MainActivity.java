package com.inoueken.handspinner;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.inoueken.handspinner.models.HandspinnerShop;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private Handler _handler;
    private Runnable _r;
    private ImageView _spinner;
    private Handspinner _handspinnerModel;
    private Display display;
    private DisplayMetrics displayMetrics;
    private GestureDetector ges;

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
        this._handspinnerModel.rotate();

        this.changeSpinner(spinner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this._spinner = (ImageView) findViewById(R.id.spinner);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        this.displayMetrics = new DisplayMetrics();
        display.getMetrics(this.displayMetrics);

        ges = new GestureDetector(this, this);

        // 定期実行ハンドラを登録
        this._handler = new Handler();
        this._r = new Runnable() {
            private float _angle = 0;
            private static final long DelayMills = 4;

            @Override
            public void run() {
                // 少しずつ回す
                //final float delta = 1.0f;
                if (_handspinnerModel != null) {
                    _spinner.setRotation(_handspinnerModel.getAngle());
                }
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

    private void changeSpinner(Handspinner spinner) {
        // 回転中心が合うようにピボット位置を補正する
        this._spinner.setPivotX(this._defaultPivotX * spinner.getMetadata().getPivotXCorrectionScale());
        this._spinner.setPivotY(this._defaultPivotY * spinner.getMetadata().getPivotYCorrectionScale());

        // ハンドスピナーの画像を差し替える
        this._spinner.setImageResource(spinner.getMetadata().getImageId());
    }

    @Override
    public boolean onDown(MotionEvent e) {
        System.out.println("おんだうん");
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ges.onTouchEvent(event);
        return false;
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
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        double vX = velocityX / displayMetrics.widthPixels * 25.4;
        double vY = velocityY / displayMetrics.heightPixels * 25.4;
        double touchX = e1.getX();
        double touchY = e1.getY();
        double spinnerCenterX = _spinner.getPivotX();
        double spinnerCenterY = _spinner.getPivotY();
        double vectorX = touchX - spinnerCenterX;
        double vectorY = touchY - spinnerCenterY;
        double distanceFromCenter = Math.sqrt(Math.pow(vectorX, 2) + Math.pow(vectorY, 2));
        double validVelocitySize = Math.abs(vectorX * vX + vectorY * vY) / Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        double arm = distanceFromCenter + Math.sqrt(vX * vX + vY * vY - validVelocitySize * validVelocitySize);
        double theta = Math.atan2(vectorX * velocityY - vectorY * velocityX, vectorX * velocityX + vectorY * velocityY);


        if (theta < 0) {
            _handspinnerModel.addForce(-(float) (validVelocitySize * arm/100000));
        } else if (theta != 0 && theta != Math.PI) {
            _handspinnerModel.addForce((float) (validVelocitySize * arm/100000));
        }
        return true;
    }
}
