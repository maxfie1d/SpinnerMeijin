package com.inoueken.handspinner;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inoueken.handspinner.models.HandspinnerMetadata;
import com.inoueken.handspinner.models.Player;
import com.inoueken.handspinner.models.SelectSpinnerActivityModel;

import java.text.NumberFormat;

public class SelectSpinnerActivity extends AppCompatActivity {
    private SelectSpinnerActivityModel ShopModel;

    public SelectSpinnerActivity(){
        this.ShopModel = new SelectSpinnerActivityModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_spinner);
        Intent intent = getIntent();


        final ImageButton LeftButton = (ImageButton) findViewById(R.id.LeftButton);
        final ImageButton RightButton = (ImageButton) findViewById(R.id.RightButton);
        Button BackButton = (Button) findViewById(R.id.BackButton);
        final Button PurchaseButton = (Button) findViewById(R.id.PurchaseButton);
        final TextView SpinnerName = (TextView) findViewById(R.id.SpinnerName);
        final TextView DetailView = (TextView) findViewById(R.id.detail);
        changeDisplay(ShopModel.setButtonVisible(), ShopModel.judgeAccessRight(), PurchaseButton,
                LeftButton, RightButton, ShopModel, SpinnerName);
        ((ImageView) findViewById(R.id.SpinnerImage)).setImageResource(ShopModel.get_selectedSpinner().getMetadata().getImageId());


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
                if (PurchaseButton.getText().equals("ゲット")) {
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
        for (int i = 0; i < s; i++) {
            sb.append("★");
        }
        for (int i = 5 - s; i > 0; i--) {
            sb.append("☆");
        }
        if (metadata.getDisplayName().equals("ベーシックスピナー")) {
            SubscribeDialog.setMessage(metadata.getDescription() + "\n" + "価格：無料"
                    + "\n速さ：" + sb+"\n生産性："+metadata.getProductivity());
        } else {
            SubscribeDialog.setMessage(metadata.getDescription() + "\n" + "価格：$" +
                    FormatPrice.format(metadata.getPrice()) + "\n" + "速さ：" + sb+"\n生産性："+metadata.getProductivity());
        }
        SubscribeDialog.show();
    }

    public void createBuyDialog(HandspinnerMetadata metadata, final SelectSpinnerActivityModel ShopModel
            , final Button button, final ImageButton left, final ImageButton right, final TextView SpinnerName) {
        AlertDialog.Builder BuyDialog = new AlertDialog.Builder(this);
        BuyDialog.setTitle("購入しますか？");
        BuyDialog.setMessage("購入後の所持コイン：$" + (Player.getPlayer().getCoinCount() - metadata.getPrice()));

        BuyDialog.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // ボタンをクリックしたときの動作
            }
        });
        BuyDialog.setNegativeButton("はい", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ShopModel.onPurchaseButtonPressed();
                changeDisplay(ShopModel.setButtonVisible(), ShopModel.judgeAccessRight(), button,
                        left, right, ShopModel, SpinnerName);
                createSelectDialog(ShopModel.get_selectedSpinner().getMetadata(), ShopModel
                        , button, left, right, SpinnerName);

            }
        });
        BuyDialog.show();
    }

    public void createSelectDialog(HandspinnerMetadata metadata, final SelectSpinnerActivityModel ShopModel
            , final Button button, final ImageButton left, final ImageButton right, final TextView SpinnerName) {
        AlertDialog.Builder SelectDialog = new AlertDialog.Builder(this);
        SelectDialog.setTitle("このスピナーを使用しますか？");
        SelectDialog.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // ボタンをクリックしたときの動作
            }
        });
        SelectDialog.setNegativeButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShopModel.onSelectButtonPressed();
                changeDisplay(ShopModel.setButtonVisible(), ShopModel.judgeAccessRight(), button,
                        left, right, ShopModel, SpinnerName);

            }
        });
        SelectDialog.show();
    }

    public void changeDisplay(int flag, boolean possession, Button button, ImageButton left, ImageButton right,
                              SelectSpinnerActivityModel shop, TextView name) {
        final TextView PriceView = (TextView) findViewById(R.id.SpinnerPirice);
        final TextView CoinView = (TextView) findViewById(R.id.CoinCount);
        NumberFormat FormatPrice = NumberFormat.getInstance();
        changeBelowButton(possession, button, shop);
        changeButtonVisble(flag, left, right);
        name.setText(shop.get_selectedSpinner().getMetadata().getDisplayName());
        if (shop.get_selectedSpinner().getMetadata().getDisplayName().equals("ベーシックスピナー")) {
            PriceView.setText("無料");
        } else {
            PriceView.setText("＄" + FormatPrice.format(shop.get_selectedSpinner().getMetadata().getPrice()));
        }
        CoinView.setText(String.valueOf(Player.getPlayer().getCoinCount()));
    }

    /**
     * ボタンのテキストを変更した際は購入ボタンが押された時の処理を変更すること
     */
    public void changeBelowButton(boolean right, Button button, SelectSpinnerActivityModel shop) {
        if (right) {
            //選択ボタンを表示する
            button.setText("つかう");
            button.setBackgroundColor(Color.rgb(83, 109, 254));
            button.setEnabled(shop.judgeSpinnerSelected(Player.getPlayer().getCurrentHandspinner()));
        } else {
            //購入ボタンを表示する
            button.setText("ゲット");
            button.setBackgroundColor(Color.rgb(0, 230, 118));
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

    @Override
    protected void onStop() {
        super.onStop();
        this.ShopModel.onStop();
    }
}
