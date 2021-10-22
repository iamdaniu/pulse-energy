package de.daniu.pulseenergy.domain;

class UsageCalculator {
    private static final double SECONDS_PER_HOUR = 60 * 60;

    private final double pulsesPerKWh;

    UsageCalculator(double pulsesPerKWh) {
        this.pulsesPerKWh = pulsesPerKWh;
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
            double pulsesPerSecond = 1.0d / secondsBetweenPulses;
            double pulsesPerHour = SECONDS_PER_HOUR * pulsesPerSecond;
            result = 1000 * pulsesPerHour / pulsesPerKWh;
        }
        return (int) result;
    }
}
