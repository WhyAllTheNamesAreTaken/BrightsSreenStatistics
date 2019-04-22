package com.blackheart.brightscreenstatistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.blackheart.brightscreenstatistics.service.AppTimeService;
import com.blackheart.brightscreenstatistics.service.ServiceKeepService;

public class AppTimeReceiver extends BroadcastReceiver {
public static final String TAG="AppTimeReceiver";
  @Override
  public void onReceive(Context context, Intent intent) {
    // TODO: This method is called when the BroadcastReceiver is receiving
    // an Intent broadcast.
    Log.e(TAG,"onReceive");
    if (intent.getAction().equals("Service Destroy")) {
      //TODO
      //在这里写又一次启动service的相关操作

      //两个service互相启动
      Intent serviceIntent1 = new Intent(context, AppTimeService.class);
      Intent serviceIntent2= new Intent(context, ServiceKeepService.class);
      if (Build.VERSION.SDK_INT >= 26) {//Android8.0
        context.startForegroundService(serviceIntent1);
        context.startForegroundService(serviceIntent2);
      } else {
        context.startService(serviceIntent1);
        context.startService(serviceIntent2);
      }
    }
  }
}
