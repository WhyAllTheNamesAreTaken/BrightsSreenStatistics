package com.blackheart.brightscreenstatistics.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class GetAppInfoUtil {
  public static String getApplicationNameByPackageName(Context context, String packageName) {

    PackageManager pm = context.getPackageManager();
    String Name;
    try {
      Name =
          pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA))
              .toString();
    } catch (PackageManager.NameNotFoundException e) {
      Name = "";
    }
    return Name;
  }

  public static Drawable getApplicationIconByPackageName(Context context, String packageName) {

    PackageManager pm = context.getPackageManager();
    Drawable Icon;
    try {
      Icon =
          pm.getApplicationIcon(packageName);
    } catch (PackageManager.NameNotFoundException e) {
      Icon = null;
    }
    return Icon;
  }
}
