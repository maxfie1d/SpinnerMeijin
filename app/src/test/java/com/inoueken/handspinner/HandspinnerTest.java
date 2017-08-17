package com.inoueken.handspinner;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by y-fujiwr on 2017/08/17.
 */

public class HandspinnerTest {
    @Test
    public void addVelocityTest() throws InterruptedException {
        Handspinner hs = new Handspinner();
        hs.rotate();
        hs.addForce(1.0f);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(hs.getAngle());
    }
}
