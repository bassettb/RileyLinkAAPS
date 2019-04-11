package info.nightscout.androidaps.plugins.PumpCommon.hw.rileylink.ble;

import android.os.AsyncTask;
import android.os.SystemClock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import info.nightscout.androidaps.plugins.PumpCommon.hw.rileylink.RileyLinkUtil;
import info.nightscout.androidaps.plugins.PumpCommon.hw.rileylink.ble.data.GattAttributes;
import info.nightscout.androidaps.plugins.PumpCommon.hw.rileylink.ble.defs.RileyLinkEncodingType;
import info.nightscout.androidaps.plugins.PumpCommon.hw.rileylink.ble.defs.command.SetSoftwareEncoding;
import info.nightscout.androidaps.plugins.PumpCommon.hw.rileylink.ble.operations.BLECommOperationResult;
import info.nightscout.androidaps.plugins.PumpCommon.utils.ByteUtil;
import info.nightscout.androidaps.plugins.PumpCommon.utils.ThreadUtil;

/**
 * Created by geoff on 5/26/16.
 */
public class RFSpyReader {

    private static final Logger LOG = LoggerFactory.getLogger(RFSpyReader.class);

    //private Context context;
    private IRileyLinkBLE rileyLinkBle;
    private Semaphore waitForRadioData = new Semaphore(0, true);
    AsyncTask<Void, Void, Void> readerTask;
    private LinkedBlockingQueue<byte[]> mDataQueue = new LinkedBlockingQueue<>();
    private int acquireCount = 0;
    private int releaseCount = 0;


    public RFSpyReader(/*Context context,*/ IRileyLinkBLE rileyLinkBle) {
        //this.context = context;
        this.rileyLinkBle = rileyLinkBle;
    }


    public void init(/*Context context,*/ RileyLinkBLE rileyLinkBLE) {
        //this.context = context;
        this.rileyLinkBle = rileyLinkBLE;
    }


    public void setRileyLinkBle(RileyLinkBLE rileyLinkBle) {
        if (readerTask != null) {
            readerTask.cancel(true);
        }
        this.rileyLinkBle = rileyLinkBle;
    }


    // This timeout must be coordinated with the length of the RFSpy radio operation or Bad Things Happen.
    public byte[] poll(int timeout_ms) {
        LOG.trace(ThreadUtil.sig() + "Entering poll at t==" + SystemClock.uptimeMillis() + ", timeout is " + timeout_ms + " mDataQueue size is " + mDataQueue.size());
        if (mDataQueue.isEmpty())
            try {
                // block until timeout or data available.
                // returns null if timeout.
                byte[] dataFromQueue = mDataQueue.poll(timeout_ms, TimeUnit.MILLISECONDS);
                if (dataFromQueue != null) {
                    LOG.debug("Got data [" + ByteUtil.shortHexString(dataFromQueue) + "] at t==" + SystemClock.uptimeMillis());
                } else {
                    LOG.debug("Got data [null] at t==" + SystemClock.uptimeMillis());
                }
                return dataFromQueue;
            } catch (InterruptedException e) {
                LOG.error("poll: Interrupted waiting for data");
            }
        return null;
    }


    // Call this from the "response count" notification handler.
    public void newDataIsAvailable() {
        releaseCount++;

        LOG.trace(ThreadUtil.sig() + "waitForRadioData released(count=" + releaseCount + ") at t=" + SystemClock.uptimeMillis());
        waitForRadioData.release();
    }


    public void start() {
        readerTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                UUID serviceUUID = UUID.fromString(GattAttributes.SERVICE_RADIO);
                UUID radioDataUUID = UUID.fromString(GattAttributes.CHARA_RADIO_DATA);
                BLECommOperationResult result;
                boolean stopAtNull = true;
                if (RileyLinkUtil.getEncoding() == RileyLinkEncodingType.Manchester)
                    stopAtNull = false;
                while (true) {
                    try {
                        acquireCount++;
                        waitForRadioData.acquire();
                        LOG.trace(ThreadUtil.sig() + "waitForRadioData acquired (count=" + acquireCount + ") at t=" + SystemClock.uptimeMillis());
                        SystemClock.sleep(100);
                        SystemClock.sleep(1);
                        result = rileyLinkBle.readCharacteristic_blocking(serviceUUID, radioDataUUID);
                        SystemClock.sleep(100);

                        if (result.resultCode == BLECommOperationResult.RESULT_SUCCESS) {
                            if (stopAtNull) {
                                // only data up to the first null is valid
                                for (int i = 0; i < result.value.length; i++) {
                                    if (result.value[i] == 0) {
                                        result.value = ByteUtil.substring(result.value, 0, i);
                                        break;
                                    }
                                }
                            }
                            mDataQueue.add(result.value);
                        } else if (result.resultCode == BLECommOperationResult.RESULT_INTERRUPTED) {
                            LOG.error("Read operation was interrupted");
                        } else if (result.resultCode == BLECommOperationResult.RESULT_TIMEOUT) {
                            LOG.error("Read operation on Radio Data timed out");
                        } else if (result.resultCode == BLECommOperationResult.RESULT_BUSY) {
                            LOG.error("FAIL: RileyLinkBLE reports operation already in progress");
                        } else if (result.resultCode == BLECommOperationResult.RESULT_NONE) {
                            LOG.error("FAIL: got invalid result code: " + result.resultCode);
                        }
                    } catch (InterruptedException e) {
                        LOG.error("Interrupted while waiting for data");
                    }
                }
            }
        }.execute();
    }

}
