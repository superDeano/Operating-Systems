public class qSort {

    public static int[] sort(int[] array) {

        //  swapValues(array, 0, array.length - 1, getPivotIndex(0, array.length - 1));
        recursiveCallToSwapValues(array, 0, array.length - 1);

        return array;
    }


    //Function which returns an index from the array which will be chosen as a pivot
    private static int getPivotIndex(int startingIndex, int lastIndex, int maxLengthAllowedForArray) {
        int findingIndex;

        do {
            findingIndex = (int) (Math.random() * lastIndex + startingIndex);

        } while (findingIndex > maxLengthAllowedForArray || findingIndex < 0);

        return findingIndex;
    }


    //Function which swap Values
    private static void swapValues(int[] array, int startingIndex, int lastIndex, int pivotIndex) {

        System.out.println("\nInside the swapValues Function");

        int pivot = array[pivotIndex];

        System.out.println("The Pivot is: " + pivot);

        int tempHolder = 0;


        while (array[startingIndex++] < pivot && startingIndex != lastIndex) {
            while (array[lastIndex--] > pivot && startingIndex != lastIndex) {

                System.out.println(startingIndex + " " + lastIndex);

                //Swapping the values
                tempHolder = array[startingIndex];
                array[startingIndex] = array[lastIndex];
                array[lastIndex] = tempHolder;
                System.out.println("Swapped");
            }
        }
    }


    private static void recursiveCallToSwapValues(int[] array, int startingIndex, int lastIndex) {

        if ((lastIndex - startingIndex) <= 1) {
            return;
        }

        int pivotIndex = getPivotIndex(startingIndex, lastIndex, array.length - 1);

        System.out.println("The Pivot index is " + pivotIndex);

        swapValues(array, startingIndex, lastIndex, pivotIndex);

        //Call for the first part of the array
        recursiveCallToSwapValues(array, startingIndex, pivotIndex);

        //Call for the second part of the array
        recursiveCallToSwapValues(array, pivotIndex + 1, lastIndex);

    }

}
