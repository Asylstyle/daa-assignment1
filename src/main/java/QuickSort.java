import java.util.Random;

public class QuickSort {

    public static void sort(int[] a, Metrics metrics) {
        metrics.comparisons = 0;
        metrics.maxDepth = 0;
        metrics.timeNanos = 0;

        long start = System.nanoTime();
        Random random = new Random();

        if (a.length > 0) {
            sort(a, 0, a.length - 1, metrics, random, 1);
        }

        metrics.timeNanos = System.nanoTime() - start;
    }

    private static void sort(int[] a, int left, int right,
                             Metrics metrics, Random random,
                             int depth) {
        metrics.recordDepth(depth);

        while (left < right) {
            int[] bounds = partition(a, left, right, metrics, random);

            int equalStart = bounds[0];
            int equalEnd = bounds[1];

            int leftSize = equalStart - left;
            int rightSize = right - equalEnd;

            if (leftSize < rightSize) {
                if (leftSize > 1) {
                    sort(a, left, equalStart - 1,
                            metrics, random, depth + 1);
                }

                left = equalEnd + 1;
            } else {
                if (rightSize > 1) {
                    sort(a, equalEnd + 1, right,
                            metrics, random, depth + 1);
                }

                right = equalStart - 1;
            }
        }
    }

    public static int[] partition(int[] a, int left, int right,
                                  Metrics metrics, Random random) {
        int pivotIndex = left + random.nextInt(right - left + 1);
        int pivot = a[pivotIndex];

        int less = left;
        int current = left;
        int greater = right;

        while (current <= greater) {
            metrics.comparisons++;

            if (a[current] < pivot) {
                swap(a, less, current);
                less++;
                current++;
            } else {
                metrics.comparisons++;

                if (a[current] > pivot) {
                    swap(a, current, greater);
                    greater--;
                } else {
                    current++;
                }
            }
        }

        return new int[]{less, greater};
    }

    private static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}