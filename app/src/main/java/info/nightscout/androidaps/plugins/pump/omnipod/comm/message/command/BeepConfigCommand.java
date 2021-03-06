package info.nightscout.androidaps.plugins.pump.omnipod.comm.message.command;

import org.joda.time.Duration;

import info.nightscout.androidaps.plugins.pump.common.utils.ByteUtil;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.MessageBlock;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.BeepType;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.MessageBlockType;

public class BeepConfigCommand extends MessageBlock {
    private final BeepType beepType;
    private final boolean basalCompletionBeep;
    private final Duration basalIntervalBeep;
    private final boolean tempBasalCompletionBeep;
    private final Duration tempBasalIntervalBeep;
    private final boolean bolusCompletionBeep;
    private final Duration bolusIntervalBeep;

    public BeepConfigCommand(BeepType beepType, boolean basalCompletionBeep, Duration basalIntervalBeep,
                             boolean tempBasalCompletionBeep, Duration tempBasalIntervalBeep,
                             boolean bolusCompletionBeep, Duration bolusIntervalBeep) {
        this.beepType = beepType;
        this.basalCompletionBeep = basalCompletionBeep;
        this.basalIntervalBeep = basalIntervalBeep;
        this.tempBasalCompletionBeep = tempBasalCompletionBeep;
        this.tempBasalIntervalBeep = tempBasalIntervalBeep;
        this.bolusCompletionBeep = bolusCompletionBeep;
        this.bolusIntervalBeep = bolusIntervalBeep;

        encode();
    }

    public BeepConfigCommand(BeepType beepType) {
        this(beepType, false, Duration.ZERO, false, Duration.ZERO, false, Duration.ZERO);
    }

    private void encode() {
        encodedData = new byte[] { beepType.getValue() };
        encodedData = ByteUtil.concat(encodedData, (byte)((basalCompletionBeep ? (1 << 6) : 0) + (basalIntervalBeep.getStandardMinutes() & 0x3f)));
        encodedData = ByteUtil.concat(encodedData, (byte)((tempBasalCompletionBeep ? (1 << 6) : 0) + (tempBasalIntervalBeep.getStandardMinutes() & 0x3f)));
        encodedData = ByteUtil.concat(encodedData, (byte)((bolusCompletionBeep ? (1 << 6) : 0) + (bolusIntervalBeep.getStandardMinutes() & 0x3f)));
    }

    @Override
    public MessageBlockType getType() {
        return MessageBlockType.BEEP_CONFIG;
    }
}
