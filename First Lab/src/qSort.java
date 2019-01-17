public class qSort {

    public static int[] sort(int[] array) {

        //  swapValues(array, 0, array.length - 1, getPivotIndex(0, array.length - 1));
        sort(array, 0, array.length - 1);

        return array;
    }


    //Function which returns an index from the array which will be chosen as a pivot
    private static int getPivotIndex(int startingIndex, int lastIndex) {
        int findingIndex;

        findingIndex = (int) (Math.random() * (lastIndex - startingIndex) + startingIndex);

        return findingIndex;
    }


    //Function which swap Values
    private static int swapValues(int[] array, int left, int right
            , int pivot) {

        System.out.println("\nInside the swapValues Function");

        System.out.println("The Pivot is: " + pivot);

        while (left < right) {
            while (array[++left] < pivot) ;
            while (right != 0 && array[--right] > pivot) ;

            System.out.println(left + " " + right);

            //Swapping the values
            swap(array, left, right);

        }
        swap(array, left, right);
        return left;
    }


    private static void sort(int[] array, int startingIndex, int lastIndex) {

        int pivotIndex = getPivotIndex(startingIndex, lastIndex);

        int pivot = array[pivotIndex];

        swap(array,pivotIndex,lastIndex);

        int pivotPos = swapValues(array, startingIndex-1, lastIndex, pivot);

        swap(array, pivotPos, lastIndex);

        //Call for the first part of the array
        if(pivotPos - startingIndex > 1)
            sort(array, startingIndex, pivotPos-1);

        //Call for the second part of the array
        if(lastIndex-pivotPos > 1)
            sort(array, pivotPos + 1, lastIndex);

    }

    private static void swap(int[] arr, int left, int right) {
        int tempHolder = arr[left];
        arr[left] = arr[right];
        arr[right] = tempHolder;
    }
}
