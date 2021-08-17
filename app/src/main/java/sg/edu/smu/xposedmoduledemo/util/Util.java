package sg.edu.smu.xposedmoduledemo.util;

import android.app.ActivityManager;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import java.util.List;
import java.util.Random;

public class Util {
    public static double fakeLat = 1.3535d;
    public static double fakeLng = 103.9879d;
    private final static String TAG = "Util";




    public static String getShuffled(String in) {
        if (in == null) {
            Log.d(TAG, "getString is null");
            return null;
        }
        try {
            Log.d(TAG, "getString is shuffled");
            return shuffle(in);
        } catch (NullPointerException e) {
            return shuffle("");
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
