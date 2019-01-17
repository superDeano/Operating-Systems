public class qSort {

    public static int[] sort(int[] array) {

        swapValues(array, 0, array.length - 1, getPivotIndex(0, array.length - 1));

        return array;
    }


    //Function which returns an index from the array which will be chosen as a pivot
    private static int getPivotIndex(int firstIndex, int lastIndex) {
        return (int) (Math.random() * lastIndex + firstIndex);
    }



    //Function which swap Values
    private static void swapValues(int[] array, int firstIndex, int lastIndex, int pivotIndex) {

        System.out.println("\nInside the swapValues Function");

        int pivot = array[pivotIndex];

        System.out.println("The Pivot is: " + pivot);
        int tempHolder = 0;


        while (array[firstIndex++] > pivot && firstIndex != lastIndex) {
            while (array[lastIndex--] < pivot && firstIndex != lastIndex) {

                System.out.println(firstIndex + " " + lastIndex);

                //Swapping the values
                tempHolder = array[firstIndex];
                array[firstIndex] = array[lastIndex];
                array[lastIndex] = tempHolder;

            }
        }
    }


}
