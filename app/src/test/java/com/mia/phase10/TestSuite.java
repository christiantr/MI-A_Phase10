package com.mia.phase10;

import com.mia.phase10.classes.CardStackTest;
import com.mia.phase10.classes.CardUnitTest;
import com.mia.phase10.classes.HandTest;
import com.mia.phase10.classes.PlayerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        CardStackTest.class,
        CardUnitTest.class,
        HandTest.class,
        PlayerTest.class
})


public class TestSuite {
}
