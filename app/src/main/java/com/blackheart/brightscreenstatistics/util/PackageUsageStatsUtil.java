package com.blackheart.brightscreenstatistics.util;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import java.util.List;

public class PackageUsageStatsUtil {
  private Context context;

  public PackageUsageStatsUtil(Context context) {
    this.context = context;
  }

  public boolean isHasOption() {
    PackageManager packageManager = context.getApplicationContext()
        .getPackageManager();
    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
    List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
        PackageManager.MATCH_DEFAULT_ONLY);
    return list.size() > 0;
  }

  public boolean isHasSwitch() {
    long ts = System.currentTimeMillis();
    UsageStatsManager usageStatsManager = (UsageStatsManager)context.getApplicationContext()
        .getSystemService(Context.USAGE_STATS_SERVICE);
    List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_BEST, 0, ts);
    if (queryUsageStats == null || queryUsageStats.isEmpty()) {
      return false;
    }
    return true;
  }



}
