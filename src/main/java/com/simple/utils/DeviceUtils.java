package com.simple.utils;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.simple.utils.MD5Encript;

public final class DeviceUtils {
    private DeviceUtils() {}

    private static final String TAG = DeviceUtils.class.getSimpleName();

    protected static final String UNKNOW = "Unknown";
    private static final String MOBILE_NETWORK = "2G/3G";
    private static final String WIFI = "Wi-Fi";

    /**
     * 获取设备的dpi
     * @return 返回设备dpi
     */
    public static int getDeviceDpi(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics() ;
        return (int)( metrics.density * 160f ) ;
    }

    /**
     *
     * @param context 上下文
     * @return 返回应用版本名
     */
    public static String getAppVersonName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1.0";
    }

    /**
     * 获取版本号 Code
     * @param context 上下文
     * @return 版本号 Code
     */
    public static String getAppVersonCode(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(
                    context.getPackageName(), 0);
            return "" + info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 判断当前应用是否具有指定的权限
     *
     * @param context
     * @param permission 权限信息的完整名称 如：<code>android.permission.INTERNET</code>
     * @return 当前仅当宿主应用含有 参数 permission 对应的权限 返回true 否则返回 false
     */
    public static boolean checkPermission(Context context, String permission) {
        if (context == null) {
            return false;
        }
        PackageManager pm = context.getPackageManager();
        if (pm.checkPermission(permission, context.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取设备唯一标识,为imei、mac与android id的MD5值。
     *
     * </p> 注意：需要权限
     * android.permission.READ_PHONE_STATE
     *
     * @param context
     * @return 返回设备唯一标识符号
     */
    public static String getUniqueDeviceId(Context context) {
        String imei = "";
        String mac = "";
        String androidId = "";
        try {
            if (checkPermission(context, permission.READ_PHONE_STATE)) {
                imei = getDeviceId(context);
            }
        } catch (Exception ex) {
            Log.w(TAG, "No IMEI.", ex);
        }
        mac = getMac(context);
        androidId = getAndroidId(context);
        // 取MD5值
        return MD5Encript.toMD5(imei + mac + androidId);
    }

    /**
     * 获取设备id
     *
     * @param context
     * @return
     */
    private static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取Android id
     * @param context
     * @return
     */
    private static String getAndroidId(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    /**
     * check google play service is avaliable
     *
     * @param context Context
     * @return
     */
    public static boolean isGooglePlayServiceAvaliable(Context context) {
        boolean isInstall = false;
        try {
            Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
            isInstall = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isInstall && getGooglePlayServiceVersion(context) >= 4;
    }

    private static int getGooglePlayServiceVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    "com.google.android.gms", 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get the mobile network access mode.
     *
     * @param context
     * @return A 2-elements String array, 1st specifies the network type, the
     * 2nd specifies the network subtype. If the network cannot be
     * retrieved, "Unknown" is filled instead.
     */
    public static String[] getNetworkAccessMode(Context context) {
        String[] res = new String[]{DeviceUtils.UNKNOW, DeviceUtils.UNKNOW};
        PackageManager pm = context.getPackageManager();
        if (pm.checkPermission(permission.ACCESS_NETWORK_STATE,
                context.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
            res[0] = DeviceUtils.UNKNOW;
            return res;
        }

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            res[0] = DeviceUtils.UNKNOW;
            return res;
        } else {
            NetworkInfo wifi_network = connectivity
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifi_network.getState() == NetworkInfo.State.CONNECTED) {
                res[0] = DeviceUtils.WIFI;
                return res;
            }
            NetworkInfo mobile_network = connectivity
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobile_network != null
                    && mobile_network.getState() == NetworkInfo.State.CONNECTED) {
                res[0] = DeviceUtils.MOBILE_NETWORK;
                res[1] = mobile_network.getSubtypeName();
                return res;
            }
        }
        return res;
    }

    /**
     * whether wifi is avaliable
     * @param context
     * @return
     */
    public static boolean isWiFiAvailable(Context context) {
        return WIFI.equals(DeviceUtils.getNetworkAccessMode(context)[0]);
    }

    /**
     * <p>
     * True if the device is connected or connection to network.
     * </p>
     * 需要权限: <code>android.permission.ACCESS_NETWORK_STATE</code> </p>
     *
     * @param context
     * @return 如果当前有网络连接返回 true 如果网络状态访问权限或没网络连接返回false
     */
    private static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null) {
                return ni.isConnectedOrConnecting();
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * <p/>
     *
     * @param context
     * @throws
     * @Title: isNetworkAvailable
     * </p>
     * @link (isOnline, checkPermission)
     * @see
     */
    public static boolean isNetworkAvailable(Context context) {
        // 检测网络状况
        if (DeviceUtils.checkPermission(context,
                permission.ACCESS_NETWORK_STATE)
                && DeviceUtils.isOnline(context)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断Sd Card 是否可读可写
     *
     * @return 当且仅当Sdcard既可读又可写返回 true 否则返回false
     */
    public static boolean isSdCardWrittenable() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 读取手机MAC地址
     *
     * @param context
     * @return 返回mac地址
     */
    public static String getMac(Context context) {
        try {
            WifiManager wifi = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(context, "android.permission.ACCESS_WIFI_STATE")) {
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            } else {
                Log.w(TAG,
                        "Could not get mac address.[no permission android.permission.ACCESS_WIFI_STATE");
            }
        } catch (Exception e) {
            Log.w(TAG, "Could not get mac address." + e.toString());
        }
        return "";
    }

    /**
     * 获取手机型号
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL ;
    }

    /**
     * get meta data which declare in AndroidManifest
     * @param context
     * @param name
     * @return
     */
    public static String getMetaData(Context context, String name) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            return ai.metaData.getString(name);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        return "";
    }
}
