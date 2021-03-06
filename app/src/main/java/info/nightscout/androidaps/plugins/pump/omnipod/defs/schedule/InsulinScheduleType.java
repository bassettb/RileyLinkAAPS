package info.nightscout.androidaps.plugins.pump.omnipod.defs.schedule;

public enum InsulinScheduleType {
    BASAL_SCHEDULE(0),
    TEMP_BASAL_SCHEDULE(1),
    BOLUS(2);

    private byte value;

    InsulinScheduleType(int value) {
        this.value = (byte)value;
    }

    public byte getValue() {
        return value;
    }

    public static InsulinScheduleType fromByte(byte input) {
        for (InsulinScheduleType type : values()) {
            if (type.value == input) {
                return type;
            }
        }
        return null;
    }
}
