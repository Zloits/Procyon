package me.zloits.procyon;

public class InstanceGetterTest {

    private final ExampleInstance exampleInstance = new ExampleInstance();

    /*
    @Test
    void getInstanceTest() {
        InstanceGetter.add(exampleInstance);

        ExampleInstance exampleInstance1 = InstanceGetter.get(ExampleInstance.class);
        assertEquals(exampleInstance, exampleInstance1);
    }

    @Test
    void duplicateInstanceTest() {
        if (!InstanceGetter.add(exampleInstance)) {
            System.out.println("Test duplicate instance test passed!");
        }
    }

     */
}
