package ru.otus.pro.reflection.tests;

import ru.otus.pro.reflection.annotations.*;

@TestContext
public class FirstTest {

    @BeforeSuite
    public void runBefore() {
        System.out.println("FirstTest::runBefore run");
    }

    @AfterSuite
    public void runAfter() {
        System.out.println("FirstTest::runAfter run");
    }

    @Test
    public void testOne() {
        throw new RuntimeException("FirstTest::testOne Ошибка тестирования");
      //  System.out.println("testOne");
    }

    @Test(priority = 2)
    public void testTwo() {
        System.out.println("FirstTest::testTwo run");
    }

    @Test(priority = 3)
    public void testThree() {
        System.out.println("FirstTest::testThree run");
    }

    @Disabled
    @Test(priority = 2)
    public void testFour() {
        System.out.println("FirstTest::testFour run");
    }
}
