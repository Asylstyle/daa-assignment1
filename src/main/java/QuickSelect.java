import java.util.Random;

public class QuickSelect {

    public static int select(int[] a, int k) {
        return select(a, k, new Metrics());
    }

    public static int select(int[] a, int k, Metrics metrics) {
        if (a == null || a.length == 0) {
            throw new IllegalArgumentException(
                    "Array must not be null or empty");
        }

        if (k < 0 || k >= a.length) {
            throw new IllegalArgumentException(
                    "k must be between 0 and " + (a.length - 1));
        }

        metrics.comparisons = 0;
        metrics.maxDepth = 0;
        metrics.timeNanos = 0;

        long start = System.nanoTime();

        Random random = new Random();
        int left = 0;
        int right = a.length - 1;

        // This method uses a loop, so its call depth stays at 1.
        metrics.recordDepth(1);

        while (left < right) {
            int[] bounds = QuickSort.partition(
                    a, left, right, metrics, random);

            int equalStart = bounds[0];
            int equalEnd = bounds[1];

            if (k < equalStart) {
                right = equalStart - 1;
            } else if (k > equalEnd) {
                left = equalEnd + 1;
            } else {
                metrics.timeNanos = System.nanoTime() - start;
                return a[k];
            }
        }

        metrics.timeNanos = System.nanoTime() - start;
        return a[left];
    }
}