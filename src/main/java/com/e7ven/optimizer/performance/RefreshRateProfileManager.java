package com.e7ven.optimizer.performance;

public final class RefreshRateProfileManager {

    private RefreshRateProfile profile =
            RefreshRateProfile.HZ_60;

    private double customRefreshRate = 60.0;

    public void detect(double refreshRate) {

        if (refreshRate <= 0.0) {
            return;
        }

        profile =
                RefreshRateProfile
                        .fromRefreshRate(refreshRate);

        /*
         * Если частота сильно отличается от
         * готовых профилей — используем Custom.
         */
        if (!isStandardRate(refreshRate)) {
            profile = RefreshRateProfile.CUSTOM;
            customRefreshRate = refreshRate;
        }
    }

    public void setProfile(
            RefreshRateProfile profile
    ) {
        if (profile == null) {
            return;
        }

        this.profile = profile;

        if (profile != RefreshRateProfile.CUSTOM) {
            customRefreshRate =
                    profile.getRefreshRate();
        }
    }

    public void setCustomRefreshRate(
            double refreshRate
    ) {
        if (refreshRate < 30.0) {
            refreshRate = 30.0;
        }

        if (refreshRate > 500.0) {
            refreshRate = 500.0;
        }

        customRefreshRate = refreshRate;
        profile = RefreshRateProfile.CUSTOM;
    }

    public RefreshRateProfile getProfile() {
        return profile;
    }

    public double getRefreshRate() {

        if (profile == RefreshRateProfile.CUSTOM) {
            return customRefreshRate;
        }

        return profile.getRefreshRate();
    }

    public double getTargetFrameTime() {
        return 1000.0 / getRefreshRate();
    }

    public String getProfileText() {
        return profile.getDisplayName();
    }

    private boolean isStandardRate(
            double refreshRate
    ) {
        for (RefreshRateProfile profile : RefreshRateProfile.values()) {

            if (profile == RefreshRateProfile.CUSTOM) {
                continue;
            }

            if (
                    Math.abs(
                            refreshRate
                                    - profile.getRefreshRate()
                    ) < 0.5
            ) {
                return true;
            }
        }

        return false;
    }
}
