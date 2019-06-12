package com.mia.phase10;

import com.mia.phase10.classes.CardStackTest;
import com.mia.phase10.classes.CardUnitTest;
import com.mia.phase10.classes.GameDataTest;
import com.mia.phase10.classes.HandTest;
import com.mia.phase10.classes.PlayerTest;
import com.mia.phase10.gameLogic.GameLogicHandlerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        CardEvaluatorTest.class,
        ShufflingActivityTest.class,
        CardStackTest.class,
        CardUnitTest.class,
        HandTest.class,
        PlayerTest.class,
        GameDataTest.class,
        //GameLogicHandlerTest.class
})


public class TestSuite {
}
