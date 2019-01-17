public class Main {

    public static void main(String[] args) {

        //Test array
        int test[] = {1, 10, 71, 3, 90, 360, 11, 5, 7, 100, 69};

        System.out.println("The length of the array is " + test.length);

        int[] testingTest = qSort.sort(test);

        for (int i = 0; i < testingTest.length; i++) {
            System.out.print(testingTest[i] + " ");
        }


    }
}
