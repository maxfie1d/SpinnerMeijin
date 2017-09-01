package com.inoueken.handspinner;

import com.inoueken.handspinner.models.HandspinnerMetadata;

import java.util.Timer;
import java.util.TimerTask;

import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

public class Handspinner {
    private int _rotationCount;
    private float angle;
    private float angularVelocity;
    private float registanceForce;
    private float rotationTaskInterval;
    private Timer t;
    private float mass;
    private BehaviorSubject<Float> _rotationAngleChangedEvent;

    private HandspinnerMetadata _metadata;
    private BehaviorSubject<CountChangedEventArgs> _rotationCountChangedEvent;

    public Handspinner(HandspinnerMetadata metadata) {
        this._rotationCount = 0;
        this.angle = 0;
        this.angularVelocity = 0;
        this.registanceForce = 0.0001f;
        this.rotationTaskInterval = 1;
        this.t = new Timer();
        this.mass = 1;
        this._metadata = metadata;
        this._rotationCountChangedEvent = BehaviorSubject.create();
        this._rotationAngleChangedEvent = BehaviorSubject.create();
    }

    public Handspinner() {
        this(null);
    }

    public void addAngle(float amount) {
        angle += amount;
    }
    public void setAngularVelocity(float value){
        angularVelocity = value;
    }
    public float getAngle() {
        return angle;
    }

    public void addForce(float amount) {
        angularVelocity = amount; //amountはハンドスピナーに加えた力を半径1の場所に加えた場合に補正したときの大きさ
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
            if (angularVelocity > 0)
                angularVelocity = Math.max(angularVelocity - registanceForce * rotationTaskInterval, 0);
            else if (angularVelocity < 0)
                angularVelocity = Math.min(angularVelocity + registanceForce * rotationTaskInterval, 0);

            _rotationAngleChangedEvent.onNext(angle);

            if (rotationCountDiff > 0) {
                _rotationCountChangedEvent.onNext(new CountChangedEventArgs(oldRotationCount, _rotationCount));
            }
        }
    }

    public void finishRotationTask() {
        t.cancel();
    }

    public Subscription subscribeRotationCountChanged(Action1<CountChangedEventArgs> action) {
        return this._rotationCountChangedEvent.subscribe(action);
    }

    public Subscription subscribeRotationAngleChanged(Action1<Float> action) {
        return this._rotationAngleChangedEvent.subscribe(action);
    }

    public HandspinnerMetadata getMetadata() {
        return this._metadata;
    }
}
