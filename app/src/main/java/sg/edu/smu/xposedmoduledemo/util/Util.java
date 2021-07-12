package sg.edu.smu.xposedmoduledemo.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Process;
import android.util.Log;

//import com.google.common.primitives.UnsignedBytes;
//import com.raizlabs.android.dbflow.sql.language.Condition;
//import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.synergylabs.pmpandroid.firewall.InterfaceTracker;

public class Util {
    public static final int DEFAULT = -1;
    public static final String DEVICE_CONFIG_FILE = "deviceConfig.txt";
    public static final String EMOJI_CHEER = "🙌";
    public static final String EMOJI_SAD = "😢";
    public static final int FAKE = 3;
    public static final String GROUP_DEFAULTS_FILE = "groupDefaults.txt";
    public static final String GROUP_ORDER_FILE = "groupOrder.txt";
    public static final String INSTALL_ID_FILE = "installID.txt";
    public static final String MIN_VERSION_FILE = "minVersion.txt";
    public static final int NO = 106;
    public static final int OFF = 1;
    public static final int ON = 0;
    public static final int PROTECTED_PAGE_NUM = 0;
    public static final String SELECTION_HISTORY_FILE = "SelectionHistory.txt";
    public static final String SUPER_SU_PACKAGE_NAME = "eu.chainfire.supersu";
    public static final int SYSTEM_APPS_PAGE_NUM = 1;
    public static final String Sa = "!CrxSPn2R!fKU89A";
    public static final int THIRD_PARTY_LIBRARY_PAGE_NUM = 2;
    public static final int UNKNOWN = 2;
    public static final String XPOSED_PACKAGE_NAME = "de.robv.android.xposed.installer";
    public static final int YES = 105;
    public static double fakeLat = 1.3535d;
    public static double fakeLng = 103.9879d;
    private static String pattern = ":[\\d]{1,6}";
    private static Pattern pat = Pattern.compile(pattern);
    private static PackageManager pm;
    private static HashSet<String> preDefinedProtectedApps = new HashSet<>();
    private static String[] predefinedProtectedAppsList = {"com.google.android.apps.photos", "com.google.android.apps.books", "com.google.android.GoogleCamera", "com.google.android.youtube", "com.android.chrome", "com.google.android.music", "com.google.android.keep", "com.google.android.gm", "com.google.android.apps.messaging", "com.google.android.calendar", "com.google.android.videos", "com.google.android.talk", "com.google.android.apps.docs", "com.google.android.apps.plus", "com.google.android.apps.fitness", "com.google.android.apps.magazines", "com.google.android.apps.docs.editors.sheets", "com.google.android.apps.docs.editors.slides", "com.google.android.googlequicksearchbox", "com.google.android.launcher"};
    private final static String TAG = "Util";

    static {
        Collections.addAll(preDefinedProtectedApps, predefinedProtectedAppsList);
    }

    public static boolean isPredefinedProtectedApp(String packageName) {
        return preDefinedProtectedApps.contains(packageName);
    }

    public static String stackTrace(Throwable e) {
        Writer result = new StringWriter();
        e.printStackTrace(new PrintWriter(result));
        return result.toString();
    }

    public static String appName(String packageName, Context context) {
        ApplicationInfo ai;
        if (pm == null) {
            pm = context.getPackageManager();
        }
        try {
            ai = pm.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            ai = null;
        }
        if (ai == null) {
            return "(AppNameNotFound)";
        }
        return (String) pm.getApplicationLabel(ai);
    }

    public static String appVersion(String packageName, Context context) {
        if (pm == null) {
            pm = context.getPackageManager();
        }
        try {
            return pm.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "(PackageVersionNotFound)";
        }
    }

//    public static List<String> interfaceInfo(boolean showMatches) {
//        List<String> ret = new ArrayList<>();
//        try {
//            for (File f : new File("/sys/class/net").listFiles()) {
//                String name = f.getName();
//                if (!showMatches) {
//                    ret.add(name);
//                } else if (matchName(InterfaceTracker.ITFS_WIFI, name) != null) {
//                    ret.add(name + ": wifi");
//                } else if (matchName(InterfaceTracker.ITFS_3G, name) != null) {
//                    ret.add(name + ": 3G");
//                } else if (matchName(InterfaceTracker.ITFS_VPN, name) != null) {
//                    ret.add(name + ": VPN");
//                } else {
//                    ret.add(name + ": unknown");
//                }
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "can't list network interfaces: " + e.getLocalizedMessage());
//        }
//        return ret;
//    }

    public static String matchName(String[] patterns, String name) {
        for (String p : patterns) {
            int minLen = Math.min(p.length(), name.length());
            int i = 0;
            while (true) {
                if (i == minLen) {
                    if (name.length() == p.length()) {
                        return p;
                    }
                } else if (name.charAt(i) == p.charAt(i)) {
                    i++;
                } else if (p.charAt(i) == '+') {
                    return p;
                }
            }
        }
        return null;
    }

    public static String getShuffledMacAddress(String in) {
        String result = Integer.toHexString(in.hashCode());
        String mac = "";
        for (int i = 0; i < 6; i++) {
            String mac2 = (i * 2) + 1 >= result.length() ? mac + "00" : mac + result.substring(i * 2, (i * 2) + 2);
            if (i == 5) {
                return mac2;
            }
            mac = mac2 + ":";
        }
        return mac;
    }

    public static String getShuffled(String in) {
        if (in == null) {
            return null;
        }
        try {
            return shuffle(in);
        } catch (NullPointerException e) {
            return shuffle("PmPdeviceID");
        }
    }

    private static String shuffle(String in) {
        Random rand = new Random();
        rand.setSeed((long) in.hashCode());
        char[] result = new char[in.length()];
        for (int i = 0; i < in.length(); i++) {
            if (Character.isDigit(in.charAt(i))) {
                result[i] = (char) (rand.nextInt(10) + 48);
            } else if (Character.isLetter(in.charAt(i))) {
                result[i] = (char) (rand.nextInt(26) + 97);
            } else {
                result[i] = in.charAt(i);
            }
        }
        return new String(result);
    }

    public static int getShuffled(Integer int1) {
        return int1.hashCode();
    }

    public static short getShuffled(Short int1) {
        return (short) int1.hashCode();
    }

    public static long getShuffled(Long int1) {
        return (long) int1.hashCode();
    }

    public static double getShuffled(Double d) {
        return (double) d.hashCode();
    }

    public static void modifyLocation(Location l) {
        Log.i(TAG, "Modifying location method has been called. The new coordinates are ({},{})"+Double.valueOf(getFakeLat()) + Double.valueOf(getFakeLng()));
        l.setLatitude(getFakeLat());
        l.setLongitude(getFakeLng());
    }

    public static double getFakeLat() {
        return fakeLat;
    }

    public static double getFakeLng() {
        return fakeLng;
    }

    public static Date getFakeDate() {
        return new GregorianCalendar(1, 1, 2000).getTime();
    }

    public static String getReadablePermission(int permission) {
        if (permission == 0) {
            return "Allow";
        }
        if (permission == 1) {
            return "Deny";
        }
        if (permission == 3) {
            return "Fake";
        }
        if (permission == 2) {
            return "Ask";
        }
        return "Default Value";
    }

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
