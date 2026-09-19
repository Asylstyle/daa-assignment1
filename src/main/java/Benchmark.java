import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Random;

public class Benchmark {
    private static final int RUNS = 5;

    public static void main(String[] args) throws IOException {
        int[] sizes = {1_000, 10_000, 100_000, 1_000_000};
        String[] inputs = {"random", "sorted", "duplicates"};
        String[] algorithms = {"MergeSort", "QuickSort", "QuickSelect"};

        System.out.println("Warming up...");

        for (String input : inputs) {
            int[] data = createInput(10_000, input);
            int[] expected = data.clone();
            Arrays.sort(expected);

            for (String algorithm : algorithms) {
                for (int run = 0; run < RUNS; run++) {
                    runOnce(algorithm, data, expected);
                }
            }
        }

        try (BufferedWriter writer =
                     Files.newBufferedWriter(Path.of("results.csv"))) {

            writer.write(
                    "algorithm,input,n,time_ms,comparisons,max_depth");
            writer.newLine();

            for (int n : sizes) {
                for (String input : inputs) {
                    int[] data = createInput(n, input);
                    int[] expected = data.clone();
                    Arrays.sort(expected);

                    for (String algorithm : algorithms) {
                        Metrics[] results = new Metrics[RUNS];

                        for (int run = 0; run < RUNS; run++) {
                            results[run] = runOnce(
                                    algorithm, data, expected);
                        }

                        Arrays.sort(results,
                                Comparator.comparingLong(
                                        (Metrics m) -> m.timeNanos));

                        // All three metrics come from the median-time run.
                        Metrics median = results[RUNS / 2];

                        String row = String.format(
                                Locale.US,
                                "%s,%s,%d,%.6f,%d,%d",
                                algorithm,
                                input,
                                n,
                                median.getTimeMillis(),
                                median.comparisons,
                                median.maxDepth);

                        writer.write(row);
                        writer.newLine();

                        System.out.println(row);
                    }
                }
            }
        }

        System.out.println("Done! Results saved to results.csv");
    }

    private static int[] createInput(int n, String input) {
        int[] a = new int[n];
        Random random = new Random(42L + n);

        for (int i = 0; i < n; i++) {
            if (input.equals("sorted")) {
                a[i] = i;
            } else if (input.equals("duplicates")) {
                a[i] = random.nextInt(10);
            } else {
                a[i] = random.nextInt();
            }
        }

        return a;
    }

    private static Metrics runOnce(String algorithm,
                                   int[] original,
                                   int[] expected) {
        // Copying and checking are outside the algorithm's timer.
        int[] a = original.clone();
        Metrics metrics = new Metrics();

        if (algorithm.equals("MergeSort")) {
            MergeSort.sort(a, metrics);
            checkSorted(a, expected);
        } else if (algorithm.equals("QuickSort")) {
            QuickSort.sort(a, metrics);
            checkSorted(a, expected);
        } else if (algorithm.equals("QuickSelect")) {
            int k = a.length / 2;
            int result = QuickSelect.select(a, k, metrics);

            if (result != expected[k]) {
                throw new IllegalStateException(
                        "QuickSelect returned an incorrect result");
            }
        } else {
            throw new IllegalArgumentException(
                    "Unknown algorithm: " + algorithm);
        }

        return metrics;
    }

    private static void checkSorted(int[] actual, int[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new IllegalStateException(
                    "Sorting returned an incorrect result");
        }
    }
}