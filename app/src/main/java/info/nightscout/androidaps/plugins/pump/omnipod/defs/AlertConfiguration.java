package info.nightscout.androidaps.plugins.pump.omnipod.defs;

import org.joda.time.Duration;

import info.nightscout.androidaps.Constants;
import info.nightscout.androidaps.plugins.pump.common.utils.ByteUtil;

public class AlertConfiguration {
    private final AlertType alertType;
    private final boolean audible;
    private final boolean autoOffModifier;
    private final Duration duration;
    private final AlertTrigger<?> alertTrigger;
    private final BeepRepeat beepRepeat;
    private final BeepType beepType;

    public AlertConfiguration(AlertType alertType, boolean audible, boolean autoOffModifier,
                              Duration duration, AlertTrigger alertTrigger,
                              BeepType beepType, BeepRepeat beepRepeat) {
        this.alertType = alertType;
        this.audible = audible;
        this.autoOffModifier = autoOffModifier;
        this.duration = duration;
        this.alertTrigger = alertTrigger;
        this.beepRepeat = beepRepeat;
        this.beepType = beepType;
    }

    public byte[] getRawData() {
        int firstByte = (alertType.getValue() << 4);
        firstByte += audible ? (1 << 3) : 0;

        if(alertTrigger instanceof UnitsRemainingAlertTrigger) {
            firstByte += 1 << 2;
        }

        if(autoOffModifier) {
            firstByte += 1 << 1;
        }

        firstByte += ((int)duration.getStandardMinutes() >>> 8) & 0x1;

        byte[] encodedData = new byte[] {
                (byte)firstByte,
                (byte)duration.getStandardMinutes()
        };

        if(alertTrigger instanceof UnitsRemainingAlertTrigger) {
            int ticks = (int)(((UnitsRemainingAlertTrigger)alertTrigger).getValue() / Constants.POD_PULSE_SIZE / 2);
            encodedData = ByteUtil.concat(encodedData, ByteUtil.getBytesFromInt16(ticks));
        } else if(alertTrigger instanceof TimerAlertTrigger) {
            int durationInMinutes = (int) ((TimerAlertTrigger)alertTrigger).getValue().getStandardMinutes();
            encodedData = ByteUtil.concat(encodedData, ByteUtil.getBytesFromInt16(durationInMinutes));
        }

        encodedData = ByteUtil.concat(encodedData, beepType.getValue());
        encodedData = ByteUtil.concat(encodedData, beepRepeat.getValue());

        return encodedData;
    }
}
