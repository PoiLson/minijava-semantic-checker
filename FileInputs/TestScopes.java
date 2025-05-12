class TestScopes {
    int x = 5; // This 'x' is in the class scope

    public static void main(String[] args) {
        int x = 10; // This 'x' is in the main method scope
        System.out.println(x); // Should print 10 (main method's 'x')

        if (true) {
            int x = 20; // This 'x' is in the if block scope
            System.out.println(x); // Should print 20 (if block's 'x')
        }

        for (int i = 0; i < 1; i++) {
            int x = 30; // This 'x' is in the for-loop block scope
            System.out.println(x); // Should print 30 (for-loop's 'x')
        }

        System.out.println(x); // Should print 10 again (main method's 'x')
    }

    public void testMethod() {
        int x = 40; // This 'x' is in the method scope
        System.out.println(x); // Should print 40 (method's 'x')
    }
}
