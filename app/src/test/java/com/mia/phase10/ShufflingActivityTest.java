package com.mia.phase10;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShufflingActivityTest {

    float x,y,z;
    ShufflingActivity shuffling;

    @Before
    public void setUp() throws Exception {
        x = 0;
        y = 0;
        z = 0;
        shuffling=new ShufflingActivity();

    }

    @After
    public void tearDown() throws Exception {
        shuffling=null;
    }

    @Test
    public void testEnoughAccelerationSmaller2() {
        assertFalse(shuffling.enoughAcceleration(x,y,z));
    }

    @Test
    public void testEnoughAccelerationBigger2() {
        x = 8;
        y = 9;
        z = 10;

        assertTrue(shuffling.enoughAcceleration(x,y,z));
    }
    @Test
    public void testGetShuffleCount() {
        assertEquals(shuffling.getShuffleCount(),0);
    }

}


