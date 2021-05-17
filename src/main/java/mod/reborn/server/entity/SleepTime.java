package mod.reborn.server.entity;

public enum SleepTime {
    DIURNAL(fromTimeToTicks(6, 0), fromTimeToTicks(22, 0)),
    NOCTURNAL(fromTimeToTicks(18, 0), fromTimeToTicks(6, 0)),
    CREPUSCULAR(fromTimeToTicks(12, 30), fromTimeToTicks(4, 30)),
    NO_SLEEP(fromTimeToTicks(0, 0), fromTimeToTicks(0, 0));

    private int wakeUpTime;
    private int sleepTime;
    private boolean sleep;

    SleepTime(int wakeUpTime, int sleepTime) {
        this.wakeUpTime = wakeUpTime;
        this.sleepTime = sleepTime;
        this.sleep = wakeUpTime != sleepTime;
    }

    public static int fromTimeToTicks(int hour, int minute) {
        int ticksPerMinute = 1000 / 60;
        return ((hour - 6) * 1000) + (minute * ticksPerMinute);
    }

    public int getWakeUpTime() {
        return this.wakeUpTime;
    }

    public int getSleepTime() {
        return this.sleepTime;
    }

    public boolean shouldSleepNextDay() {
        return this.wakeUpTime > this.sleepTime;
    }

    public boolean shouldSleep() {
        return this.sleep;
    }

    public int getAwakeTime() {
        int newSleepTime = this.sleepTime;

        if (this.shouldSleepNextDay()) {
            newSleepTime += 24000;
        }

        return newSleepTime - this.wakeUpTime;
    }
}
