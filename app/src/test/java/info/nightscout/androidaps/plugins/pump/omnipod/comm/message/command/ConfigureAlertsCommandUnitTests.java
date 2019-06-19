package info.nightscout.androidaps.plugins.pump.omnipod.comm.message.command;

import org.joda.time.Duration;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import info.nightscout.androidaps.plugins.pump.common.utils.ByteUtil;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.AlertConfiguration;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.AlertType;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.BeepRepeat;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.BeepType;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.ExpirationAdvisory;

import static org.junit.Assert.assertArrayEquals;

public class ConfigureAlertsCommandUnitTests {
    @Test
    public void testMessageCorrect() {
        // from https://github.com/ps2/rileylink_ios/blob/master/OmniKitTests/MessageTests.swift

        Duration softExpirationTime = Duration.standardHours(72).minus(Duration.standardMinutes(1));
        AlertConfiguration alertConfiguration1 = new AlertConfiguration( //
                AlertType.TIMER_LIMIT, //
                true, //
                false, //
                Duration.standardHours(7), //
                new ExpirationAdvisory(ExpirationAdvisory.ExpirationType.TIMER, softExpirationTime), //
                BeepType.BEEP_BEEP_BEEP, //
                BeepRepeat.EVERY_MINUTE_FOR_15_MINUTES);

        assertArrayEquals( //
                ByteUtil.fromHexString("79a410df0502"), //
                alertConfiguration1.getRawData());

        Duration hardExpirationTime = Duration.standardHours(79).minus(Duration.standardMinutes(1));
        AlertConfiguration alertConfiguration2 = new AlertConfiguration( //
                AlertType.END_OF_SERVICE, //
                true, //
                false, //
                Duration.ZERO, //
                new ExpirationAdvisory(ExpirationAdvisory.ExpirationType.TIMER, hardExpirationTime), //
                BeepType.BEEEEEEP, //
                BeepRepeat.EVERY_MINUTE_FOR_15_MINUTES);

        assertArrayEquals( //
                ByteUtil.fromHexString("280012830602"), //
                alertConfiguration2.getRawData());

        AlertConfiguration alertConfiguration3 = new AlertConfiguration( //
                AlertType.AUTO_OFF, //
                false, //
                true, //
                Duration.standardMinutes(15), //
                new ExpirationAdvisory(ExpirationAdvisory.ExpirationType.TIMER, Duration.ZERO), //
                BeepType.BIP_BEEP_BIP_BEEP_BIP_BEEP_BIP_BEEP, //
                BeepRepeat.EVERY_MINUTE_FOR_15_MINUTES);

        assertArrayEquals( //
                ByteUtil.fromHexString("020f00000202"), //
                alertConfiguration3.getRawData());

        ConfigureAlertsCommand configureAlertsCommand = new ConfigureAlertsCommand( //
                0xfeb6268b, //
                Arrays.asList(alertConfiguration1, alertConfiguration2, alertConfiguration3));

        assertArrayEquals( //
                ByteUtil.fromHexString("1916feb6268b79a410df0502280012830602020f00000202"), //
                configureAlertsCommand.getRawData());
    }

    // TODO add tests
}
