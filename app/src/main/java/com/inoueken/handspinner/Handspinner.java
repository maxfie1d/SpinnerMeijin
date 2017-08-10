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
    private float rotatationTaskInterval;
    private Timer t;
    private float mass;

    public Handspinner() {
        this._rotationCount = 0;
        this.angle = 0;
        this.angularVelocity = 0;
        this.registanceForce = 0;
        this.rotatationTaskInterval = 0.001f;
        this.t = new Timer();
        this.mass = 1;
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

    private void rotate(){
        t.schedule(new rotationTask(),0,(long) rotatationTaskInterval);
    }

    private class rotationTask extends TimerTask {
        public void run(){
            calc();
        }
        private void calc(){
            angle += angularVelocity * rotatationTaskInterval;
            if(angle >= 360f){
                angle -= 360f;
                _rotationCount++;
            }else if(angle <= -360f){
                angle += 360f;
                _rotationCount++;
            }
            angularVelocity -= registanceForce * rotatationTaskInterval;
        }
    }

    private void finishRotationTask(){
        t.cancel();
    }
}
