package com.gamecockmobile.util;

import android.content.Context;
import android.content.pm.PackageManager;

import com.gamecockmobile.util.LogUtils.*;
public class NetUtils {
  private static String mUserAgent = null;
  private static String APP_NAME = "GamecockMobile";
  
  
  public static String getUserAgent(Context mContext){
    if(mUserAgent == null){
      mUserAgent = APP_NAME;
      try {
        String packageName = mContext.getPackageName();
        String version = mContext.getPackageManager().getPackageInfo(packageName, 0).versionName;
        mUserAgent = mUserAgent + " (" + packageName + "/" + version + ")";
      } catch (PackageManager.NameNotFoundException e){
      }
    }
    return mUserAgent;
  }
}
