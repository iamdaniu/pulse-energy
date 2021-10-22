package de.daniu.pulseenergy.domain;

class UsageCalculator {
    private static final double SECONDS_PER_HOUR = 60 * 60;

    private final double pulsesPerKWh;
    private final double secondsPerKWh;

    UsageCalculator(double pulsesPerKWh) {
        this.pulsesPerKWh = pulsesPerKWh;
        secondsPerKWh = SECONDS_PER_HOUR / pulsesPerKWh;
    }

    public double usagePerPulseKWh() {
        return 1d / pulsesPerKWh;
    }

    public double getTotalUsageKWh(long pulseCount) {
        return (double) pulseCount / pulsesPerKWh;
    }

    public int getUsage(double secondsBetweenPulses) {
        double result = 0;
        if (secondsBetweenPulses != 0) {
            result = 1000d * secondsPerKWh / secondsBetweenPulses;
        }
        return (int) result;
    }
}
