package com.e7ven.optimizer.performance;

public enum RefreshRateProfile {

    HZ_60(60),
    HZ_90(90),
    HZ_120(120),
    HZ_144(144),
    HZ_165(165),
    HZ_240(240),
    CUSTOM(0);

    private final int refreshRate;

    RefreshRateProfile(int refreshRate) {
        this.refreshRate = refreshRate;
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public double getFrameTime() {
        if (refreshRate <= 0) {
            return 0.0;
        }

        return 1000.0 / refreshRate;
    }

    public String getDisplayName() {
        if (this == CUSTOM) {
            return "Custom";
        }

        return refreshRate + " Hz";
    }

    public static RefreshRateProfile fromRefreshRate(
            double refreshRate
    ) {
        RefreshRateProfile closest = HZ_60;
        double smallestDifference =
                Math.abs(refreshRate - 60.0);

        for (RefreshRateProfile profile : values()) {

            if (profile == CUSTOM) {
                continue;
            }

            double difference =
                    Math.abs(
                            refreshRate
                                    - profile.refreshRate
                    );

            if (difference < smallestDifference) {
                smallestDifference = difference;
                closest = profile;
            }
        }

        return closest;
    }
}
