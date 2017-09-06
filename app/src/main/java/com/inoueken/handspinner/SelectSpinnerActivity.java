package com.inoueken.handspinner;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.inoueken.handspinner.models.HandspinnerMetadata;
import com.inoueken.handspinner.models.HandspinnerShop;
import com.inoueken.handspinner.models.Player;
import com.inoueken.handspinner.models.SelectSpinnerActivityModel;

import java.text.NumberFormat;

public class SelectSpinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_spinner);
        Intent intent = getIntent();
        final SelectSpinnerActivityModel ShopModel = new SelectSpinnerActivityModel();

        /*
        テスト用ボタン
         */
        final Button TestButton = (Button) findViewById(R.id.TestButton);
        final ImageButton LeftButton = (ImageButton) findViewById(R.id.LeftButton);
        final ImageButton RightButton = (ImageButton) findViewById(R.id.RightButton);
        Button BackButton = (Button) findViewById(R.id.BackButton);
        final Button PurchaseButton = (Button) findViewById(R.id.PurchaseButton);
        final TextView SpinnerName = (TextView) findViewById(R.id.SpinnerName);
        final TextView DetailView = (TextView) findViewById(R.id.detail);
        changeDisplay(ShopModel.setButtonVisible(), ShopModel.judgeAccessRight(), PurchaseButton,
                LeftButton, RightButton, ShopModel, SpinnerName);
        ((ImageView) findViewById(R.id.SpinnerImage)).setImageResource(ShopModel.get_selectedSpinner().getMetadata().getImageId());


        /*
        テスト用ボタン
         */
        TestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.getPlayer().removeHanspinnerAccessRights(ShopModel.get_selectedSpinner().getMetadata().getId());
            }
        });

        DetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSubscribeDialog(ShopModel.get_selectedSpinner().getMetadata());
            }
        });

        LeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopModel.onLeftButtonPressed();
                ((ImageView) findViewById(R.id.SpinnerImage)).setImageResource(ShopModel.get_selectedSpinner().getMetadata().getImageId());
                changeDisplay(ShopModel.setButtonVisible(), ShopModel.judgeAccessRight(), PurchaseButton,
                        LeftButton, RightButton, ShopModel, SpinnerName);
            }
        });
        RightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopModel.onRightButtonPressed();
                ((ImageView) findViewById(R.id.SpinnerImage)).setImageResource(ShopModel.get_selectedSpinner().getMetadata().getImageId());
                changeDisplay(ShopModel.setButtonVisible(), ShopModel.judgeAccessRight(), PurchaseButton,
                        LeftButton, RightButton, ShopModel, SpinnerName);
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
                } catch (Exception e) {
                    System.out.println("遷移ミス");
                }
            }
        });
        PurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PurchaseButton.getText().equals("げっと")) {
                    createBuyDialog(ShopModel.get_selectedSpinner().getMetadata(), ShopModel
                            , PurchaseButton, LeftButton, RightButton, SpinnerName);
                } else {
                    ShopModel.onSelectButtonPressed();
                }
                //ダイアログを表示すると、処理前に関数を抜ける
                changeDisplay(ShopModel.setButtonVisible(), ShopModel.judgeAccessRight(), PurchaseButton,
                        LeftButton, RightButton, ShopModel, SpinnerName);

            }
        });

    }

    public void createSubscribeDialog(HandspinnerMetadata metadata) {
        AlertDialog.Builder SubscribeDialog = new AlertDialog.Builder(this);
        NumberFormat FormatPrice = NumberFormat.getInstance();
        SubscribeDialog.setTitle(metadata.getDisplayName());
        StringBuilder sb = new StringBuilder();
        int s = metadata.getSpeed();
        for(int i=0;i<s;i++){
            sb.append("☆");
        }
        SubscribeDialog.setMessage(metadata.getDescription() + "\n" + "価格：" +
                FormatPrice.format(metadata.getPrice()) + "\n" + "速さ：" + sb);

        SubscribeDialog.show();
    }

    public void createBuyDialog(HandspinnerMetadata metadata, final SelectSpinnerActivityModel ShopModel
            , final Button button, final ImageButton left, final ImageButton right, final TextView SpinnerName) {
        AlertDialog.Builder BuyDialog = new AlertDialog.Builder(this);
        BuyDialog.setTitle("購入しますか？");
        BuyDialog.setMessage("購入後の所持コイン：" + (Player.getPlayer().getCoinCount() - metadata.getPrice()));
        BuyDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // ボタンをクリックしたときの動作
            }
        });
        BuyDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ShopModel.onPurchaseButtonPressed();
                changeDisplay(ShopModel.setButtonVisible(), ShopModel.judgeAccessRight(), button,
                        left, right, ShopModel, SpinnerName);
            }
        });
        BuyDialog.show();
    }

    public void changeDisplay(int flag, boolean possession, Button button, ImageButton left, ImageButton right,
                              SelectSpinnerActivityModel shop, TextView name) {
        final TextView  PriceView = (TextView)findViewById(R.id.SpinnerPirice);
        final TextView CoinView = (TextView)findViewById(R.id.CoinCount);
        NumberFormat FormatPrice = NumberFormat.getInstance();
        changeBelowButton(possession, button, shop);
        changeButtonVisble(flag, left, right);
        name.setText(shop.get_selectedSpinner().getMetadata().getDisplayName());
        PriceView.setText("＄"+FormatPrice.format(shop.get_selectedSpinner().getMetadata().getPrice()));
        CoinView.setText(String.valueOf(Player.getPlayer().getCoinCount()));
    }

    public void changeBelowButton(boolean right, Button button, SelectSpinnerActivityModel shop) {
        if (right) {
            //選択ボタンを表示する
            button.setText("つかう");
            button.setBackgroundColor(Color.rgb(255,165,0));
            button.setEnabled(shop.judgeSpinnerSelected(Player.getPlayer().getCurrentHandspinner()));
        } else {
            //購入ボタンを表示する
            button.setText("げっと");
            button.setBackgroundColor(Color.rgb(173,255,47));
            button.setEnabled(Player.getPlayer().judgeCanBuySpinner(shop.get_selectedSpinner().getMetadata().getPrice()));
        }
    }

    public void changeButtonVisble(int flag, ImageButton left, ImageButton right) {
        if (flag == 1) {
            //左ボタン無効
            left.setVisibility(View.INVISIBLE);
        } else if (flag == 2) {
            //右ボタン無効
            right.setVisibility(View.INVISIBLE);
        } else {
            //ボタン有効化
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.VISIBLE);
        }
    }

}
