import java.math.BigInteger;

public class Testing {

    public static void test() {

        int maxArrayLength = 10000;
        int maxNumberInArray = 9999999;
        int minNumberInArray = 1;
        int number;

        int[] testingArrayWithThread = new int[maxArrayLength];
        int[] testingArrayWOThread = new int[maxArrayLength];

        //Filling the arrays
        for (int index = 0; index < maxArrayLength; index++) {
            number = (int) (Math.random() * maxNumberInArray + minNumberInArray);
            testingArrayWithThread[index] = number;
            testingArrayWOThread[index] = number;
        }

        BigInteger startingTimeWithThreading, endingTimeWithThreading, startingTimeWithoutThreading, endingTimeWithoutThreading;

        //Timing the sorting done without Threading
        startingTimeWithoutThreading = (BigInteger.valueOf(System.nanoTime()));
        qSortWithoutThreading.sort(testingArrayWOThread);
        endingTimeWithoutThreading = BigInteger.valueOf(System.nanoTime());

        //Timing the sorting done with Threading
        startingTimeWithThreading = BigInteger.valueOf(System.nanoTime());
        qSort.sort(testingArrayWithThread);
        endingTimeWithThreading = BigInteger.valueOf(System.nanoTime());

        BigInteger durationWithoutThreading = endingTimeWithoutThreading.subtract(startingTimeWithoutThreading);
        BigInteger durationWithThreading = endingTimeWithThreading.subtract(startingTimeWithThreading);

        System.out.println("Time taken for the sorting without Threading is : " + durationWithoutThreading + " in nano seconds");
        System.out.println("Time take for the sorting with Threading is : " + durationWithThreading + " in nano seconds");

        int result = durationWithoutThreading.compareTo(durationWithThreading);
        String Result = (result < 0)? "Without Threading is faster" : "With Threading is faster";
        System.out.println(Result);


    }

}
