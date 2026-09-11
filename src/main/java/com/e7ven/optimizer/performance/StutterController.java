package com.e7ven.optimizer.performance;

public final class StutterController {

    private static final double SPIKE_MULTIPLIER = 1.50;
    private static final double SEVERE_SPIKE_MULTIPLIER = 2.50;

    private static final int SPIKE_HISTORY_SIZE = 60;

    private final boolean[] spikeHistory =
            new boolean[SPIKE_HISTORY_SIZE];

    private int writeIndex = 0;
    private int sampleCount = 0;

    private int consecutiveSpikes = 0;
    private int totalSpikes = 0;

    private boolean spikeDetected;
    private boolean severeSpikeDetected;

    public void update(
            double currentFrameTime,
            double averageFrameTime
    ) {
        if (currentFrameTime <= 0.0 || averageFrameTime <= 0.0) {
            return;
        }

        spikeDetected = false;
        severeSpikeDetected = false;

        double ratio =
                currentFrameTime / averageFrameTime;

        if (ratio >= SEVERE_SPIKE_MULTIPLIER) {

            severeSpikeDetected = true;
            spikeDetected = true;

        } else if (ratio >= SPIKE_MULTIPLIER) {

            spikeDetected = true;
        }

        recordSpike(spikeDetected);

        if (spikeDetected) {
            consecutiveSpikes++;
            totalSpikes++;
        } else {
            consecutiveSpikes = 0;
        }
    }

    private void recordSpike(boolean spike) {

        spikeHistory[writeIndex] = spike;

        writeIndex =
                (writeIndex + 1)
                        % SPIKE_HISTORY_SIZE;

        if (sampleCount < SPIKE_HISTORY_SIZE) {
            sampleCount++;
        }
    }

    public boolean isSpikeDetected() {
        return spikeDetected;
    }

    public boolean isSevereSpikeDetected() {
        return severeSpikeDetected;
    }

    public int getConsecutiveSpikes() {
        return consecutiveSpikes;
    }

    public int getTotalSpikes() {
        return totalSpikes;
    }

    public int getRecentSpikeCount() {

        int count = 0;

        for (int i = 0; i < sampleCount; i++) {
            if (spikeHistory[i]) {
                count++;
            }
        }

        return count;
    }

    public double getSpikeRate() {

        if (sampleCount == 0) {
            return 0.0;
        }

        return
                (double) getRecentSpikeCount()
                        / sampleCount
                        * 100.0;
    }

    public void reset() {

        for (int i = 0;
             i < SPIKE_HISTORY_SIZE;
             i++) {

            spikeHistory[i] = false;
        }

        writeIndex = 0;
        sampleCount = 0;

        consecutiveSpikes = 0;
        totalSpikes = 0;

        spikeDetected = false;
        severeSpikeDetected = false;
    }

    public String getSpikeRateText() {

        return String.format(
                "%.1f%%",
                getSpikeRate()
        );
    }
}
