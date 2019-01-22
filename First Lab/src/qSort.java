public class qSort {

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


    private static void sort(int[] array, int startingIndex, int lastIndex) {

        int pivotIndex = getPivotIndex(startingIndex, lastIndex);

        int pivot = array[pivotIndex];

        swap(array, pivotIndex, lastIndex);

        int pivotPos = swapValues(array, startingIndex - 1, lastIndex, pivot);

        swap(array, pivotPos, lastIndex);


        try {

            //Call for the first part of the array
            if (pivotPos - startingIndex > 1) {
                Thread threadLeft = new Thread(() -> sort(array, startingIndex, pivotPos - 1));
                threadLeft.start();
                threadLeft.join();
            }


            //Call for the second part of the array
            if (lastIndex - pivotPos > 1) {
                Thread threadRight = new Thread(() -> sort(array, pivotPos + 1, lastIndex));
                threadRight.start();
                threadRight.join();
            }

        } catch (InterruptedException ex) {
            System.out.println("Thread error");
        }


    }

    private static void swap(int[] arr, int left, int right) {
        int tempHolder = arr[left];
        arr[left] = arr[right];
        arr[right] = tempHolder;
    }
}
