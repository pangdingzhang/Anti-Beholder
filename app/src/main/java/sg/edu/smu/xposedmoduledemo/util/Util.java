package sg.edu.smu.xposedmoduledemo.util;

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
//
//    public static String getLauncher(Context mContext) {
//        Intent intent = new Intent("android.intent.action.MAIN");
//        intent.addCategory("android.intent.category.HOME");
//        return mContext.getPackageManager().resolveActivity(intent, 65536).activityInfo.packageName;
//    }
//
//    public static String getPackageName(String className) {
//        int idx = className.lastIndexOf(46);
//        if (idx != -1) {
//            return className.substring(0, idx);
//        }
//        Log.e(TAG, "className {} did not meet the expected format for a class name"+className);
//        return className;
//    }
//
//    public static ArrayList<String> getUniques(StackTraceElement[] stackTrace) {
//        ArrayList<String> result = new ArrayList<>();
//        String last = null;
//        for (StackTraceElement ele : stackTrace) {
//            if (ele != null) {
//                String curr = ele.getClassName();
//                if (!curr.equals(last)) {
//                    result.add(curr);
//                    last = curr;
//                }
//            }
//        }
//        return result;
//    }
//
//    public static boolean isWhiteListed() {
//        int uid = Process.myUid();
//        return uid == 1001 || uid == 1000;
//    }
//
//    public static boolean isWhiteListed(StackTraceElement[] parentThreadStackTrace) {
//        return false;
//    }
//
//    public static void writeObject(Context context, String key, Object object) throws IOException {
//        FileOutputStream fos = context.openFileOutput(key, 0);
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(object);
//        oos.close();
//        fos.close();
//    }
//
//    public static Object readObject(Context context, String key) throws IOException, ClassNotFoundException {
//        FileInputStream fis = context.openFileInput(key);
//        ObjectInputStream ois = new ObjectInputStream(fis);
//        Object object = ois.readObject();
//        fis.close();
//        ois.close();
//        return object;
//    }
//
//    public static String SHA1(String input) throws NoSuchAlgorithmException {
//        MessageDigest mDigest = MessageDigest.getInstance(CommonUtils.SHA1_INSTANCE);
//        if (mDigest == null) {
//            System.out.println("MDIGEST IS NULL");
//        }
//        byte[] result = mDigest.digest(input.getBytes());
//        StringBuffer sb = new StringBuffer();
//        for (byte b : result) {
//            sb.append(Integer.toString((b & UnsignedBytes.MAX_VALUE) + 256, 16).substring(1));
//        }
//        return sb.toString();
//    }
//
//    public static String getRWK(Context context, String key) {
//        try {
//            return SHA1(Sa + getDeviceInstallID(context) + key);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static String getNewInstallID(Context context) {
//        String installID = null;
//        String deviceID = null;
//        try {
//            String deviceID2 = DeviceUtil.getIMEI(context);
//            String key = getRandomString(15);
//            Log.i(TAG, "hashed device id is and hashed key is " + deviceID2 + "   " + key);
//            try {
//                installID = SHA1(deviceID2 + key);
//            } catch (NoSuchAlgorithmException e) {
//                Log.i(TAG, "Can't SHA " + ((String) null));
//                e.printStackTrace();
//            }
//        } catch (Exception e2) {
//            try {
//                deviceID = DeviceUtil.getDeviceUDID(context);
//                Log.i(TAG, "Couldn't retrieve IMEI");
//            } catch (Exception e3) {
//                Log.i(TAG, "Couldn't get UDID");
//                deviceID = DeviceUtil.getMacAddr(context);
//            } catch (Throwable th) {
//                String key2 = getRandomString(15);
//                Log.i(TAG, "hashed device id is and hashed key is " + deviceID + "   " + key2);
//                try {
//                    SHA1(deviceID + key2);
//                } catch (NoSuchAlgorithmException e4) {
//                    Log.i(TAG, "Can't SHA " + ((String) null));
//                    e4.printStackTrace();
//                }
//                throw th;
//            }
//            String key3 = getRandomString(15);
//            Log.i(TAG, "hashed device id is and hashed key is " + deviceID + "   " + key3);
//            try {
//                installID = SHA1(deviceID + key3);
//            } catch (NoSuchAlgorithmException e5) {
//                Log.i(TAG, "Can't SHA " + ((String) null));
//                e5.printStackTrace();
//            }
//        }
//        if (installID != null && installID.length() != 0) {
//            return installID;
//        }
//        return getRandomString(20) + getRandomString(20);
//    }
//
//    public static String getDeviceInstallID(Context context) {
//        try {
//            return (String) readObject(context, INSTALL_ID_FILE);
//        } catch (IOException | ClassNotFoundException e) {
//            Log.i(TAG, "Expected while setup");
//            return null;
//        }
//    }
//
//    public static List<ApplicationInfo> getInstalledApplications(Context context) {
//        new Intent("android.intent.action.MAIN", (Uri) null).addCategory("android.intent.category.LAUNCHER");
//        if (pm == null) {
//            pm = context.getPackageManager();
//        }
//        return pm.getInstalledApplications(128);
//    }
//
//    public static boolean isAllowedApp(String packageName) {
//        if (packageName != null && !packageName.contains(PMPUtil.getPMPPackageName()) && !packageName.contains(XPOSED_PACKAGE_NAME) && !packageName.contains(SUPER_SU_PACKAGE_NAME)) {
//            return true;
//        }
//        return false;
//    }
//
//    public static boolean isAppInstalled(Context context, String uri) {
//        try {
//            context.getPackageManager().getPackageInfo(uri, 1);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            return false;
//        }
//    }
//
//    public static boolean isSystemApp(Context context, String pkgName) {
//        if (pm == null) {
//            pm = context.getPackageManager();
//        }
//        try {
//            if ((pm.getApplicationInfo(pkgName, 128).flags & 1) == 0 || isPredefinedProtectedApp(pkgName)) {
//                return false;
//            }
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            return false;
//        }
//    }
//
//    public static String getSanitizedProcessUID(String uidName) {
//        if (pat == null) {
//            pat = Pattern.compile(pattern);
//        }
//        try {
//            if (pat.matcher(uidName).find()) {
//                return uidName.substring(0, uidName.indexOf(58));
//            }
//            return uidName;
//        } catch (Exception e) {
//            return uidName;
//        }
//    }
//
//    public static String getRandomString(int length) {
//        return ((UUID.randomUUID().toString() + UUID.randomUUID().toString()) + UUID.randomUUID().toString()).replaceAll(Condition.Operation.MINUS, "").substring(0, length);
//    }
//
//    public static String getTimeZone() {
//        return TimeZone.getDefault().getDisplayName(false, 1, Locale.ENGLISH);
//    }
//
//    public static String getCurrentDay() {
//        return new SimpleDateFormat("EEEE").format(Calendar.getInstance().getTime());
//    }
//
//    public static String[] stackTraceToDisplayString(StackTraceElement[] stacktrace) {
//        String[] stringStackTrace = new String[stacktrace.length];
//        for (int i = 0; i < stacktrace.length; i++) {
//            stringStackTrace[i] = stacktrace[i].toString();
//        }
//        return stringStackTrace;
//    }
//
//    public static String stackTraceToString(String[] stringStackTrace) {
//        String str = Arrays.toString(Arrays.copyOfRange(stringStackTrace, 4, stringStackTrace.length));
//        return str.substring(1, str.length() - 1);
//    }
//
//    public static String stackTraceToString(StackTraceElement[] stacktrace) {
//        String stackTrace = Arrays.toString(Arrays.copyOfRange(stacktrace, 4, stacktrace.length));
//        return stackTrace.substring(1, stackTrace.length() - 1);
//    }
//
//    public static HashMap<String, String> getInstalledAppsFromCache(Context context) {
//        HashMap<String, String> appNameVersion = new HashMap<>();
//        try {
//            try {
//                BufferedReader br = new BufferedReader(new FileReader(new File(context.getFilesDir() + Condition.Operation.DIVISION + "installed_apps")));
//                while (true) {
//                    String line = br.readLine();
//                    if (line == null) {
//                        break;
//                    }
//                    appNameVersion.put(line, br.readLine());
//                }
//                br.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e2) {
//            e2.printStackTrace();
//        }
//        return appNameVersion;
//    }
}
