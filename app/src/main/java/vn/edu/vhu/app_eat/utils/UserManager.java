package vn.edu.vhu.app_eat.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {

    private static final String PREF_NAME = "appeat_user_pref";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_PHONE = "user_phone";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_ADDRESS = "user_address";
    private static final String KEY_AVATAR = "user_avatar";
    private static final String KEY_POINTS = "user_points";
    private static final String KEY_LAST_CHECKIN_DATE = "last_checkin_date";
    private static final String KEY_CHECKIN_STREAK = "checkin_streak";
    private static final String KEY_PIN_X = "user_pin_x";
    private static final String KEY_PIN_Y = "user_pin_y";

    public static final String DEFAULT_NAME = "Nguyễn Văn A";
    public static final String DEFAULT_PHONE = "0901 234 567";
    public static final String DEFAULT_EMAIL = "vana@gmail.com";
    public static final String DEFAULT_ADDRESS = "123 Nguyễn Huệ, Quận 1, TP.HCM";
    public static final String DEFAULT_AVATAR = "avt_1";
    public static final int DEFAULT_POINTS = 450;
    public static final float DEFAULT_PIN_X = 0.78f;
    public static final float DEFAULT_PIN_Y = 0.82f;

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getUserName(Context context) {
        return getPrefs(context).getString(KEY_NAME, DEFAULT_NAME);
    }

    public static String getUserPhone(Context context) {
        return getPrefs(context).getString(KEY_PHONE, DEFAULT_PHONE);
    }

    public static String getUserEmail(Context context) {
        return getPrefs(context).getString(KEY_EMAIL, DEFAULT_EMAIL);
    }

    public static String getUserAddress(Context context) {
        return getPrefs(context).getString(KEY_ADDRESS, DEFAULT_ADDRESS);
    }

    public static float getUserPinX(Context context) {
        return getPrefs(context).getFloat(KEY_PIN_X, DEFAULT_PIN_X);
    }

    public static float getUserPinY(Context context) {
        return getPrefs(context).getFloat(KEY_PIN_Y, DEFAULT_PIN_Y);
    }

    public static void setUserAddress(Context context, String address, float pinX, float pinY) {
        getPrefs(context).edit()
                .putString(KEY_ADDRESS, address)
                .putFloat(KEY_PIN_X, pinX)
                .putFloat(KEY_PIN_Y, pinY)
                .apply();
    }

    public static String getUserAvatar(Context context) {
        return getPrefs(context).getString(KEY_AVATAR, DEFAULT_AVATAR);
    }

    public static int getUserPoints(Context context) {
        return getPrefs(context).getInt(KEY_POINTS, DEFAULT_POINTS);
    }

    public static void saveUserProfile(Context context, String name, String phone, String email, String address, String avatar) {
        getPrefs(context).edit()
                .putString(KEY_NAME, name)
                .putString(KEY_PHONE, phone)
                .putString(KEY_EMAIL, email)
                .putString(KEY_ADDRESS, address)
                .putString(KEY_AVATAR, avatar)
                .apply();
    }

    public static void addPoints(Context context, int pointsToAdd) {
        int current = getUserPoints(context);
        getPrefs(context).edit().putInt(KEY_POINTS, current + pointsToAdd).apply();
    }

    public static boolean redeemPoints(Context context, int pointsToSpend) {
        int current = getUserPoints(context);
        if (current >= pointsToSpend) {
            getPrefs(context).edit().putInt(KEY_POINTS, current - pointsToSpend).apply();
            return true;
        }
        return false;
    }

    public static String getRankTitle(int points) {
        if (points >= 2000) return "KIM CƯƠNG (DIAMOND)";
        if (points >= 1000) return "VÀNG (GOLD)";
        if (points >= 300) return "BẠC (SILVER)";
        return "ĐỒNG (BRONZE)";
    }

    public static int getCheckinStreak(Context context) {
        return getPrefs(context).getInt(KEY_CHECKIN_STREAK, 2); // Default 2 days streak for demonstration
    }

    public static boolean hasCheckedInToday(Context context) {
        String today = getTodayDateString();
        String lastCheckin = getPrefs(context).getString(KEY_LAST_CHECKIN_DATE, "");
        return today.equals(lastCheckin);
    }

    public static int getStreakReward(int dayIndex) {
        switch (dayIndex) {
            case 1: return 10;
            case 2: return 20;
            case 3: return 30;
            case 4: return 40;
            case 5: return 50;
            case 6: return 60;
            case 7: return 100;
            default: return 30;
        }
    }

    public static int claimDailyReward(Context context) {
        if (hasCheckedInToday(context)) {
            return 0;
        }

        int currentStreak = getCheckinStreak(context);
        int newStreak = (currentStreak % 7) + 1;
        int rewardPoints = getStreakReward(newStreak);

        addPoints(context, rewardPoints);
        getPrefs(context).edit()
                .putString(KEY_LAST_CHECKIN_DATE, getTodayDateString())
                .putInt(KEY_CHECKIN_STREAK, newStreak)
                .apply();

        return rewardPoints;
    }

    private static String getTodayDateString() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
}
