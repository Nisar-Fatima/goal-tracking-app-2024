package com.techtide.goaltracking.util;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesUtil {
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    private static Preferences prefs = Preferences.userRoot().node(PreferencesUtil.class.getName());

    public static void saveLoginDetails(String username, String password) {
        prefs.put(USERNAME_KEY, username);
        prefs.put(PASSWORD_KEY , password);
    }

    public static void clearSavedLoginDetails() throws BackingStoreException {
        prefs.clear();
    }

    public static String getUsername() {

        return prefs.get(USERNAME_KEY, "");
    }

    public static String getPassword() {
        return prefs.get(PASSWORD_KEY , "");
    }
}



