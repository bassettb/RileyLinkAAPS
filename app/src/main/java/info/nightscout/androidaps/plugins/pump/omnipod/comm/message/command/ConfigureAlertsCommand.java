package info.nightscout.androidaps.plugins.pump.omnipod.comm.message.command;

import java.util.List;

import info.nightscout.androidaps.plugins.pump.common.utils.ByteUtil;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.AlertConfiguration;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.MessageBlock;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.MessageBlockType;

public class ConfigureAlertsCommand extends MessageBlock {
    private final int nonce;
    private final List<AlertConfiguration> configurations;

    @Override
    public MessageBlockType getType() {
        return MessageBlockType.CONFIGURE_ALERTS;
    }

    public ConfigureAlertsCommand(int nonce, List<AlertConfiguration> configurations) {
        this.nonce = nonce;
        this.configurations = configurations;
        encode();
    }

    private void encode() {
        encodedData = ByteUtil.getBytesFromInt(nonce);
        for (AlertConfiguration config : configurations) {
            encodedData = ByteUtil.concat(encodedData, config.getRawData());
        }
    }
}
