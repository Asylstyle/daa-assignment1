import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MergeSortTest {

    private void checkSort(int[] input) {
        int[] expected = input.clone();
        Arrays.sort(expected);

        int[] actual = input.clone();
        MergeSort.sort(actual, new Metrics());

        assertArrayEquals(expected, actual);
    }

    @Test
    void sortsRandomArrays() {
        Random random = new Random(42);

        for (int test = 0; test < 100; test++) {
            int[] a = new int[16 + random.nextInt(985)];

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
        for (int i = 0; i < sorted.length; i++) {
            sorted[i] = i;
        }
        checkSort(sorted);
    }

    @Test
    void sortsAroundCutoff() {
        for (int size = 14; size <= 16; size++) {
            int[] a = new int[size];

            for (int i = 0; i < size; i++) {
                a[i] = size - i;
            }

            checkSort(a);
        }
    }

    @Test
    void countsComparisonsAndResetsMetrics() {
        Metrics metrics = new Metrics();

        MergeSort.sort(new int[]{3, 2, 1}, metrics);

        assertEquals(3L, metrics.comparisons);
        assertEquals(1, metrics.maxDepth);

        MergeSort.sort(new int[0], metrics);

        assertEquals(0L, metrics.comparisons);
        assertEquals(0, metrics.maxDepth);
    }
}