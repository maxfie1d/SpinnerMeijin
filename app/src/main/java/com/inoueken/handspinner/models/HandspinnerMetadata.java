package com.inoueken.handspinner.models;


/**
 * ハンドスピナーの属性を格納するクラスだよ♡
 */
public class HandspinnerMetadata {
    // ハンドスピナーのID
    private String _id;

    // 表示名
    private String _displayName;

    private int _speed;
    private String _description;
    private int _price;

    // pivotXの補正倍率
    private float _pivotXCorrectionScale;

    // pivotYの補正倍率
    private float _pivotYCorrectionScale;

    private String _imagePath;
    private int _imageId;

    public HandspinnerMetadata(
            String id,
            String displayName,
            int speed,
            String description,
            int price,
            float pivotXCorrectionScale,
            float pivotYCorrectionScale,
            String imagePath,
            int imageId) {
        this._id = id;
        this._displayName = displayName;
        this._speed = speed;
        this._description = description;
        this._price = price;
        this._pivotXCorrectionScale = pivotXCorrectionScale;
        this._pivotYCorrectionScale = pivotYCorrectionScale;
        this._imagePath = imagePath;
        this._imageId = imageId;
    }

    public String getId() {
        return this._id;
    }

    /**
     * 表示名を返します
     *
     * @return 表示名
     */
    public String getDisplayName() {
        return this._displayName;
    }

    /**
     * すばやさを返します
     *
     * @return すばやさ
     */
    public int getSpeed() {
        return this._speed;
    }

    /**
     * 説明文を返します
     *
     * @return 説明文
     */
    public String getDescription() {
        return this._description;
    }

    /**
     * ねだんを返します
     *
     * @return ねだん
     */
    public int getPrice() {
        return this._price;
    }

    public float getPivotXCorrectionScale() {
        return this._pivotXCorrectionScale;
    }

    public float getPivotYCorrectionScale() {
        return this._pivotYCorrectionScale;
    }

    public String getImagePath() {
        return this._imagePath;
    }

    public int getImageId() {
        return this._imageId;
    }
}
