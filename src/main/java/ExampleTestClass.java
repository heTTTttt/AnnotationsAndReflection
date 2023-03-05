public class ExampleTestClass {
    @BeforeSuite
    public void setUp() {
        System.out.println("BeforeSuite method");
    }

    @Test(priority = 5)
    public void testMethod1() {
        System.out.println("TestMethod1 with priority 5");
    }

    @Test(priority = 7)
    public void testMethod2() {
        System.out.println("TestMethod2 with priority 7");
    }

    @Test(priority = 2)
    public void testMethod3() {
        System.out.println("TestMethod3 with priority 2");
    }

    @AfterSuite
    public void tearDown() {
        System.out.println("AfterSuite method");
    }
}

