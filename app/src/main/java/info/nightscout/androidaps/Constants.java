package info.nightscout.androidaps;


import org.joda.time.Duration;

/**
 * Created by mike on 07.06.2016.
 */
public class Constants {
    public static final double POD_PULSE_SIZE = 0.05;
    public static final double MAX_RESERVOIR_READING = 50.0;
    public static final double MAX_BOLUS = 30.0;
    public static final double MAX_BASAL_RATE = 30.0;
    public static final Duration MAX_TEMP_BASAL_DURATION = Duration.standardHours(12);
    public static final int DEFAULT_ADDRESS = 0xffffffff;

    public static final Duration SERVICE_DURATION = Duration.standardHours(80);
    public static final Duration EXPIRATION_ALERT_WINDOW = Duration.standardHours(2);
    public static final Duration EXPIRATION_ADVISORY_WINDOW = Duration.standardHours(2);
    public static final Duration END_OF_SERVICE_IMMINENT_WINDOW = Duration.standardHours(1);

    public static final Duration POD_PRIME_DURATION = Duration.standardSeconds(55);
    public static final Duration POD_CANNULA_INSERTION_DURATION = Duration.standardSeconds(10);

//    public static final String MGDL = "mg/dl"; // This is Nightscout's representation
//    public static final String MMOL = "mmol";
//
//    public static final double MMOLL_TO_MGDL = 18; // 18.0182;
//    public static final double MGDL_TO_MMOLL = 1 / MMOLL_TO_MGDL;
//
//    public static final double defaultDIA = 3d;
//
//    public static final Double REALLYHIGHBASALRATE = 1111111d;
//    public static final Integer REALLYHIGHPERCENTBASALRATE = 1111111;
//    public static final double REALLYHIGHBOLUS = 1111111d;
//    public static final Integer REALLYHIGHCARBS = 1111111;
//    public static final double REALLYHIGHIOB = 1111111d;
//
//    public static final Integer notificationID = 556677;
//
//    public static final int hoursToKeepInDatabase = 72;
//    public static final int daysToKeepHistoryInDatabase = 30;
//
//    public static final long keepAliveMsecs = 5 * 60 * 1000L;
//
//    // SMS COMMUNICATOR
//    public static final long remoteBolusMinDistance = 15 * 60 * 1000L;
//
//    // Circadian Percentage Profile
//    public static final int CPP_MIN_PERCENTAGE = 50;
//    public static final int CPP_MAX_PERCENTAGE = 200;
//    public static final int CPP_MIN_TIMESHIFT = -6;
//    public static final int CPP_MAX_TIMESHIFT = 23;
//
//    //DanaR
//    public static final double dailyLimitWarning = 0.95d;
//
//    // Temp targets
//    public static final int defaultActivityTTDuration = 90; // min
//    public static final double defaultActivityTTmgdl = 140d;
//    public static final double defaultActivityTTmmol = 8d;
//    public static final int defaultEatingSoonTTDuration = 45; // min
//    public static final double defaultEatingSoonTTmgdl = 90d;
//    public static final double defaultEatingSoonTTmmol = 5d;
//    public static final int defaultHypoTTDuration = 30; // min
//    public static final double defaultHypoTTmgdl = 120d;
//    public static final double defaultHypoTTmmol = 6.5d;
//
//    //NSClientInternal
//    public static final int MAX_LOG_LINES = 100;
//
//    //Screen: Threshold for width/height to go into small width/height layout
//    public static final int SMALL_WIDTH = 320;
//    public static final int SMALL_HEIGHT = 480;
//
//    //Autosens
//    public static final double DEVIATION_TO_BE_EQUAL = 2.0;
//    public static final double DEFAULT_MAX_ABSORPTION_TIME = 6.0;
//
//    // Pump
//    public static final int PUMP_MAX_CONNECTION_TIME_IN_SECONDS = 120 - 1;
//    public static final int MIN_WATCHDOG_INTERVAL_IN_SECONDS = 12 * 60;
//
//    //SMS Communicator
//    //public static final long SMS_CONFIRM_TIMEOUT = T.mins(5).msecs();
}
