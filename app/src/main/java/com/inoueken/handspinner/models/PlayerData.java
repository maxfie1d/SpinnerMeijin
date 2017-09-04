package com.inoueken.handspinner.models;

import java.util.HashSet;
import java.util.Set;

/**
 * シリアライズ可能なプレイヤーに関するデータを表す
 */
public class PlayerData {
    public int coinCount;
    public String currentSpinner;
    public AccessRights accessRights;

    public static PlayerData getDefault() {
        Set<String> accessRights = new HashSet<>();
        accessRights.add("basic_spinner");

        return new PlayerData(0, "basic_spinner", accessRights);
    }

    public PlayerData(int coinCount, String currentSpinner, Set<String> accessRights) {
        this.coinCount = coinCount;
        this.currentSpinner = currentSpinner;
        this.accessRights = this.createAccessRightsFromCollection(accessRights);
    }

    private AccessRights createAccessRightsFromCollection(Set<String> accessRights) {
        final boolean basic_spinner = accessRights.contains("basic_spinner");
        final boolean rare_spinner = accessRights.contains("rare_spinner");
        final boolean legendary_spinner = accessRights.contains("legendary_spinner");
        final boolean ultra_spinner = accessRights.contains("ultra_spinner");
        final boolean kakiage_spinner = accessRights.contains("kakiage_spinner");
        return new AccessRights(basic_spinner, rare_spinner, legendary_spinner, ultra_spinner, kakiage_spinner);
    }

    public class AccessRights {
        public boolean basic_spinner;
        public boolean rare_spinner;
        public boolean legendary_spinner;
        public boolean ultra_spinner;
        public boolean kakiage_spinner;

        public AccessRights(boolean basic_spinner,
                            boolean rare_spinner,
                            boolean legendary_spinner,
                            boolean ultra_spinner,
                            boolean kakiage_spinner) {
            this.basic_spinner = basic_spinner;
            this.rare_spinner = rare_spinner;
            this.legendary_spinner = legendary_spinner;
            this.ultra_spinner = ultra_spinner;
            this.kakiage_spinner = kakiage_spinner;
        }
    }
}
