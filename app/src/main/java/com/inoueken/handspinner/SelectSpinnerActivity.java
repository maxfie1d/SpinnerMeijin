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
import android.widget.TextView;

import com.inoueken.handspinner.models.HandspinnerShop;
import com.inoueken.handspinner.models.SelectSpinnerActivityModel;

public class SelectSpinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_spinner);
        Intent intent = getIntent();
        final AppData appData = (AppData) intent.getSerializableExtra("data");
        final SelectSpinnerActivityModel ShopModel = new SelectSpinnerActivityModel(appData);
        ImageButton LeftButton = (ImageButton)findViewById(R.id.LeftButton);
        ImageButton RightButton = (ImageButton)findViewById(R.id.RightButton);
        Button BackButton = (Button)findViewById(R.id.BackButton);
        Button PurchaseButton = (Button)findViewById(R.id.PurchaseButton);
        LeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopModel.onLeftButtonPressed();

            }
        });
        RightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopModel.onRightButtonPressed();
            }
        });
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    // keyword "RESULT" でデータを返す
                    intent.putExtra("RESULT", appData);
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
}
