package com.inoueken.handspinner;


public class RotationCountChangedEventArgs {
    private int _oldCount;
    private int _newCount;

    public RotationCountChangedEventArgs(int oldCount, int newCount) {
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
