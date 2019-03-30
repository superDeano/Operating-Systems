public class QSort {

    //Public function
    public static int[] sort(int[] array) {

        sort(array, 0, array.length - 1);

        return array;
    }


    //Function which returns an index from the array which will be chosen as a pivot
    private static int getPivotIndex(int startingIndex, int lastIndex) {

        return (int) (Math.random() * (lastIndex - startingIndex) + startingIndex);
    }


    //Function which swap Values
    private static int swapValues(int[] array, int left, int right, int pivot) {

        while (left < right) {
            while (array[++left] < pivot) ;
            while (right != 0 && array[--right] > pivot) ;

            //Swapping the values
            swap(array, left, right);
        }

        swap(array, left, right);
        return left;
    }

    //Function which starts the Quick Sort
    private static void sort(int[] array, int startingIndex, int lastIndex) {

        int pivotIndex = getPivotIndex(startingIndex, lastIndex);

        //Gets Pivots
        int pivot = array[pivotIndex];

        //Swaps pivot
        swap(array, pivotIndex, lastIndex);

        int pivotPos = swapValues(array, startingIndex - 1, lastIndex, pivot);

        swap(array, pivotPos, lastIndex);


        try {
            Thread threadLeft = null;
            Thread threadRight = null;

            //Call for the first part of the array
            if (pivotPos - startingIndex > 1) {
                threadLeft = new Thread(() -> sort(array, startingIndex, pivotPos - 1));
                threadLeft.start();
            }


            //Call for the second part of the array
            if (lastIndex - pivotPos > 1) {
                threadRight = new Thread(() -> sort(array, pivotPos + 1, lastIndex));
                threadRight.start();
            }

            //To make sure that the main function is not waiting for all the running threads
            if(threadRight != null){
                threadRight.join();
            }
            if(threadLeft != null){
                threadLeft.join();
            }

        } catch (InterruptedException ex) {
            System.out.println("Thread error");
        }


    }

    //Function which swaps values
    private static void swap(int[] arr, int left, int right) {
        int tempHolder = arr[left];
        arr[left] = arr[right];
        arr[right] = tempHolder;
    }
}
