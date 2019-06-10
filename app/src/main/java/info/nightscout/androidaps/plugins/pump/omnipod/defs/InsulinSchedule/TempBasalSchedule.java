package info.nightscout.androidaps.plugins.pump.omnipod.defs.InsulinSchedule;

import org.apache.commons.lang3.NotImplementedException;

import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.IRawRepresentable;

public class TempBasalSchedule extends DeliverySchedule implements IRawRepresentable {

    @Override
    public byte[] getRawData() {
        byte[] rawData = new byte[0];
        throw new NotImplementedException("TEMP_BASAL_SCHEDULE.getRawData");
    }

    @Override
    public InsulinScheduleType getType() {
        return InsulinScheduleType.TEMP_BASAL_SCHEDULE;
    }

    @Override
    public int checksum() {
        throw new NotImplementedException("InsulinScheduleType.checksum");
        //return 0;
    }
}
