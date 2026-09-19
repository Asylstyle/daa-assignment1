public class Metrics {
    public long comparisons = 0;
    public int maxDepth = 0;
    public long timeNanos = 0;

    public void recordDepth(int depth) {
        if (depth > maxDepth) {
            maxDepth = depth;
        }
    }

    public double getTimeMillis() {
        return timeNanos / 1_000_000.0;
    }
}
