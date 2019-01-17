public class Main {

    public static void main(String[] args) {

        int[] testingTest = {};
        boolean error = false;

        for(int i = 0; i < 1000; i++) {
            //Test array
            int test[] = {1, 10, 71, 3, 90, 360, 11, 5, 7, 100, 69};

            System.out.println("The length of the array is " + test.length);

             testingTest = qSort.sort(test);

            for (int j = 1; j < testingTest.length; j++) {
                if (testingTest[j] < testingTest[j - 1]) {
                    error = true;
                    break;
                }
            }
        }

        if(error) {
            System.out.println("Error");
            for (int j = 0; j < testingTest.length; j++) {
                System.out.print(testingTest[j] + " ");
            }
        }else{
            System.out.println("Succeed");
        }
    }
}
