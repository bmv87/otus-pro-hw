package ru.otus.pro.reflection.tests;

import ru.otus.pro.reflection.annotations.*;


@TestContext
@Disabled(reason = "Весь класс отключен.")
public class SecondTest {

    @BeforeSuite
    public void runBefore() {
        System.out.println("SecondTest::runBefore run");
    }

    @AfterSuite
    public void runAfter() {
        System.out.println("SecondTest::runAfter run");
    }

    @Test
    public void testOne() {
        System.out.println("SecondTest::testOne run");
    }

    @Test(priority = 2)
    public void testTwo() {
        System.out.println("SecondTest::testTwo run");
    }

    @Test(priority = 7)
    public void testThree() {
        System.out.println("SecondTest::testThree run");
    }

    @Test(priority = 2)
    public void testFour() {
        System.out.println("SecondTest::testFour run");
    }

    @Disabled(reason = "Пока не нужен")
    @Test(priority = 8)
    public void testFive() {
        System.out.println("SecondTest::testFive run");
    }
}
