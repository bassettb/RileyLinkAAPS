package info.nightscout.androidaps.plugins.pump.omnipod.comm.message.command;

import java.nio.ByteBuffer;

import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.MessageBlock;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.MessageBlockType;

// https://github.com/openaps/openomni/wiki/Command-07-Assign-ID
public class AssignAddressCommand extends MessageBlock {
    private final int address;

    public int getAddress() {
        return address;
    }

    @Override
    public MessageBlockType getType() {
        return MessageBlockType.ASSIGN_ADDRESS;
    }

    public AssignAddressCommand(int address) {
        this.address = address;
        encodedData = ByteBuffer.allocate(4).putInt(this.address).array();
    }

}
