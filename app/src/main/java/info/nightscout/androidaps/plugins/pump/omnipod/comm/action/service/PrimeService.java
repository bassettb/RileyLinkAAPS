package info.nightscout.androidaps.plugins.pump.omnipod.comm.action.service;

import org.joda.time.Duration;

import java.util.Collections;
import java.util.List;

import info.nightscout.androidaps.plugins.pump.omnipod.comm.OmnipodCommunicationService;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.action.BolusAction;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.action.ConfigureAlertsAction;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.OmnipodMessage;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.command.FaultConfigCommand;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.response.StatusResponse;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.AlertConfiguration;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.AlertConfigurationFactory;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.state.PodSessionState;

public class PrimeService {
    public StatusResponse executeDisableTab5Sub16FaultConfigCommand(OmnipodCommunicationService communicationService, PodSessionState podState) {
        FaultConfigCommand faultConfigCommand = new FaultConfigCommand(podState.getCurrentNonce(), (byte) 0x00, (byte) 0x00);
        OmnipodMessage faultConfigMessage = new OmnipodMessage(podState.getAddress(),
                Collections.singletonList(faultConfigCommand), podState.getMessageNumber());
        return communicationService.exchangeMessages(StatusResponse.class, podState, faultConfigMessage);
    }

    public StatusResponse executeFinishSetupReminderAlertCommand(OmnipodCommunicationService communicationService, PodSessionState podState) {
        List<AlertConfiguration> alertConfigurations = Collections.singletonList(
                AlertConfigurationFactory.createFinishSetupReminderAlertConfiguration());
        return communicationService.executeAction(new ConfigureAlertsAction(podState, alertConfigurations));
    }

    public StatusResponse executePrimeBolusCommand(OmnipodCommunicationService communicationService, PodSessionState podState) {
        return communicationService.executeAction(new BolusAction(podState, 2.6, Duration.standardSeconds(1)));
    }
}
