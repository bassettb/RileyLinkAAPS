package info.nightscout.androidaps.plugins.pump.omnipod.comm.action;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Arrays;

import info.nightscout.androidaps.plugins.pump.omnipod.comm.OmnipodCommunicationService;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.OmnipodMessage;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.command.BasalScheduleExtraCommand;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.command.SetInsulinScheduleCommand;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.response.StatusResponse;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.schedule.BasalSchedule;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.state.PodSessionState;

public class SetBasalScheduleAction implements OmnipodAction<StatusResponse> {
    private final PodSessionState podState;
    private final BasalSchedule basalSchedule;
    private final boolean confidenceReminder;
    private final Duration scheduleOffset;

    public SetBasalScheduleAction(PodSessionState podState, BasalSchedule basalSchedule,
                                  boolean confidenceReminder, Duration scheduleOffset) {
        if(podState == null) {
            throw new IllegalArgumentException("Pod state cannot be null");
        }
        if(basalSchedule == null) {
            throw new IllegalArgumentException("Basal schedule cannot be null");
        }
        if(scheduleOffset == null) {
            throw new IllegalArgumentException("Schedule offset cannot be null");
        }
        this.podState = podState;
        this.basalSchedule = basalSchedule;
        this.confidenceReminder = confidenceReminder;
        this.scheduleOffset = scheduleOffset;
    }

    @Override
    public StatusResponse execute(OmnipodCommunicationService communicationService) {
        SetInsulinScheduleCommand setBasal = new SetInsulinScheduleCommand(podState.getCurrentNonce(), basalSchedule, scheduleOffset);
        BasalScheduleExtraCommand extraCommand = new BasalScheduleExtraCommand(basalSchedule, scheduleOffset,
                true, confidenceReminder, Duration.ZERO);
        OmnipodMessage basalMessage = new OmnipodMessage(podState.getAddress(), Arrays.asList(setBasal, extraCommand),
                podState.getMessageNumber());

        StatusResponse statusResponse = communicationService.exchangeMessages(StatusResponse.class, podState, basalMessage);
        podState.setBasalSchedule(basalSchedule);
        return statusResponse;
    }

    public static Duration calculateScheduleOffset(DateTime dateTime) {
        return new Duration(new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(), 0, 0, 0), dateTime);
    }
}
