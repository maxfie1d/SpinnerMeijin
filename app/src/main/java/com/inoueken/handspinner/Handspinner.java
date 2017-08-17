package com.inoueken.handspinner;

import java.util.*;
import java.util.Timer;

/**
 * Created by n-isida on 2017/08/08.
 */

public class Handspinner {
    private int _rotationCount;
    private float angle;
    private float angularVelocity;
    private float registanceForce;
    private float rotationTaskInterval;
    private Timer t;
    private float mass;
    private ArrayList<RotationCountChangedEventListener> _listeners;

    public Handspinner() {
        this._rotationCount = 0;
        this.angle = 0;
        this.angularVelocity = 0;
        this.registanceForce = 0;
        this.rotationTaskInterval = 1;
        this.t = new Timer();
        this.mass = 1;
        this._listeners = new ArrayList<>();
    }

    public float getAngle() {
        return angle;
    }

    public void addForce(float amount) {
        angularVelocity += amount; //amountはハンドスピナーに加えた力を半径1の場所に加えた場合に補正したときの大きさ
    }

    public int getRotationCount() {
        return this._rotationCount;
    }

    public void rotate() {
        t.schedule(new rotationTask(), 0, (long) rotationTaskInterval);
    }

    private class rotationTask extends TimerTask {
        public void run() {
            calc();
        }

        private void calc() {
            angle += angularVelocity * rotationTaskInterval;
            int oldRotationCount = _rotationCount;
            int rotationCountDiff = Math.abs((int) (angle / 360f));
            _rotationCount += rotationCountDiff;
            angle -= 360f * (int) (angle / 360f);
            angularVelocity -= registanceForce * rotationTaskInterval;

            if (rotationCountDiff > 0) {
                for (RotationCountChangedEventListener listener : _listeners) {
                    listener.rotationChanged(new RotationCountChangedEventArgs(oldRotationCount, _rotationCount));
                }
            }
        }
    }

    private void finishRotationTask() {
        t.cancel();
    }

    public void addRotationCountChangedEventListener(RotationCountChangedEventListener listener) {
        this._listeners.add(listener);
    }
}
