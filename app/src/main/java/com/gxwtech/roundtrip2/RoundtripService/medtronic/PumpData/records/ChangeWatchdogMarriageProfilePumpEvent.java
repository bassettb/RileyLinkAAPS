package com.gxwtech.roundtrip2.RoundtripService.medtronic.PumpData.records;

import com.gxwtech.roundtrip2.RoundtripService.medtronic.PumpModel;

/**
 * Created by geoff on 6/5/16.
 */
public class ChangeWatchdogMarriageProfilePumpEvent extends TimeStampedRecord {
    public ChangeWatchdogMarriageProfilePumpEvent() {}

    @Override
    public String getShortTypeName() {
        return "Ch WD Marriage";
    }

    @Override
    public boolean parseFrom(byte[] data, PumpModel model) {
        return simpleParse(12,data,2);
    }
}
