import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Main {
    private static final int RANDOM = 0;
    private static final int SORTED = 1;
    private static final int REVERSE = 2;
    private static long timestamp;

    public static void main(String[] args) {
        //args
        int startR = 305;
        int endR = 320;
        int step = 1;
        int tests = 100000;
        int dataSet = RANDOM;
        String key = tests + "test" + step + "step";

        DecimalFormat format = new DecimalFormat("0.0000");

        PrintWriter writer = getWriter(key + dataSet + ".csv");
        writer.write("N, insertionTime, mergeTime\n");

        //number of elements
        for (int n = startR; n < endR; n = n + step) {
            System.out.println(n);
            double merge = 0;
            double insertion = 0;

            //tests
            for (int i = 0; i < tests; i++) {
                //cases of data sets
                int[] data = new int[n];
                switch (dataSet) {
                    case RANDOM:
                        data = getRandomArray(n);
                        break;
                    case SORTED:
                        data = getSortedArray(n);
                        break;
                    case REVERSE:
                        data = getReverseArray(n);
                        break;
                    default:
                        System.out.println("No such data set: " + dataSet);
                }

                //merge sort doesn't change the initial array
                startTimer();
                int[] result = mergeSort(data);
                //double currentI = endTimer();
                merge = merge + endTimer();
                if (!isSorted(result))
                    System.out.println("Array is not sorted correctly.");

                //insertion sort is in place so it sorts the original array
                startTimer();
                result = insertionSort(data);
                //double currentM = endTimer();
                insertion = insertion + endTimer();
                if (!isSorted(result))
                    System.out.println("Array is not sorted correctly.");

                //writer.write(n + ", " + (int)(currentI*1000000) + ", " + (int)(currentM*1000000) + "\n");
            }


            String row = n + ", " + format.format(insertion / tests) + ", " + format.format(merge / tests) + ", "
                    + format.format(insertion / tests - merge / tests) + "\n";
            writer.write(row);
        }
        writer.close();
    }

    /**
     * Insertion sort in place.
     *
     * @param data the array to sort
     * @return the sorted array
     */
    private static int[] insertionSort(int[] data) {
        for (int i = 1; i < data.length; i++) {
            int toInsert = data[i];
            int indexToInsert = i;
            int pos = i - 1;
            while (pos >= 0 && data[pos] > toInsert) {
                swap(indexToInsert, pos, data);
                toInsert = data[pos];
                indexToInsert--;
                pos--;
            }
        }
        return data;
    }

    /**
     * Helper method to swap two elements in an array.
     *
     * @param i    index of the first element
     * @param j    index of the second element
     * @param data the array in which the swap take place
     */
    private static void swap(int i, int j, int[] data) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    /**
     * Merge sort.
     *
     * @param data the array to br sorted
     * @return a sorted array
     */

    private static int[] mergeSort(int[] data) {
        int n = data.length;
        if (n <= 1)
            return data;
        else {
            int[] p1 = Arrays.copyOfRange(data, 0, n / 2);
            int[] p2 = Arrays.copyOfRange(data, n / 2, n);
            p1 = mergeSort(p1);
            p2 = mergeSort(p2);
            return merge(p1, p2);
        }
    }

    /**
     * The merge step from the merge sort.
     *
     * @param left  the first list
     * @param right the second list
     * @return a sorted array merged from the upper two
     */

    private static int[] merge(int[] left, int[] right) {
        int size = left.length + right.length;
        int[] sorted = new int[size];
        int l = 0, r = 0, current = 0;
        while (l < left.length && r < right.length) {
            if (left[l] < right[r]) {
                sorted[current] = left[l];
                l++;
            } else {
                sorted[current] = right[r];
                r++;
            }
            current++;
        }

        while (l < left.length) {
            sorted[current] = left[l];
            l++;
            current++;
        }

        while (r < right.length) {
            sorted[current] = right[r];
            r++;
            current++;
        }
        return sorted;
    }

    /**
     * Generate an array with random values between 0 and n.
     *
     * @param n the length of the array
     * @return the generated array
     */
    private static int[] getRandomArray(int n) {
        int[] temp = new int[n];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = (int) (Math.random() * n) + n;
        }
        return temp;
    }

    /**
     * Generates a sorted array with values from 0 to n-1.
     *
     * @param n the length of the array
     * @return the sorted array
     */

    private static int[] getSortedArray(int n) {
        int[] temp = new int[n];
        for (int i = 0; i < n; i++) {
            temp[i] = i;
        }
        return temp;
    }

    /**
     * Generates a reverse array with values from n-1 to 0.
     *
     * @param n the length of the array
     * @return the reverse array
     */

    private static int[] getReverseArray(int n) {
        int[] temp = new int[n];
        for (int i = 0; i < n; i++) {
            temp[n - 1 - i] = i;
        }
        return temp;
    }

    /**
     * Storing the current time(nanoseconds) in timestamp.
     */
    private static void startTimer() {
        timestamp = System.nanoTime();
    }

    /**
     * Stopping the "timer".
     *
     * @return the difference between the current time and the timestamp(from startTime) in milliseconds
     */
    private static double endTimer() {
        return (System.nanoTime() - timestamp) / 1000000.0;
    }

    /**
     * Helper method to check if the sorts are working correctly.
     *
     * @param data the array to check
     * @return true if the array is sorted
     */
    private static boolean isSorted(int[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            if (data[i] > data[i + 1])
                return false;
        }
        return true;
    }

    /**
     * Getting a write printer for a certain file.
     *
     * @param report the name of the file
     * @return print writer for the file
     */

    private static PrintWriter getWriter(String report) {
        PrintWriter writer = null;
        File file = new File(report);
        try {
            file.createNewFile();
            writer = new PrintWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer;
    }


}
