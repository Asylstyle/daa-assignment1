import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class QuickSelectTest {

    @Test
    void selectsFromRandomArrays() {
        Random random = new Random(42);

        for (int test = 0; test < 100; test++) {
            int[] a = new int[1 + random.nextInt(1000)];

            for (int i = 0; i < a.length; i++) {
                a[i] = random.nextInt();
            }

            int[] sorted = a.clone();
            Arrays.sort(sorted);

            int k = random.nextInt(a.length);

            assertEquals(sorted[k],
                    QuickSelect.select(a.clone(), k, new Metrics()));

            assertEquals(sorted[0],
                    QuickSelect.select(a.clone(), 0));

            assertEquals(sorted[a.length - 1],
                    QuickSelect.select(a.clone(), a.length - 1));
        }
    }

    @Test
    void selectsFromOneElement() {
        assertEquals(7, QuickSelect.select(new int[]{7}, 0));
    }

    @Test
    void selectsFromEqualElements() {
        int[] a = new int[100];
        Arrays.fill(a, 5);

        assertEquals(5, QuickSelect.select(a, 50));
    }

    @Test
    void selectsFromSortedArray() {
        int[] a = new int[100];

        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }

        assertEquals(40, QuickSelect.select(a, 40));
    }

    @Test
    void selectsEveryPositionWithDuplicates() {
        int[] a = {4, 1, 4, -2, 9, 1, 0, 4};
        int[] sorted = a.clone();
        Arrays.sort(sorted);

        for (int k = 0; k < a.length; k++) {
            assertEquals(sorted[k],
                    QuickSelect.select(a.clone(), k));
        }
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class,
                () -> QuickSelect.select(new int[0], 0));

        assertThrows(IllegalArgumentException.class,
                () -> QuickSelect.select(null, 0));

        assertThrows(IllegalArgumentException.class,
                () -> QuickSelect.select(new int[]{1, 2}, -1));

        assertThrows(IllegalArgumentException.class,
                () -> QuickSelect.select(new int[]{1, 2}, 2));
    }

    @Test
    void recordsAndResetsMetrics() {
        Metrics metrics = new Metrics();

        assertEquals(5, QuickSelect.select(
                new int[]{5, 5, 5}, 1, metrics));

        assertEquals(6L, metrics.comparisons);
        assertEquals(1, metrics.maxDepth);

        assertEquals(9, QuickSelect.select(
                new int[]{9}, 0, metrics));

        assertEquals(0L, metrics.comparisons);
        assertEquals(1, metrics.maxDepth);
    }
}