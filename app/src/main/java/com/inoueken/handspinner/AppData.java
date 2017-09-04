package com.inoueken.handspinner;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inoueken.handspinner.models.Player;
import com.inoueken.handspinner.models.PlayerData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class AppData {
    public static void init(Context context) {
        if (AppData._context == null) {
            AppData._context = context;
        }
    }

    private static Context _context;

    public AppData() {
    }

    public PlayerData loadPlayerData() {
        // JSONファイルを読み込む
        // エラーならばデフォルトのデータを読み込むことにする
        String json = this.loadFileContent(createSettingsFile());
        if (json == null) {
            Log.d("debug", "プレイヤーデータが存在しないため、デフォルト値を読み込みます");
            return PlayerData.getDefault();
        } else {
            Log.d("debug", "プレイヤーデータを読み込みます");
            Log.d("debug", json);
            // デシリアライズする
            PlayerData playerData = new Gson().fromJson(json, PlayerData.class);
            return playerData;
        }
    }

    public void savePlayerData(Player player) {
        // PlayerからplayerDataを作る
        PlayerData playerData = new PlayerData(
                player.getCoinCount(),
                player.getCurrentHandspinner().getMetadata().getId(),
                player.getHandspinnerAccessRights());
        final String json = new GsonBuilder().setPrettyPrinting().create().toJson(playerData);
        this.saveFile(this.createSettingsFile(), json);
        Log.d("debug", "プレイヤーデータを保存しました");
        Log.d("debug", json);
    }

    private File createSettingsFile() {
        return new File(AppData._context.getFilesDir(), "settings.json");
    }


    private void saveFile(File file, String content) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadFileContent(File file) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String content = new String(buffer);
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
