package com.parttime.enterprise.prefences;


import android.content.Context;
import android.content.SharedPreferences;


/* this prefrence use the enum for dynamic and standard use
  prefence enum type
 * */

public enum AppPrefence {
    INSTANCE;

    private static final String SHARED_PREFERENCE_NAME = "parttimeappenterprise";
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public boolean isFirstTimeLaunch() {
        return mPreferences.getBoolean(SharedPreferncesKeys.IS_FIRST_TIME_LAUNCH.toString(), true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        mEditor.putBoolean(SharedPreferncesKeys.IS_FIRST_TIME_LAUNCH.toString(), isFirstTime);
        mEditor.commit();
    }

    /**
     * private constructor for singleton class
     *
     * @param context
     */
    public void initAppPreferences(Context context) {
        mPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    /* get app version
     * */
    public int getAppVersion() {
        return mPreferences.getInt(SharedPreferncesKeys.appVersion.toString(), 0);
    }


    /*
     * set App version
     * */
    public void setAppVersion(int value) {
        mEditor.putInt(SharedPreferncesKeys.appVersion.toString(), value);
        mEditor.commit();
    }

    public void setString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }


    public void setLong(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public void setInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void setBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public String getString(String key) {
        return mPreferences.getString(key, null);
    }


    public String getString(String key, String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }

    public int getInt(String key) {
        return mPreferences.getInt(key, Integer.MIN_VALUE);
    }

    public int getInt(String key, int defaultValue) {
        return mPreferences.getInt(key, defaultValue);
    }

    public long getLong(String key) {
        return mPreferences.getLong(key, Long.MIN_VALUE);
    }

    public long getLong(String key, long defaultValue) {
        return mPreferences.getLong(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Used to clear all the values stored in preferences
     *
     * @return void
     */
    public void clearPreferences() {
        mEditor.clear();
        mEditor.commit();
    }


    /**
     * Enum for shared preferences keys to store various values
     *
     * @author vikash
     */
    public enum SharedPreferncesKeys {
        appVersion, IS_FIRST_TIME_LAUNCH, name,
        email,
        apiToken,
        enterpriseId,
        socialProvider,
        deviceToken,
        deviceType,
        LANGUAGE_KEY,
        LANGUAGE,
        LANGUAGE_SCREEN,
        FILTER,
        hourlyRateSetting,
        enrollWorkerSetting,
        notificationONOFF,
        USER_ID,
        ENTERPRISE_ADMIN,
        SUPERVISIOR,
        SCHEDULER,
        SUPER_EVALUATOR,
        RECRUITER,
        ACCOUNT_TYPE
    }
}