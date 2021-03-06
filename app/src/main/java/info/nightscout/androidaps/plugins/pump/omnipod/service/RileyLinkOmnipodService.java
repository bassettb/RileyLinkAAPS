package info.nightscout.androidaps.plugins.pump.omnipod.service;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.gxwtech.roundtrip2.MainApp;
import com.gxwtech.roundtrip2.RT2Const;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.nightscout.androidaps.plugins.pump.common.hw.rileylink.RileyLinkCommunicationManager;
import info.nightscout.androidaps.plugins.pump.common.hw.rileylink.RileyLinkConst;
import info.nightscout.androidaps.plugins.pump.common.hw.rileylink.RileyLinkUtil;
import info.nightscout.androidaps.plugins.pump.common.hw.rileylink.ble.RFSpy;
import info.nightscout.androidaps.plugins.pump.common.hw.rileylink.ble.RileyLinkBLE;
import info.nightscout.androidaps.plugins.pump.common.hw.rileylink.ble.defs.RileyLinkEncodingType;
import info.nightscout.androidaps.plugins.pump.common.hw.rileylink.defs.RileyLinkTargetDevice;
import info.nightscout.androidaps.plugins.pump.common.hw.rileylink.service.RileyLinkService;
import info.nightscout.androidaps.plugins.pump.common.hw.rileylink.service.RileyLinkServiceData;
import info.nightscout.androidaps.plugins.pump.common.hw.rileylink.service.data.ServiceTransport;
import info.nightscout.androidaps.plugins.pump.omnipod.OmnipodManager;
import info.nightscout.androidaps.plugins.pump.omnipod.comm.OmnipodCommunicationService;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.state.PodSessionState;
import info.nightscout.androidaps.plugins.pump.omnipod.defs.state.PodState;
import info.nightscout.androidaps.plugins.pump.omnipod.util.OmniPodConst;
import info.nightscout.androidaps.plugins.pump.omnipod.util.Utils;
import info.nightscout.androidaps.utils.SP;

/**
 * Created by andy on 6/1/18.
 */

public class RileyLinkOmnipodService extends RileyLinkService {

    private static final Logger LOG = LoggerFactory.getLogger(RileyLinkOmnipodService.class);

    private OmnipodManager omnipodManager;

    public RileyLinkOmnipodService() {
        super(MainApp.instance().getApplicationContext());
        LOG.debug("RileyLinkOmnipodService newly constructed");
        RileyLinkUtil.setRileyLinkService(this);
    }

    @Override
    public void initRileyLinkServiceData() {
        rileyLinkServiceData = new RileyLinkServiceData(RileyLinkTargetDevice.Omnipod);

        RileyLinkUtil.setRileyLinkServiceData(rileyLinkServiceData);

        // get most recently used RileyLink address
        rileyLinkServiceData.rileylinkAddress = SP.getString(RileyLinkConst.Prefs.RileyLinkAddress, "");
    }

    @Override
    public RileyLinkCommunicationManager getDeviceCommunicationManager() {
        return omnipodManager.getCommunicationService();
    }

    public OmnipodManager getOmnipodManager() {
        return omnipodManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RileyLinkUtil.setRileyLinkService(this);
        rileyLinkBLE = new RileyLinkBLE(context);
        rfspy = new RFSpy(rileyLinkBLE);

        rfspy.startReader();

        RileyLinkUtil.setRileyLinkBLE(rileyLinkBLE);

        PodSessionState podState = null;
        if(SP.contains(OmniPodConst.Prefs.POD_STATE)) {
            try {
                Gson gson = Utils.gsonDateTime();
                String storedPodState = SP.getString(OmniPodConst.Prefs.POD_STATE, null);
                podState = gson.fromJson(storedPodState, PodSessionState.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // TODO obtain and pass saved podstate
        omnipodManager = new OmnipodManager(new OmnipodCommunicationService(rfspy), podState);
    }

    @Override
    public RileyLinkEncodingType getEncoding() {
        return RileyLinkEncodingType.Manchester;
    }


    // TODO
    @Override
    public String getDeviceSpecificBroadcastsIdentifierPrefix() {
        return null;
    }

    // TODO
    @Override
    public boolean handleDeviceSpecificBroadcasts(Intent intent) {
        return false;
    }

    // TODO
    @Override
    public void registerDeviceSpecificBroadcasts(IntentFilter intentFilter) {
    }

    @Override
    public boolean handleIncomingServiceTransport(Intent intent) {
        Bundle bundle = intent.getBundleExtra(RT2Const.IPC.bundleKey);

        ServiceTransport serviceTransport = new ServiceTransport(bundle);

        if (serviceTransport.getServiceCommand().isPumpCommand()) {
            LOG.debug("IsPumpCommand not implemented.");
        } else {
            if ("UseThisRileylink".equals(serviceTransport.getOriginalCommandName())) {// If we are not connected, connect using the given address.
                // If we are connected and the addresses differ, disconnect, connect to new.
                // If we are connected and the addresses are the same, ignore.
                String deviceAddress = serviceTransport.getServiceCommand().getMap().getString("rlAddress", "");
                if ("".equals(deviceAddress)) {
                    LOG.error("handleIPCMessage: null RL address passed");
                } else {
                    return reconfigureRileyLink(deviceAddress);
                }
            } else {
                LOG.error("handleIncomingServiceTransport: Failed to handle service command '" + serviceTransport.getOriginalCommandName() + "'");
            }
        }
        return false;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return RileyLinkUtil.getRileyLinkIPCConnection().doOnBind(intent);
    }

    public class LocalBinder extends Binder {
        public RileyLinkOmnipodService getServiceInstance() {
            return RileyLinkOmnipodService.this;
        }
    }

}
