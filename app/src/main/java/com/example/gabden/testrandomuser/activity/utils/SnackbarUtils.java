package com.example.gabden.testrandomuser.activity.utils;

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.gabden.testrandomuser.R;

public final class SnackbarUtils {

    private SnackbarUtils() {}

    private static Snackbar createSnackbar(@NonNull Activity activity, @StringRes int text) {
        return Snackbar.make(activity.findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT);
    }

    private static Snackbar createSnackbar(@NonNull View view, @StringRes int text) {
        return Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
    }

    public static void showPlainSnackbar(@NonNull Activity activity, @StringRes int text) {
        createSnackbar(activity, text).show();
    }

    public static void showPlainSnackbar(@NonNull View view, @StringRes int text) {
        createSnackbar(view, text).show();
    }

    public static void showStyledSnackbar(@NonNull Activity activity, @StringRes int text, @ColorRes int textColor, @ColorRes int backgroundColor) {
        Snackbar snackbar = createSnackbar(activity, text);
        snackbar.setActionTextColor(ContextCompat.getColor(activity, textColor));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(activity, backgroundColor));
        snackbar.show();
    }

    public static void showErrorSnackbar(@NonNull Activity activity, @StringRes int text) {
        showStyledSnackbar(activity, text, R.color.white, R.color.red);
    }
}
