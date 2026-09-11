package com.e7ven.optimizer.performance;

public final class FrameTimeMonitor {

    private static final int HISTORY_SIZE = 240;
    private static final int LOW_SAMPLE_COUNT = 24;

    private static final double NANOS_TO_MILLIS = 1_000_000.0;

    private final double[] frameTimes =
            new double[HISTORY_SIZE];

    private int writeIndex = 0;
    private int sampleCount = 0;

    private long frameStartNanos = 0L;

    private double currentFrameTime = 0.0;
    private double averageFrameTime = 0.0;
    private double onePercentLowFrameTime = 0.0;
    private double worstFrameTime = 0.0;

    public void beginFrame() {
        frameStartNanos = System.nanoTime();
    }

    public void endFrame() {

        if (frameStartNanos == 0L) {
            return;
        }

        long elapsedNanos =
                System.nanoTime() - frameStartNanos;

        frameStartNanos = 0L;

        double frameTime =
                elapsedNanos / NANOS_TO_MILLIS;

        if (frameTime <= 0.0
                || frameTime > 10_000.0) {
            return;
        }

        currentFrameTime = frameTime;

        frameTimes[writeIndex] =
                frameTime;

        writeIndex =
                (writeIndex + 1)
                        % HISTORY_SIZE;

        if (sampleCount < HISTORY_SIZE) {
            sampleCount++;
        }

        recalculateStatistics();
    }

    private void recalculateStatistics() {

        if (sampleCount == 0) {
            return;
        }

        double total = 0.0;
        double worst = 0.0;

        for (int i = 0; i < sampleCount; i++) {

            double value =
                    frameTimes[i];

            total += value;

            if (value > worst) {
                worst = value;
            }
        }

        averageFrameTime =
                total / sampleCount;

        worstFrameTime =
                worst;

        onePercentLowFrameTime =
                calculateOnePercentLow();
    }

    private double calculateOnePercentLow() {

        if (sampleCount == 0) {
            return 0.0;
        }

        /*
         * For a 240-frame history, the slowest
         * 1% is only about 2-3 samples.
         *
         * We use a small reusable-sized local
         * array instead of sorting the entire
         * history every frame.
         */

        int count =
                Math.min(
                        LOW_SAMPLE_COUNT,
                        sampleCount
                );

        double[] slowest =
                new double[count];

        int slowestCount = 0;

        for (int i = 0; i < sampleCount; i++) {

            double value =
                    frameTimes[i];

            if (slowestCount < count) {

                slowest[slowestCount] =
                        value;

                slowestCount++;

                continue;
            }

            int smallestIndex = 0;

            for (int j = 1;
                 j < slowestCount;
                 j++) {

                if (
                        slowest[j]
                                < slowest[smallestIndex]
                ) {
                    smallestIndex = j;
                }
            }

            if (
                    value
                            > slowest[smallestIndex]
            ) {
                slowest[smallestIndex] =
                        value;
            }
        }

        /*
         * We only need the slowest 1%.
         */

        int required =
                Math.max(
                        1,
                        (int) Math.ceil(
                                sampleCount * 0.01
                        )
                );

        required =
                Math.min(
                        required,
                        slowestCount
                );

        double total = 0.0;

        for (int i = 0; i < required; i++) {

            int largestIndex = i;

            for (int j = i + 1;
                 j < slowestCount;
                 j++) {

                if (
                        slowest[j]
                                > slowest[largestIndex]
                ) {
                    largestIndex = j;
                }
            }

            double temp =
                    slowest[i];

            slowest[i] =
                    slowest[largestIndex];

            slowest[largestIndex] =
                    temp;

            total +=
                    slowest[i];
        }

        return total / required;
    }

    public double getCurrentFrameTime() {
        return currentFrameTime;
    }

    public double getAverageFrameTime() {
        return averageFrameTime;
    }

    public double getOnePercentLowFrameTime() {
        return onePercentLowFrameTime;
    }

    public double getWorstFrameTime() {
        return worstFrameTime;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public double getFrameTime(int index) {

        if (index < 0
                || index >= HISTORY_SIZE) {

            throw new IndexOutOfBoundsException(
                    "Frame time index: " + index
            );
        }

        return frameTimes[index];
    }

    public double[] getHistoryCopy() {

        double[] copy =
                new double[sampleCount];

        for (int i = 0;
             i < sampleCount;
             i++) {

            int index =
                    (writeIndex
                            - sampleCount
                            + i
                            + HISTORY_SIZE)
                            % HISTORY_SIZE;

            copy[i] =
                    frameTimes[index];
        }

        return copy;
    }

    public int getHistorySize() {
        return HISTORY_SIZE;
    }

    public void reset() {

        for (int i = 0;
             i < HISTORY_SIZE;
             i++) {

            frameTimes[i] = 0.0;
        }

        writeIndex = 0;
        sampleCount = 0;

        frameStartNanos = 0L;

        currentFrameTime = 0.0;
        averageFrameTime = 0.0;
        onePercentLowFrameTime = 0.0;
        worstFrameTime = 0.0;
    }
}
