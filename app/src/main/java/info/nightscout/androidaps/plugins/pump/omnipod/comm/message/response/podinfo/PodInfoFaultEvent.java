package info.nightscout.androidaps.plugins.pump.omnipod.comm.message.response.podinfo;

import org.joda.time.Duration;

import info.nightscout.androidaps.Constants;
import info.nightscout.androidaps.plugins.pump.common.utils.ByteUtil;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.AlertSet;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.DeliveryStatus;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.FaultEventCode;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.LogEventErrorCode;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.PodInfoType;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.PodProgressStatus;

public class PodInfoFaultEvent extends PodInfo {
    private static final int MINIMUM_MESSAGE_LENGTH = 21;

    private final PodProgressStatus podProgressStatus;
    private final DeliveryStatus deliveryStatus;
    private final double insulinNotDelivered;
    private final byte podMessageCounter;
    private final double totalInsulinDelivered;
    private final FaultEventCode currentStatus;
    private final Duration faultEventTimeSinceActivation;
    private Double reservoirLevel;
    private final Duration timeActive;
    private final AlertSet unacknowledgedAlerts;
    private final boolean faultAccessingTables;
    private final LogEventErrorCode logEventErrorType;
    private final PodProgressStatus logEventErrorPodProgressStatus;
    private final byte receiverLowGain;
    private final byte radioRSSI;
    private final PodProgressStatus previousPodProgressStatus;
    private final byte[] unknownValue;

    public PodInfoFaultEvent(byte[] encodedData) {
        super(encodedData);

        if(encodedData.length < MINIMUM_MESSAGE_LENGTH) {
            throw new IllegalArgumentException("Not enough data");
        }

        podProgressStatus = PodProgressStatus.fromByte(encodedData[1]);
        deliveryStatus = DeliveryStatus.fromByte((byte)(encodedData[2] & 0xf));
        insulinNotDelivered = Constants.POD_PULSE_SIZE * (((encodedData[3] & 0x3) << 8) | encodedData[4]);
        podMessageCounter = encodedData[5];
        totalInsulinDelivered = Constants.POD_PULSE_SIZE * ((encodedData[6] << 8) | encodedData[7]);
        currentStatus = FaultEventCode.fromByte(encodedData[8]);

        byte[] minutesSinceActivationBytes = ByteUtil.substring(getEncodedData(), 9, 2);
        int minutesSinceActivation = ByteUtil.toInt(minutesSinceActivationBytes[0], minutesSinceActivationBytes[2]);
        if(minutesSinceActivation == 0xffff) {
            faultEventTimeSinceActivation = null;
        } else {
            faultEventTimeSinceActivation = Duration.standardMinutes(minutesSinceActivation);
        }

        double reservoirValue = ((encodedData[11] & 0x3) << 8) + encodedData[12] * Constants.POD_PULSE_SIZE;
        if(reservoirValue > Constants.MAX_RSERVOIR_READING) {
            reservoirLevel = null;
        } else {
            reservoirLevel = reservoirValue;
        }

        byte[] timeActiveBytes = ByteUtil.substring(encodedData, 13, 2);
        int minutesActive = ByteUtil.toInt(timeActiveBytes[0], timeActiveBytes[1]);
        timeActive = Duration.standardMinutes(minutesActive);

        unacknowledgedAlerts = new AlertSet(encodedData[15]);
        faultAccessingTables = encodedData[16] == 0x02;
        logEventErrorType = LogEventErrorCode.fromByte((byte)(encodedData[17] >> 4));
        logEventErrorPodProgressStatus = PodProgressStatus.fromByte((byte)(encodedData[17] & 0xf));
        receiverLowGain = (byte)(encodedData[18] >> 6);
        radioRSSI = (byte)(encodedData[18] & 0x3f);
        previousPodProgressStatus = PodProgressStatus.fromByte((byte)(encodedData[19] & 0xf));
        unknownValue = ByteUtil.substring(encodedData, 20, 2);
    }

    @Override
    public PodInfoType getType() {
        return PodInfoType.FAULT_EVENTS;
    }

    public PodProgressStatus getPodProgressStatus() {
        return podProgressStatus;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public double getInsulinNotDelivered() {
        return insulinNotDelivered;
    }

    public byte getPodMessageCounter() {
        return podMessageCounter;
    }

    public double getTotalInsulinDelivered() {
        return totalInsulinDelivered;
    }

    public FaultEventCode getCurrentStatus() {
        return currentStatus;
    }

    public Duration getFaultEventTimeSinceActivation() {
        return faultEventTimeSinceActivation;
    }

    public Double getReservoirLevel() {
        return reservoirLevel;
    }

    public Duration getTimeActive() {
        return timeActive;
    }

    public AlertSet getUnacknowledgedAlerts() {
        return unacknowledgedAlerts;
    }

    public boolean isFaultAccessingTables() {
        return faultAccessingTables;
    }

    public LogEventErrorCode getLogEventErrorType() {
        return logEventErrorType;
    }

    public PodProgressStatus getLogEventErrorPodProgressStatus() {
        return logEventErrorPodProgressStatus;
    }

    public byte getReceiverLowGain() {
        return receiverLowGain;
    }

    public byte getRadioRSSI() {
        return radioRSSI;
    }

    public PodProgressStatus getPreviousPodProgressStatus() {
        return previousPodProgressStatus;
    }

    public byte[] getUnknownValue() {
        return unknownValue;
    }
}
