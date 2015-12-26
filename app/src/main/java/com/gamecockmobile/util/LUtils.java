package com.gamecockmobile.util;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by piedt on 12/25/15.
 */
public class LUtils {
    protected AppCompatActivity mActivity;


    private LUtils (AppCompatActivity activity) {
        mActivity = activity;
    }

    public static LUtils getInstance(AppCompatActivity activity) {
        return new LUtils(activity);
    }

    private static boolean hasL() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
