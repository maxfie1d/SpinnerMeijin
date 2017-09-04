package com.inoueken.handspinner;


public class CountChangedEventArgs{
    private int _oldCount;
    private int _newCount;

    public CountChangedEventArgs(int oldCount, int newCount) {
        this._oldCount = oldCount;
        this._newCount = newCount;
    }

    public int getOldCount() {
        return this._oldCount;
    }

    public int getNewCount() {
        return this._newCount;
    }
}
