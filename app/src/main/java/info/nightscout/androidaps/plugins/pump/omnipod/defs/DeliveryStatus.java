package info.nightscout.androidaps.plugins.pump.omnipod.defs;

// https://github.com/openaps/openomni/wiki/Command-1D-Status-response
public enum DeliveryStatus {
    SUSPENDED((byte)0x00),
    NORMAL((byte)0x01),
    TEMP_BASAL_RUNNING((byte)0x02),
    PRIMING((byte)0x04),
    BOLUS_IN_PROGRESS((byte)0x05),
    BOLUS_AND_TEMP_BASAL((byte)0x06);

    private byte value;

    DeliveryStatus(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static DeliveryStatus fromByte(byte value) {
        for (DeliveryStatus type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown DeliveryStatus: "+ value);
    }
}
