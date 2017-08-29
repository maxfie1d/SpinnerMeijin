package com.inoueken.handspinner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.inoueken.handspinner.models.HandspinnerShop;
import com.inoueken.handspinner.models.SelectSpinnerActivityModel;

public class SelectSpinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_spinner);
        Intent intent = getIntent();
        //final AppData appData = (AppData) intent.getSerializableExtra("data");
        AppData appData = new AppData();
        final SelectSpinnerActivityModel ShopModel = new SelectSpinnerActivityModel(appData);
        final ImageButton LeftButton = (ImageButton)findViewById(R.id.LeftButton);
        final ImageButton RightButton = (ImageButton)findViewById(R.id.RightButton);
        Button BackButton = (Button)findViewById(R.id.BackButton);
        final Button PurchaseButton = (Button)findViewById(R.id.PurchaseButton);
        changeButtonVisble(ShopModel.setButtonVisible(),LeftButton,RightButton);
        changeBelowButton(ShopModel.judgeAccessRight(),PurchaseButton);

        LeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopModel.onLeftButtonPressed();
                ((ImageView)findViewById(R.id.SpinnerImage)).setImageResource(R.drawable.rare_spinner);
                changeBelowButton(ShopModel.judgeAccessRight(),PurchaseButton);
                changeButtonVisble(ShopModel.setButtonVisible(),LeftButton,RightButton);
            }
        });
        RightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopModel.onRightButtonPressed();
                ((ImageView)findViewById(R.id.SpinnerImage)).setImageResource(R.drawable.legendary_spinner);
                changeBelowButton(ShopModel.judgeAccessRight(),PurchaseButton);
                changeButtonVisble(ShopModel.setButtonVisible(),LeftButton,RightButton);
            }
        });
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    // keyword "RESULT" でデータを返す
                    //intent.putExtra("RESULT", appData);
                    setResult(RESULT_OK, intent);
                    finish();
                }catch (Exception e){
                    System.out.println("遷移ミス");
                }
            }
        });
        PurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopModel.onPurchaseButtonPressed();
            }
        });

    }
    public void changeBelowButton (boolean right,Button button){
        if (right) {
            //選択ボタンを表示する
            button.setText("選択する");
        } else {
            //購入ボタンを表示する
            button.setText("購入する");
        }
    }
    public void changeButtonVisble(int flag,ImageButton left,ImageButton right){
        if(flag==1){
            //左ボタン無効
            left.setVisibility(View.INVISIBLE);
        }else if(flag==2){
            //右ボタン無効
            right.setVisibility(View.INVISIBLE);
        }else{
            //ボタン有効化
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.VISIBLE);
        }
    }
}
