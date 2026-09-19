import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuickSortTest {

    private void checkSort(int[] input) {
        int[] expected = input.clone();
        Arrays.sort(expected);

        int[] actual = input.clone();
        QuickSort.sort(actual, new Metrics());

        assertArrayEquals(expected, actual);
    }

    @Test
    void sortsRandomArrays() {
        Random random = new Random(42);

        for (int test = 0; test < 100; test++) {
            int[] a = new int[2 + random.nextInt(999)];

            for (int i = 0; i < a.length; i++) {
                a[i] = random.nextInt();
            }

            checkSort(a);
        }
    }

    @Test
    void sortsEdgeCases() {
        checkSort(new int[0]);
        checkSort(new int[]{7});

        int[] equal = new int[100];
        Arrays.fill(equal, 5);
        checkSort(equal);

        int[] sorted = new int[100];
        int[] reversed = new int[100];

        for (int i = 0; i < 100; i++) {
            sorted[i] = i;
            reversed[i] = 100 - i;
        }

        checkSort(sorted);
        checkSort(reversed);
    }

    @Test
    void sortsManyDuplicates() {
        Random random = new Random(123);
        int[] a = new int[10_000];

        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextInt(10);
        }

        checkSort(a);
    }

    @Test
    void limitsDepthOnSortedArray() {
        int n = 100_000;
        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = i;
        }

        int[] expected = a.clone();
        Metrics metrics = new Metrics();

        QuickSort.sort(a, metrics);

        assertArrayEquals(expected, a);

        double depthLimit = 2 * Math.log(n) / Math.log(2);
        assertTrue(metrics.maxDepth <= depthLimit);
    }

    @Test
    void handlesEqualValuesInOnePartition() {
        int[] a = new int[100_000];
        Arrays.fill(a, 8);

        Metrics metrics = new Metrics();
        QuickSort.sort(a, metrics);

        int[] expected = new int[a.length];
        Arrays.fill(expected, 8);

        assertArrayEquals(expected, a);
        assertEquals(1, metrics.maxDepth);
        assertEquals(2L * a.length, metrics.comparisons);

        QuickSort.sort(new int[0], metrics);

        assertEquals(0L, metrics.comparisons);
        assertEquals(0, metrics.maxDepth);
    }
}