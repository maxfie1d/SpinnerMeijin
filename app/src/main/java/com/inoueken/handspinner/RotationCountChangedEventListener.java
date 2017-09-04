package com.inoueken.handspinner;

import java.util.EventListener;

/**
 * Created by n-isida on 2017/08/17.
 */

public interface RotationCountChangedEventListener extends EventListener {
    void rotationChanged(CountChangedEventArgs args);
}
