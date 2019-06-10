package info.nightscout.androidaps.plugins.pump.omnipod.comm.message;

import org.apache.commons.lang3.NotImplementedException;

import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.response.PodInfoResponse;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.response.VersionResponse;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.response.ErrorResponse;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.message.response.StatusResponse;


public enum MessageBlockType {
    VERSION_RESPONSE(0x01),
    POD_INFO_RESPONSE(0x02),
    SETUP_POD(0x03),
    ERROR_RESPONSE(0x06),
    ASSIGN_ADDRESS(0x07),
    FAULT_CONFIG(0x08),
    GET_STATUS(0x0e),
    ACKNOWLEDGE_ALERT(0x11),
    BASAL_SCHEDULE_EXTRA(0x13),
    TEMP_BASAL_EXTRA(0x16),
    BOLUS_EXTRA(0x17),
    CONFIGURE_ALERTS(0x19),
    SET_INSULIN_SCHEDULE(0x1a),
    DEACTIVATE_POD(0x1c),
    STATUS_RESPONSE(0x1d),
    BEEP_CONFIG(0x1e),
    CANCEL_DELIVERY(0x1f);

    byte value;

    MessageBlockType(int value) {
        this.value = (byte)value;
    }

    public byte getValue() {
        return value;
    }

    public static MessageBlockType fromByte(byte input) {
        for (MessageBlockType type : values()) {
            if (type.value == input) {
                return type;
            }
        }
        return null;
    }

    public MessageBlock decode(byte[] encodedData) {
        switch (this) {
            case VERSION_RESPONSE:
                return new VersionResponse(encodedData);
            case ERROR_RESPONSE:
                return new ErrorResponse(encodedData);
            case POD_INFO_RESPONSE:
                return new PodInfoResponse(encodedData);
            case STATUS_RESPONSE:
                return new StatusResponse(encodedData);
//            case SETUP_POD:
//                break;
//            case ASSIGN_ADDRESS:
//                break;
//            case GET_STATUS:
//                break;
//            case BASAL_SCHEDULE_EXTRA:
//                break;
//            case TEMP_BASAL_EXTRA:
//                break;
//            case BOLUS_EXTRA:
//                break;
//            case CONFIGURE_ALERTS:
//                break;
//            case SET_INSULIN_SCHEDULE:
//                break;
//            case DEACTIVATE_POD:
//                break;
//            case CANCEL_DELIVERY:
//                break;
            default:
                throw new NotImplementedException(this.name());
        }
    }
}
